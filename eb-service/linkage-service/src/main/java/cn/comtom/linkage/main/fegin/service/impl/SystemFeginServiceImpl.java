package cn.comtom.linkage.main.fegin.service.impl;

import cn.comtom.domain.system.accessnode.info.AccessNodeInfo;
import cn.comtom.domain.system.log.info.SysOperateLogInfo;
import cn.comtom.domain.system.log.request.SysOperateLogAddRequest;
import cn.comtom.domain.system.plan.info.SysPlanMatchInfo;
import cn.comtom.domain.system.plan.info.SysPlanResoRefInfo;
import cn.comtom.domain.system.region.info.RegionAreaInfo;
import cn.comtom.domain.system.sysparam.info.SysParamsInfo;
import cn.comtom.linkage.main.fegin.SystemFegin;
import cn.comtom.linkage.main.fegin.service.FeginBaseService;
import cn.comtom.linkage.main.fegin.service.ISystemFeginService;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class SystemFeginServiceImpl extends FeginBaseService implements ISystemFeginService {

    @Autowired
    private SystemFegin systemFegin;

    @Override
    public Boolean isWhite(String reqIp) {
        return getBoolean(systemFegin.isWhite(reqIp));
    }

    @Override
    public AccessNodeInfo getByPlatformId(String ebrid) {
        return getData(systemFegin.getByPlatformId(ebrid));
    }

    @Override
    public List<SysPlanMatchInfo> getMatchPlan(String eventLevel,String eventType, String srcEbrId, String areaCode) {
        return getDataList(systemFegin.getMatchPlan(eventLevel,eventType,srcEbrId,areaCode));
    }

    @Override
    public List<SysPlanResoRefInfo> getPlanResRefByPlanId(String planId) {
        return getDataList(systemFegin.getPlanResRefByPlanId(planId));
    }

    @Override
    public List<RegionAreaInfo> getRegionAreaByAreaCodes(List<String> areaCodeList) {
        return getDataList(systemFegin.getRegionAreaByAreaCodes(areaCodeList));
    }

    @Override
    public RegionAreaInfo getRegionAreaByAreaCode(String areaCode) {
        return getData(systemFegin.getRegionAreaByAreaCode(areaCode));
    }

    @Override
    public SysOperateLogInfo saveSysOperateLog(SysOperateLogAddRequest request) {
        return getData(systemFegin.saveSysOperateLog(request));
    }
    
    @Override
    public String getByKey(String paramKey) {
        SysParamsInfo data = getData(systemFegin.getByKey(paramKey));
        if(data!=null && StringUtils.isNotBlank(data.getParamValue())) {
        	return data.getParamValue();
        }
        return null;
    }
}
