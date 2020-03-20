package cn.comtom.system.main.plan.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;

import cn.comtom.domain.system.plan.info.SysPlanAreaInfo;
import cn.comtom.domain.system.plan.info.SysPlanMatchInfo;
import cn.comtom.domain.system.plan.info.SysPlanResoRefInfo;
import cn.comtom.domain.system.plan.info.SysPlanSeverityInfo;
import cn.comtom.domain.system.plan.request.SysPlanAreaAddRequest;
import cn.comtom.domain.system.plan.request.SysPlanMatchAddRequest;
import cn.comtom.domain.system.plan.request.SysPlanMatchUpdRequest;
import cn.comtom.domain.system.plan.request.SysPlanPageRequest;
import cn.comtom.domain.system.plan.request.SysPlanResoRefAddRequest;
import cn.comtom.domain.system.plan.request.SysPlanSeverityAddRequest;
import cn.comtom.system.fw.BaseDao;
import cn.comtom.system.fw.BaseServiceImpl;
import cn.comtom.system.main.plan.dao.SysPlanMatchDao;
import cn.comtom.system.main.plan.entity.dbo.SysPlanArea;
import cn.comtom.system.main.plan.entity.dbo.SysPlanMatch;
import cn.comtom.system.main.plan.entity.dbo.SysPlanResoRef;
import cn.comtom.system.main.plan.entity.dbo.SysPlanSeverity;
import cn.comtom.system.main.plan.service.ISysPlanAreaService;
import cn.comtom.system.main.plan.service.ISysPlanMatchService;
import cn.comtom.system.main.plan.service.ISysPlanResRefService;
import cn.comtom.system.main.plan.service.ISysPlanSeverityService;
import cn.comtom.tools.constants.SymbolConstants;
import cn.comtom.tools.enums.StateDictEnum;
import cn.comtom.tools.utils.UUIDGenerator;

@Service
public class SysPlanMatchServiceImpl extends BaseServiceImpl<SysPlanMatch,String> implements ISysPlanMatchService {

    @Autowired
    private SysPlanMatchDao dao;

    @Autowired
    private ISysPlanResRefService sysPlanResRefService;

    @Autowired
    private ISysPlanAreaService sysPlanAreaService;

    @Autowired
    private ISysPlanSeverityService sysPlanSeverityService;

    @Override
    public BaseDao<SysPlanMatch, String> getDao() {
        return dao;
    }

    @Override
    public List<SysPlanMatchInfo> getMatchPlan(String severity,String eventType, String srcEbrId, String areaCodes) {
    	String[] severityArr = null;
    	String[] areaCodeArr = null;
    	if(StringUtils.isNotBlank(severity)) {
    		severityArr = severity.split(SymbolConstants.GENERAL_SEPARATOR);
    	}
    	if(StringUtils.isNotBlank(areaCodes)) {
    		areaCodeArr = areaCodes.split(SymbolConstants.GENERAL_SEPARATOR);
    	}
		if(StringUtils.isNotBlank(srcEbrId)) { 
			String[] srcEbrIds = srcEbrId.split(SymbolConstants.GENERAL_SEPARATOR);
			List<String> ebrList = Lists.newArrayList(srcEbrIds);
			Collections.sort(ebrList);
			srcEbrId = String.join(",", ebrList);
		}
		
        List<SysPlanMatchInfo> tempList = dao.getMatchedPlan(severityArr, eventType, srcEbrId, areaCodeArr);
        return Optional.ofNullable(tempList).orElse(Collections.emptyList()).stream().collect(Collectors.toList());
    }

    @Override
    public List<SysPlanMatchInfo> page(SysPlanPageRequest pageRequest) {
        List<SysPlanMatchInfo> list=dao.page(pageRequest);
        for(SysPlanMatchInfo info:list){
            List<SysPlanResoRefInfo> refList = sysPlanResRefService.getByPlanId(info.getPlanId());
            info.setRefList(refList);
            List<SysPlanAreaInfo> areaList = sysPlanAreaService.getByPlanId(info.getPlanId());
            info.setAreaList(areaList);
            List<SysPlanSeverityInfo> severityList = sysPlanSeverityService.getByPlanId(info.getPlanId());
            info.setSeverityList(severityList);
        }
        return list;
    }
//
//    private String getResoName(List<SysPlanResoRefInfo> refList) {
//        StringBuffer sb=new StringBuffer("");
//        for(SysPlanResoRefInfo info:refList){
//            sb.append(info.getResoName()).append(",");
//        }
//        if(sb.toString().length()>0){
//            return sb.toString().substring(0,sb.toString().length()-1);
//        }
//        return "";
//    }

