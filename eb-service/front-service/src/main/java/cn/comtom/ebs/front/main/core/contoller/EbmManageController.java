package cn.comtom.ebs.front.main.core.contoller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;

import cn.comtom.domain.core.ebd.info.EbdInfo;
import cn.comtom.domain.core.ebm.info.EbmAuxiliaryInfo;
import cn.comtom.domain.core.ebm.info.EbmDispatchInfo;
import cn.comtom.domain.core.ebm.info.EbmDispatchInfoInfo;
import cn.comtom.domain.core.ebm.info.EbmInfo;
import cn.comtom.domain.core.ebm.request.EbmAuditRequest;
import cn.comtom.domain.core.ebm.request.EbmHandBack;
import cn.comtom.domain.core.ebm.request.EbmPageRequest;
import cn.comtom.domain.core.flow.info.DispatchFlowInfo;
import cn.comtom.domain.core.scheme.info.SchemeEbrInfo;
import cn.comtom.domain.core.scheme.info.SchemeInfo;
import cn.comtom.domain.reso.ebr.info.EbrInfo;
import cn.comtom.domain.reso.file.info.OriginFileInfo;
import cn.comtom.domain.system.plan.info.SysPlanMatchInfo;
import cn.comtom.ebs.front.common.FrontErrorEnum;
import cn.comtom.ebs.front.fegin.service.ICoreFeginService;
import cn.comtom.ebs.front.fegin.service.IResoFeginService;
import cn.comtom.ebs.front.fegin.service.ISystemFeginService;
import cn.comtom.ebs.front.fw.AuthController;
import cn.comtom.ebs.front.main.core.model.EbmInfoAll;
import cn.comtom.ebs.front.main.core.model.SchemeInfoAll;
import cn.comtom.ebs.front.main.core.service.IEbmService;
import cn.comtom.ebs.front.main.mqListener.MQMessageProducer;
import cn.comtom.ebs.front.main.oauth2.Permissions;
import cn.comtom.tools.constants.MqQueueConstant;
import cn.comtom.tools.constants.RedisKeyConstants;
import cn.comtom.tools.enums.CommonDictEnum;
import cn.comtom.tools.mq.message.EbmStateResponseMessage;
import cn.comtom.tools.response.ApiEntityResponse;
import cn.comtom.tools.response.ApiPageResponse;
import cn.comtom.tools.response.ApiResponse;
import cn.comtom.tools.response.ApiResponseBuilder;
import cn.comtom.tools.utils.EntityUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * @author:WJ
 * @date: 2018/12/25 0025
 * @time: 下午 7:38
 */

@Slf4j
@RestController
@RequestMapping("/safeRest/ebm")
@Api(tags = "接入信息管理", description = "接入信息管理")
public class EbmManageController extends AuthController {

    @Autowired
    private MQMessageProducer mqMessageProducer;
    @Autowired
    private IEbmService ebmService;
    @Autowired
    private ICoreFeginService coreFeginService;
    @Autowired
    private IResoFeginService resoFeginService;
    @Autowired
    private ISystemFeginService systemFeginService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;


    @GetMapping("/page")
    @ApiOperation(value = "分页查询预警接入信息记录", notes = "分页查询预警接入信息记录")
    @RequiresPermissions(Permissions.CORE_EBM_QUERY)
    public ApiPageResponse<EbmInfo> page(@Valid EbmPageRequest req){
        if(log.isDebugEnabled()){
            log.debug("[!~] get ebm info by page. req=[{}]",req);
        }
        if(StringUtils.isBlank(req.getSidx())) {
        	req.setOrder("desc");
        	req.setSidx("createTime");
        }
        req.setMsgType("1");
        return ebmService.getEbmInfoByPage(req);
    }

    @GetMapping("/{ebmId}")
    @ApiOperation(value = "根据ID查询预警接入信息记录", notes = "根据ID查询预警接入信息记录")
    public ApiEntityResponse<EbmInfo> getById(@PathVariable(name = "ebmId") String ebmId){
        if(log.isDebugEnabled()){
            log.debug("[!~] get ebm info by ebmId. ebmId=[{}]",ebmId);
        }
        return ebmService.getEbmInfoById(ebmId);
    }

