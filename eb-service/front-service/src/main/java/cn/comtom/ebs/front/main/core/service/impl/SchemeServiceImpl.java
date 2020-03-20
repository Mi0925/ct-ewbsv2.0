package cn.comtom.ebs.front.main.core.service.impl;

import cn.comtom.domain.core.constants.FlowConstants;
import cn.comtom.domain.core.ebd.info.EbdInfo;
import cn.comtom.domain.core.ebm.info.EbmDispatchInfo;
import cn.comtom.domain.core.ebm.info.EbmInfo;
import cn.comtom.domain.core.ebm.request.EbmDispatchAddBatchRequest;
import cn.comtom.domain.core.ebm.request.EbmDispatchInfoAddRequest;
import cn.comtom.domain.core.flow.request.DispatchFlowUpdateRequest;
import cn.comtom.domain.core.scheme.info.SchemeEbrInfo;
import cn.comtom.domain.core.scheme.info.SchemeInfo;
import cn.comtom.domain.core.scheme.request.SchemeEbrAddBatchRequest;
import cn.comtom.domain.core.scheme.request.SchemeUpdateRequest;
import cn.comtom.domain.reso.ebr.info.EbrAdapterInfo;
import cn.comtom.domain.reso.ebr.info.EbrBroadcastInfo;
import cn.comtom.domain.reso.ebr.info.EbrPlatformInfo;
import cn.comtom.domain.reso.ebr.info.EbrStationInfo;
import cn.comtom.domain.system.sysparam.info.SysParamsInfo;
import cn.comtom.ebs.front.fegin.CoreFegin;
import cn.comtom.ebs.front.fegin.service.ICoreFeginService;
import cn.comtom.ebs.front.fegin.service.ISystemFeginService;
import cn.comtom.ebs.front.main.core.service.ISchemeService;
import cn.comtom.tools.constants.Constants;
import cn.comtom.tools.enums.CommonDictEnum;
import cn.comtom.tools.enums.ResTypeDictEnum;
import cn.comtom.tools.enums.StateDictEnum;
import cn.comtom.tools.utils.DateUtil;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SchemeServiceImpl implements ISchemeService {

    @Autowired
    private ICoreFeginService coreFeginService;

    @Autowired
    private CoreFegin coreFegin;

    @Autowired
    private ISystemFeginService systemFeginService;

    @Override
    public String getEbmDispatchInfoTitle(String msgTitle) {
        return DateUtil.format(new Date(),DateUtil.DATE_PATTERN.YYYYMMDD).concat(msgTitle).concat(Constants.EBM_DIAPATCH_INFO_TITLE_SUFFIX);
    }

    @Override
    public Boolean auditSchemeInfo(SchemeUpdateRequest request) {
        Boolean success = coreFeginService.updateSchemeInfo(request);
        //如果是审核通过，继续下面逻辑
        if(StateDictEnum.AUDIT_STATUS_PASS.getKey().equals(request.getAuditResult())){
        	//修改调度方案资源,先删除，再新增
        	if(CollectionUtils.isNotEmpty(request.getRefEbrList())) {
        		coreFeginService.deleteSchemeEbr(request.getSchemeId());
        		SchemeEbrAddBatchRequest schemeEbrAddBatchRequest = new SchemeEbrAddBatchRequest();
        		request.getRefEbrList().forEach(item->{
        			item.setSchemeId(request.getSchemeId());
        		});
        		schemeEbrAddBatchRequest.setSchemeEbrInfoList(request.getRefEbrList());
        		coreFeginService.saveSchemeEbrBatch(schemeEbrAddBatchRequest);
        	}
            //审核通过，将在分发表中插入分发数据
            SchemeInfo schemeInfo = coreFeginService.getSchemeInfoById(request.getSchemeId());
            EbmInfo ebmInfo = coreFeginService.getEbmInfoById(schemeInfo.getEbmId());
            Date startTime = ebmInfo.getStartTime();
            List<SchemeEbrInfo> schemeEbrInfoList = coreFeginService.findSchemeEbrListBySchemeId(request.getSchemeId());
            List<EbmDispatchInfo> ebmDispatchInfoList = Optional.ofNullable(schemeEbrInfoList).orElse(Collections.emptyList()).stream()
                    .map(schemeEbrInfo -> {
                        EbmDispatchInfo ebmDispatchInfo = new EbmDispatchInfo();
                        ebmDispatchInfo.setFailCount(0);
                        ebmDispatchInfo.setEbmId(schemeInfo.getEbmId());
                        ebmDispatchInfo.setDispatchTime(new Date());
                        ebmDispatchInfo.setBrdSysType(schemeEbrInfo.getEbrType());
                        ebmDispatchInfo.setState(StateDictEnum.DISPATCH_STATE_READY.getKey());
                        List<EbdInfo> ebdInfoList = coreFeginService.getEbdInfoByEbmId(schemeInfo.getEbmId());
                        Optional.ofNullable(ebdInfoList).orElse(Collections.emptyList())
                                .forEach(ebdInfo -> {
                                    if(CommonDictEnum.FLAG_RECEIVE.getKey().equals(ebdInfo.getSendFlag())){
                                        ebmDispatchInfo.setEbdId(ebdInfo.getEbdId());
                                    }
                                });
                        //根据资源类型，分别获取不同类型的播出资源的信息
        				if(Constants.EBR_SUB_SOURCE_TYPE_PLATEFORM.equals(schemeEbrInfo.getEbrType())){
        					//播出平台
        					ebmDispatchInfo.setPsEbrId(schemeEbrInfo.getEbrId());
        				}
        				//播出系统
        				if(Constants.EBR_SUB_SOURCE_TYPE_BROADCAST.equals(schemeEbrInfo.getEbrType())){
        					//播出系统编码
        					ebmDispatchInfo.setBsEbrId(schemeEbrInfo.getEbrId());
        				}
        				//台站
        				if(Constants.EBR_SUB_SOURCE_TYPE_STATION.equals(schemeEbrInfo.getEbrType())){
        					//台站系统编码
        					ebmDispatchInfo.setStEbrId(schemeEbrInfo.getEbrId());
        				}
        				//适配器
        				if(Constants.EBR_SUB_SOURCE_TYPE_ADAPTOR.equals(schemeEbrInfo.getEbrType())){
        					//适配器系统编码
        					ebmDispatchInfo.setAsEbrId(schemeEbrInfo.getEbrId());
        				}
        				ebmDispatchInfo.setPlayTime(startTime);
                        return  ebmDispatchInfo;
                    }).collect(Collectors.toList());
            EbmDispatchAddBatchRequest ebmDispatchAddBatchRequest = new EbmDispatchAddBatchRequest();
            ebmDispatchAddBatchRequest.setEbmDispatchInfoList(ebmDispatchInfoList);
            coreFeginService.saveEbmDispatchBatch(ebmDispatchAddBatchRequest);

            DispatchFlowUpdateRequest dispatchFlowUpdateRequest = new DispatchFlowUpdateRequest();
            dispatchFlowUpdateRequest.setFlowId(schemeInfo.getFlowId());
            dispatchFlowUpdateRequest.setFlowStage(FlowConstants.STAGE_PROCESS);               //预警阶段 预警处理
            dispatchFlowUpdateRequest.setFlowState(FlowConstants.STATE_MSG_AUDIT);              //流程节点 消息审核

            //生成并保存Ebm分发消息
            EbmDispatchInfoAddRequest ebmDispatchInfoAddRequest = new EbmDispatchInfoAddRequest();
            ebmDispatchInfoAddRequest.setEbmId(ebmInfo.getEbmId());
            ebmDispatchInfoAddRequest.setInfoTitle(getEbmDispatchInfoTitle(ebmInfo.getMsgTitle()));
            SysParamsInfo sysParamsInfo = systemFeginService.getByKey(Constants.EBM_DISPATCH_INFO_AUTO_AUDIT);
            if(sysParamsInfo != null && StringUtils.isNotBlank(sysParamsInfo.getParamValue())
                    && CommonDictEnum.EBM_DISPATCH_INFO_AUTO_AUDIT_TRUE.getKey().equals(sysParamsInfo.getParamValue())){
                //自动审核
                ebmDispatchInfoAddRequest.setAuditResult(StateDictEnum.AUDIT_STATUS_PASS.getKey());
                ebmDispatchInfoAddRequest.setAuditTime(new Date());
                ebmDispatchInfoAddRequest.setAuditUser(CommonDictEnum.EBM_DEFAULT_USER.getKey());
                //消息自动审核通过，修改流程为消息分发
                dispatchFlowUpdateRequest.setFlowState(FlowConstants.STATE_MSG_SEND);
            }
            coreFeginService.saveEbmDispatchInfoInfo(ebmDispatchInfoAddRequest);

            //更新流程状态
            dispatchFlowUpdateRequest.setUpdateTime(new Date());
            coreFeginService.updateDispatchFlow(dispatchFlowUpdateRequest);
        }
        return success;
    }
}