    @Override
    public void delete(String planId) {
        //逻辑删除 更改status为0
        SysPlanMatch sysPlanMatch = dao.selectById(planId);
        sysPlanMatch.setStatus("0");
        dao.update(sysPlanMatch);
    }

    @Override
    public SysPlanMatch saveSysPlanMath(SysPlanMatchAddRequest request) {
        //1.保存预案信息
        SysPlanMatch sysPlanMatch=new SysPlanMatch();
        BeanUtils.copyProperties(request,sysPlanMatch);
        sysPlanMatch.setPlanId(UUIDGenerator.getUUID());
        if(sysPlanMatch.getCreateTime() == null){
            sysPlanMatch.setCreateTime(new Date());
        }
        if(StringUtils.isBlank(sysPlanMatch.getStatus())){
            sysPlanMatch.setStatus(StateDictEnum.PLAN_STATUS_NORMAL.getKey());
        }
        dao.save(sysPlanMatch);

        //2.保存关联数据
        bathSaveSysResoRef(request.getRefList(),sysPlanMatch.getPlanId());
        if(CollectionUtils.isNotEmpty(request.getSeverityList()))
           bathSaveSysSeverity(request.getSeverityList(),sysPlanMatch.getPlanId());
        if(CollectionUtils.isNotEmpty(request.getAreaList()))
           bathSaveSysArea(request.getAreaList(),sysPlanMatch.getPlanId());

        return sysPlanMatch;
    }

    private void bathSaveSysArea(List<SysPlanAreaAddRequest> areaList, String planId) {
        List<SysPlanArea> list=new ArrayList<SysPlanArea>();
        for (SysPlanAreaAddRequest addRequest:areaList){
            SysPlanArea sysPlanArea=new SysPlanArea();
            BeanUtils.copyProperties(addRequest,sysPlanArea);
            sysPlanArea.setPlanId(planId);
            list.add(sysPlanArea);
        }
        sysPlanAreaService.saveList(list);
    }

    private void bathSaveSysSeverity(List<SysPlanSeverityAddRequest> severityList, String planId) {
        List<SysPlanSeverity> list=new ArrayList<SysPlanSeverity>();
        for (SysPlanSeverityAddRequest addRequest:severityList){
            SysPlanSeverity sysPlanSeverity=new SysPlanSeverity();
            BeanUtils.copyProperties(addRequest,sysPlanSeverity);
            sysPlanSeverity.setPlanId(planId);
            list.add(sysPlanSeverity);
        }
        sysPlanSeverityService.saveList(list);
    }

    private void bathSaveSysResoRef(List<SysPlanResoRefAddRequest> refList,String planId) {
        List<SysPlanResoRef> list=new ArrayList<SysPlanResoRef>();
        for(SysPlanResoRefAddRequest addRequest:refList){
            SysPlanResoRef ref=new SysPlanResoRef();
            BeanUtils.copyProperties(addRequest,ref);
            ref.setPlanId(planId);
            ref.setRefId(UUIDGenerator.getUUID());
            list.add(ref);
        }
        sysPlanResRefService.saveList(list);
    }

    @Override
    public SysPlanMatch updateSysPlanMath(SysPlanMatchUpdRequest request) {

        //1.逻辑删除
        delete(request.getPlanId());

        //2.重新新增
        SysPlanMatchAddRequest addRequest=new SysPlanMatchAddRequest();
        BeanUtils.copyProperties(request,addRequest);
        SysPlanMatch sysPlanMatch = saveSysPlanMath(addRequest);
        return sysPlanMatch;
    }

    @Override
    public SysPlanMatchInfo getByPlanId(String planId) {
        SysPlanMatch sysPlanMatch = dao.selectById(planId);
        SysPlanMatchInfo info=new SysPlanMatchInfo();
        BeanUtils.copyProperties(sysPlanMatch,info);
        List<SysPlanResoRefInfo> refList = sysPlanResRefService.getByPlanId(planId);
        info.setRefList(refList);
        List<SysPlanAreaInfo> areaList = sysPlanAreaService.getByPlanId(planId);
        info.setAreaList(areaList);
        List<SysPlanSeverityInfo> severityList = sysPlanSeverityService.getByPlanId(planId);
        info.setSeverityList(severityList);
        return info;
    }
}
