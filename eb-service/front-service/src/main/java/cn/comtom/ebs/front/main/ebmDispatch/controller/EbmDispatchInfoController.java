package cn.comtom.ebs.front.main.ebmDispatch.controller;

import cn.comtom.domain.core.constants.FlowConstants;
import cn.comtom.domain.core.ebd.info.EbdInfo;
import cn.comtom.domain.core.ebm.info.EbmDispatchInfoInfo;
import cn.comtom.domain.core.ebm.info.EbmInfo;
import cn.comtom.domain.core.ebm.request.EbmDispatchInfoAuditRequest;
import cn.comtom.domain.core.ebm.request.EbmDispatchInfoUpdateRequest;
import cn.comtom.domain.core.flow.request.DispatchFlowUpdateRequest;
import cn.comtom.ebs.front.common.AuthRest;
import cn.comtom.ebs.front.common.FrontErrorEnum;
import cn.comtom.ebs.front.fegin.service.ICoreFeginService;
import cn.comtom.ebs.front.fw.AuthController;
import cn.comtom.ebs.front.main.ebmDispatch.service.IEbmDispatchInfoService;
import cn.comtom.ebs.front.main.core.service.IEbdService;
import cn.comtom.ebs.front.main.core.service.IEbmService;
import cn.comtom.ebs.front.main.mqListener.MQMessageProducer;
import cn.comtom.tools.constants.MqQueueConstant;
import cn.comtom.tools.enums.CommonDictEnum;
import cn.comtom.tools.enums.StateDictEnum;
import cn.comtom.tools.response.ApiEntityResponse;
import cn.comtom.tools.response.ApiResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;

/**
 * @author:WJ
 * @date: 2019/1/21 0021
 * @time: 下午 2:01
 */
@AuthRest
@RestController
@RequestMapping("/safeRest/ebm/dispatchInfo")
@Api(tags = "消息专题")
public class EbmDispatchInfoController extends AuthController {

    @Autowired
    private IEbmDispatchInfoService ebmDispatchInfoService;

    @Autowired
    private IEbmService ebmService;

    @Autowired
    private ICoreFeginService coreFeginService;

    @Autowired
    private IEbdService ebdService;

    @Autowired
    private MQMessageProducer mqMessageProducer;

    @PutMapping("/audit")
    @ApiOperation(value = "审核", notes = "审核")
    public ApiResponse audit(@RequestBody @Valid EbmDispatchInfoAuditRequest request, BindingResult bindResult) {
        EbmDispatchInfoInfo info = ebmDispatchInfoService.getEbmDispatchInfoByEbmId(request.getEbmId());
        if (info != null) {
            //1.更新表结构
            EbmDispatchInfoUpdateRequest updateRequest = new EbmDispatchInfoUpdateRequest();
            BeanUtils.copyProperties(info, updateRequest);
            BeanUtils.copyProperties(request, updateRequest);
            updateRequest.setAuditTime(new Date());
            updateRequest.setAuditUser(this.getUserId());
            ebmDispatchInfoService.updateEbmDispatchInfo(updateRequest);

            //2.审核通过触发MQ调用分发
            if (request.getAuditResult().equals(StateDictEnum.AUDIT_STATUS_PASS.getKey())) {
                //通过ebmId查询需要发送的ebd数据包
                List<EbdInfo> ebdInfoList = ebdService.getEbdInfoByEbmId(request.getEbmId());
                for (EbdInfo ebd : ebdInfoList) {
                    if (ebd.getSendFlag().equals(CommonDictEnum.FLAG_SEND.getKey())) {
                        //调用MQ产生一条状态反馈的消息，由联动服务去消费
                        mqMessageProducer.sendData(MqQueueConstant.EBM_DISPATCH_QUEUE, ebd.getEbdId());
                    }
                }

                //更新流程状态 为 消息分发
                ApiEntityResponse<EbmInfo> response = ebmService.getEbmInfoById(info.getEbmId());
                if (response.getSuccessful()) {
                    EbmInfo ebmInfo = response.getData();
                    DispatchFlowUpdateRequest dispatchFlowUpdateRequest = new DispatchFlowUpdateRequest();
                    dispatchFlowUpdateRequest.setFlowId(ebmInfo.getFlowId());
                    dispatchFlowUpdateRequest.setFlowStage(FlowConstants.STAGE_PROCESS);               //预警阶段 预警处理
                    dispatchFlowUpdateRequest.setFlowState(FlowConstants.STATE_MSG_SEND);
                    dispatchFlowUpdateRequest.setUpdateTime(new Date());
                    coreFeginService.updateDispatchFlow(dispatchFlowUpdateRequest);
                }

            }
        } else {
            ApiResponse.error(FrontErrorEnum.EBMDISPATCHINFO_IS_NOT_EXIST.getCode(), FrontErrorEnum.EBMDISPATCHINFO_IS_NOT_EXIST.getMsg());
        }
        return ApiResponse.ok();
    }

}