    @GetMapping("/getEbmInfoAll")
    @ApiOperation(value = "查询预警接入信息及其关联信息记录", notes = "查询预警接入信息及其关联信息记录")
    public ApiEntityResponse<EbmInfoAll> getEbmInfoAll(@RequestParam(name = "ebmId") String ebmId){
        if(log.isDebugEnabled()){
            log.debug("[!~] get ebm info and relative info by ebmId. ebmId=[{}]",ebmId);
        }

        //先从缓存中获取ebm信息，如果没有获取到才执行查询。
        String jsonString = stringRedisTemplate.opsForValue().get(RedisKeyConstants.EBM_INFO_ALL_CACHE_KEY + ebmId);
        EbmInfoAll ebmInfoAll = JSON.toJavaObject(JSON.parseObject(jsonString),EbmInfoAll.class);
        if(ebmInfoAll != null && !EntityUtil.isReallyEmpty(ebmInfoAll)){
            //刷新缓存
            stringRedisTemplate.opsForValue().set(RedisKeyConstants.EBM_INFO_ALL_CACHE_KEY + ebmId ,JSON.toJSONString(ebmInfoAll),5,TimeUnit.SECONDS);
            return ApiEntityResponse.ok(ebmInfoAll);
        }

        ApiEntityResponse<EbmInfo> response = ebmService.getEbmInfoById(ebmId);
        EbmInfo ebmInfo = Optional.ofNullable(response).map(ApiEntityResponse::getData).orElseGet(EbmInfo::new);

        //方案相关信息
        SchemeInfo schemeInfo = coreFeginService.getSchemeInfoByEbmId(ebmId);
        SchemeInfoAll schemeInfoAll = Optional.ofNullable(schemeInfo).map(schemeInfo1 -> {
                                        SchemeInfoAll infoAll = new SchemeInfoAll();
                                        BeanUtils.copyProperties(schemeInfo1,infoAll);
                                        return infoAll;
                                    }).orElseGet(SchemeInfoAll::new);
        String planId = schemeInfo.getPlanId();
        //方案匹配的预案
        if(StringUtils.isNotBlank(planId)){
            //预案详情以及关联信息
            SysPlanMatchInfo sysPlanMatchInfo = systemFeginService.getByPlanId(planId);
            schemeInfoAll.setSysPlanMatchInfo(sysPlanMatchInfo);
        }
        //方案匹配的播出资源
        List<SchemeEbrInfo> schemeEbrInfoList = coreFeginService.findSchemeEbrListBySchemeId(schemeInfo.getSchemeId());
        schemeInfoAll.setSchemeEbrInfoList(schemeEbrInfoList);


        List<EbmAuxiliaryInfo> ebmAuxiliaryInfoList = new ArrayList<>();
        List<EbmDispatchInfo> ebmDispatchInfoList = new ArrayList<>();
        OriginFileInfo originFileInfo = null;
        if(!EntityUtil.isReallyEmpty(ebmInfo)){
            //查询ebm辅助数据
            ebmAuxiliaryInfoList = coreFeginService.getEbmAuxiliaryInfoEbmId(ebmInfo.getEbmId());
            //查询ebm分发数据
            ebmDispatchInfoList = coreFeginService.getEbmDispatchByEbmId(ebmInfo.getEbmId());

            List<EbdInfo> ebdInfoList = coreFeginService.getEbdInfoByEbmId(ebmId);


            //获取EBD数据包文件列表
            originFileInfo = Optional.ofNullable(ebdInfoList).orElse(Collections.emptyList()).stream()
                    .filter(ebdInfo -> CommonDictEnum.FLAG_RECEIVE.getKey().equals(ebdInfo.getSendFlag()))   //只取接收的数据包
                    .findFirst()
                    .map(ebd -> resoFeginService.getOriginFileById(ebd.getFileId()))
                    .orElseGet(OriginFileInfo::new);

            //获取ebd辅助数据包文件列表
/*
            List<EbdFilesInfo> ebdFilesInfoList = new ArrayList<>();
            if(ebdInfo != null && !EntityUtil.isReallyEmpty(ebdInfo)){
                ebdFilesInfoList = coreFeginService.getEbdFilesInfoByEbdId(ebdInfo.getEbdId());
            }
            ebdFilesInfo = Optional.ofNullable(ebdFilesInfoList).orElse(Collections.emptyList())
                    .stream().findFirst().orElse(null);*/

        }

        //获取播出资源信息
        List<EbrInfo> ebrInfoList = Optional.ofNullable(ebmDispatchInfoList).orElse(Collections.emptyList()).stream()
                .filter(Objects::nonNull)
                .map(ebmDispatchInfo -> {
                    EbrInfo ebrInfo = new EbrInfo();
                    if (StringUtils.isNotBlank(ebmDispatchInfo.getBsEbrId())) {
                        ebrInfo.setEbrId(ebmDispatchInfo.getBsEbrId());
                    } else if (StringUtils.isNotBlank(ebmDispatchInfo.getPsEbrId())) {
                        ebrInfo.setEbrId(ebmDispatchInfo.getPsEbrId());
                    } else if (StringUtils.isNotBlank(ebmDispatchInfo.getStEbrId())) {
                        ebrInfo.setEbrId(ebmDispatchInfo.getStEbrId());
                    } else if (StringUtils.isNotBlank(ebmDispatchInfo.getAsEbrId())) {
                        ebrInfo.setEbrId(ebmDispatchInfo.getAsEbrId());
                    }
                    return ebrInfo;
                })
                .distinct()
                .map(info ->  resoFeginService.getEbrCommonInfoById(info.getEbrId()))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());


