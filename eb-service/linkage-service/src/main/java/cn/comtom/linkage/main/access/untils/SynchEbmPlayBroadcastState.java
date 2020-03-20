package cn.comtom.linkage.main.access.untils;

import cn.comtom.domain.core.constants.FlowConstants;
import cn.comtom.domain.core.ebm.info.EbmBrdRecordInfo;
import cn.comtom.domain.core.ebm.info.EbmDispatchInfo;
import cn.comtom.domain.core.ebm.info.EbmInfo;
import cn.comtom.domain.core.ebm.request.EbmUpdateRequest;
import cn.comtom.domain.core.flow.info.DispatchFlowInfo;
import cn.comtom.domain.core.scheme.info.SchemeEbrInfo;
import cn.comtom.domain.core.scheme.info.SchemeInfo;
import cn.comtom.linkage.commons.BroadcastStateEnum;
import cn.comtom.linkage.main.common.service.ICommonService;
import cn.comtom.linkage.main.fegin.service.ICoreFeginService;
import cn.comtom.tools.constants.Constants;
import cn.comtom.tools.enums.StateDictEnum;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * @author:WJ
 * @date: 2018/12/13 0013
 * @time: 下午 2:02
 * 维护bc_ebm表中的播发状态字段
 */
@Component
public class SynchEbmPlayBroadcastState {


    @Autowired
    private ICoreFeginService coreFeginService;


    @Autowired
    private ICommonService commonService;


    /**
     * 通过ebmId维护对应的播发状态字段
     * 目前三种场景触发
     * 1、下级播发状态反馈
     * 2、下级播发记录反馈
     * 3、上级播发状态请求
     *
     * @param ebmId
     */
    public void updateEbmBroadcastStateByEbmId(String ebmId) {

        EbmInfo ebmInfo = coreFeginService.getEbmInfoById(ebmId);

        /*List<EbmBrdRecordInfo> ebmBrdRecordList = coreFeginService.getEbmBrdRecordListByEbmId(ebmId);

        //如果是取消不做任何处理
        if (ebmInfo.getBroadcastState() != BroadcastStateEnum.cancelled.getCode()) {
            long startTime = ebmInfo.getStartTime().getTime();
            long endTime = ebmInfo.getEndTime().getTime();
            long curTime = new Date().getTime();
            //1如果当前时间小于播发开始请求，代表还未分发，ebmInfo中的boradcastState状态应该未（等待播发），此时不做处理
//            if (curTime < startTime) {
//                ebmInfo.setBroadcastState(BroadcastStateEnum.timetogo.getCode());
//            } else if (startTime < curTime && endTime > curTime) {// 开始时间小于当前时间并且在结束时间内则反馈播发中状态
//                ebmInfo.setBroadcastState(BroadcastStateEnum.inprogress.getCode());
//            }else{//代表已经播发结束了，这个时候需要通过计算得出当前播发状态
               // BroadcastStateEnum stateEnum = getBrdState(ebmInfo.getSchemeId(), ebmBrdRecordList);//测试大喇叭管控临时注释
               // ebmInfo.setBroadcastState(stateEnum.getCode());
            //}

            EbmUpdateRequest updateRequest = new EbmUpdateRequest();
            BeanUtils.copyProperties(ebmInfo, updateRequest);
            coreFeginService.updateEbm(updateRequest);
        }
		*/
        // 2019/2/25 0025  更新流程===========================
        Integer stateCode = ebmInfo.getBroadcastState();
        if (BroadcastStateEnum.noprogress.getCode().intValue() != stateCode.intValue()&&BroadcastStateEnum.timetogo.getCode().intValue()!=stateCode.intValue()) {
            // 获取调度流程编号
            String flowId = ebmInfo.getFlowId();
            String flowStage = null;
            String flowState = null;
            switch (stateCode) {
                case 2:
                    flowStage = FlowConstants.STAGE_PLAYING;
                    flowState = FlowConstants.STATE_PLAYING;
                    break;
                case 3:
                    flowStage = FlowConstants.STAGE_COMPLETE;
                    flowState = FlowConstants.STATE_COMPLETE_SUCCESS;
                    break;
                case 4:
                    flowStage = FlowConstants.STAGE_COMPLETE;
                    flowState = FlowConstants.STATE_COMPLETE_FAILE;
                    break;
                case 5:
                    flowStage = FlowConstants.STAGE_COMPLETE;
                    flowState = FlowConstants.STATE_COMPLETE_CANCEL;
                    break;
                default:
                    flowStage = FlowConstants.STAGE_PLAYING;
                    flowState = FlowConstants.STATE_PLAYING_WAIT;
                    break;
            }
            commonService.updateDispatchFlowState(flowId, flowStage, flowState);
        }


    }