        //获取处理流程信息
        DispatchFlowInfo dispatchFlowInfo = null;
        if(StringUtils.isNotBlank(ebmInfo.getFlowId())){
            dispatchFlowInfo = coreFeginService.getDispatchFlowInfoById(ebmInfo.getFlowId());
        }

        //获取分发消息详情
        EbmDispatchInfoInfo ebmDispatchInfoInfo = coreFeginService.getEbmDispatchInfoInfoByEbmId(ebmId);


        ebmInfoAll = new EbmInfoAll();
        BeanUtils.copyProperties(ebmInfo,ebmInfoAll);
        ebmInfoAll.setEbmAuxiliaryInfoList(ebmAuxiliaryInfoList);
        ebmInfoAll.setEbmDispatchInfoList(ebmDispatchInfoList);
        ebmInfoAll.setEbrInfoList(ebrInfoList);
        ebmInfoAll.setOriginFileInfo(originFileInfo);
        ebmInfoAll.setSchemeInfoAll(schemeInfoAll);
        ebmInfoAll.setDispatchFlowInfo(dispatchFlowInfo);
        ebmInfoAll.setEbmDispatchInfoInfo(ebmDispatchInfoInfo);
        //缓存到redis中，生效时间 5分钟。
        stringRedisTemplate.opsForValue().set(RedisKeyConstants.EBM_INFO_ALL_CACHE_KEY + ebmId ,JSON.toJSONString(ebmInfoAll),5,TimeUnit.SECONDS);
        return ApiEntityResponse.ok(ebmInfoAll);
    }

    @PostMapping("/handBack")
    @ApiOperation(value = "手动触发状态反馈", notes = "手动触发状态反馈")
    @RequiresPermissions(Permissions.CORE_EBM_WARN_BACK)
    public ApiResponse handBack(@RequestBody @Valid EbmHandBack request, BindingResult result){
        //调用MQ产生一条状态反馈的消息，由联动服务去消费
        EbmStateResponseMessage message=new EbmStateResponseMessage();
        message.setEbmId(request.getEbmId());
        message.setHandType("手动");
        mqMessageProducer.sendData(MqQueueConstant.EBMStateResponseQueue,JSON.toJSONString(message));

        return ApiResponse.ok();
    }
    
    @PostMapping("/cancelEbmPlay/{ebmId}")
    @RequiresPermissions(Permissions.CORE_EBM_CANCELPLAY)
    @ApiOperation(value = "根据ebmId取消播发", notes = "根据ebmId取消播发")
    public ApiResponse cancelEbmPlay(@PathVariable(name = "ebmId") String ebmId){
        if(log.isDebugEnabled()){
            log.debug("[!~] cancelEbmPlay by ebmId. ebmId=[{}]",ebmId);
        }
        Boolean resut = coreFeginService.cancelEbmPlay(ebmId);
        if(resut) {
        	return ApiResponse.ok();
        }else {
        	return ApiResponseBuilder.buildError(FrontErrorEnum.REQUIRED_PARAM_EMPTY);
        }
    }
    
    @PostMapping("/audit")
    @RequiresPermissions(Permissions.CORE_EBM_AUDIT)
    @ApiOperation(value = "根据ebmId审核消息", notes = "根据ebmId审核消息")
    public ApiResponse auditEbm(@RequestBody EbmAuditRequest request){
        if(log.isDebugEnabled()){
            log.debug("[!~] auditEbm by request. request=[{}]",JSON.toJSONString(request));
        }
        request.setAuditTime(new Date());
        coreFeginService.auditEbm(request);
        return ApiResponse.ok();
    }
    
}