    /**
     * 根据播发记录确定当前信息播发状态 主要是返回播发成功还是播发失败
     *
     * @param schemeId
     * @param ebmBrdRecords
     * @return
     */
    public BroadcastStateEnum getBrdState(String schemeId, List<EbmBrdRecordInfo> ebmBrdRecords) {
        List<SchemeEbrInfo> schemeEbrList = coreFeginService.findSchemeEbrListBySchemeId(schemeId);
        if (CollectionUtils.isEmpty(schemeEbrList)) {
            return BroadcastStateEnum.noprogress;
        }

        if (ebmBrdRecords == null || ebmBrdRecords.isEmpty()) {
            SchemeInfo schemeInfo = coreFeginService.getSchemeInfoById(schemeId);

            //判断已分发的消息是否有分发失败并且超过重发次数的。如果有则为播放失败，否则为等待播发
            List<EbmDispatchInfo> ebmDispatchInfos = coreFeginService.getEbmDispatchByEbmId(schemeInfo.getEbmId());
            String sysParam = commonService.getSysParamValue(Constants.MAX_SEND_RETRY_TIMES);
            Integer retryTimes = StringUtils.isBlank(sysParam) ? 0 : Integer.valueOf(sysParam);
            Boolean failed = Optional.ofNullable(ebmDispatchInfos).orElse(Collections.emptyList()).stream()
                    .filter(Objects::nonNull)
                    .anyMatch(ebmDispatchInfo ->
                       StateDictEnum.DISPATCH_STATE_FAILED.getKey().equals(ebmDispatchInfo.getState())
                               && ebmDispatchInfo.getFailCount() >= retryTimes
                    );

            if (StringUtils.isNotBlank(schemeInfo.getFlowId())) {
                DispatchFlowInfo dispatchFlowInfo = coreFeginService.getDispatchFlowById(schemeInfo.getFlowId());
                /*
                没有播发记录，
                分发流程已经到了消息分发以后的阶段，并且不是播发等待时，默认设置为播放失败
                if (FlowConstants.STATE_PLAYING_WAIT.equals(dispatchFlowInfo.getFlowState())) {
                    return BroadcastStateEnum.timetogo;
                }else if(Integer.valueOf(dispatchFlowInfo.getFlowState()) >= Integer.valueOf(FlowConstants.STATE_MSG_SEND)){
                    return BroadcastStateEnum.failed;
                }*/
                if(failed){
                    return BroadcastStateEnum.failed;
                }else if(Integer.valueOf(dispatchFlowInfo.getFlowState()) >= Integer.valueOf(FlowConstants.STATE_MSG_SEND)){
                    return BroadcastStateEnum.timetogo;
                }
            }
            return BroadcastStateEnum.noprogress;
        }
        List<String> ebrIdList = new ArrayList<String>();
        for (SchemeEbrInfo schemeEbr : schemeEbrList) {
            String ebrId = schemeEbr.getEbrId();
            ebrIdList.add(ebrId);
        }
        for (EbmBrdRecordInfo ebmBrdRecord : ebmBrdRecords) {
            String ebrId = ebmBrdRecord.getResourceId();
            if (ebrIdList.contains(ebrId)) {
                Integer stateCode = ebmBrdRecord.getBrdStateCode();
                if (BroadcastStateEnum.failed.getCode().equals(stateCode)) {
                    return BroadcastStateEnum.failed;
                } else if (BroadcastStateEnum.inprogress.getCode().equals(stateCode)) {
                    return BroadcastStateEnum.inprogress;
                } else if (BroadcastStateEnum.cancelled.getCode().equals(stateCode)) {
                    return BroadcastStateEnum.cancelled;
                }
            }
        }
        return BroadcastStateEnum.succeeded;
    }


    /**
     * 维护下发EBM数据的状态信息
     *
     * @param orgEbmId
     * @param stateCode
     */
    public void updateNewEbmBroadcastStateByEbmId(String orgEbmId, Integer stateCode) {
        EbmInfo ebmInfo = coreFeginService.getEbmInfoById(orgEbmId);
        EbmUpdateRequest updateRequest = new EbmUpdateRequest();
        BeanUtils.copyProperties(ebmInfo, updateRequest);
        updateRequest.setBroadcastState(stateCode);
        coreFeginService.updateEbm(updateRequest);
    }

    /**
     * 判断当前ebm下发数据是否已经全部反馈，或者已经播发结束了（未全部反馈也需要主动上报）,具体逻辑后续实现
     *
     * @param ebmId
     * @return
     */
    public boolean needToBack(String ebmId) {


        return true;
    }
}
