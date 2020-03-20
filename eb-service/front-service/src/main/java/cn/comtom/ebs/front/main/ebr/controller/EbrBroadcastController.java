package cn.comtom.ebs.front.main.ebr.controller;

import cn.comtom.domain.reso.ebr.info.EbrBroadcastInfo;
import cn.comtom.domain.reso.ebr.info.EbrIdCreatorInfo;
import cn.comtom.domain.reso.ebr.info.EbrPlatformInfo;
import cn.comtom.domain.reso.ebr.request.BroadcastPageRequest;
import cn.comtom.domain.reso.ebr.request.EbrBroadcastAddRequest;
import cn.comtom.domain.reso.ebr.request.EbrBroadcastUpdateRequest;
import cn.comtom.domain.system.accessnode.info.AccessNodeInfo;
import cn.comtom.domain.system.accessnode.request.AccessNodeRequest;
import cn.comtom.domain.system.sysparam.info.SysParamsInfo;
import cn.comtom.ebs.front.common.AuthRest;
import cn.comtom.ebs.front.fegin.service.IResoFeginService;
import cn.comtom.ebs.front.fegin.service.ISystemFeginService;
import cn.comtom.ebs.front.fw.AuthController;
import cn.comtom.ebs.front.main.ebr.service.IEbrBroadcastService;
import cn.comtom.ebs.front.main.ebr.service.IPlatformService;
import cn.comtom.ebs.front.utils.EbUtil;
import cn.comtom.tools.constants.Constants;
import cn.comtom.tools.enums.StateDictEnum;
import cn.comtom.tools.response.ApiEntityResponse;
import cn.comtom.tools.response.ApiPageResponse;
import cn.comtom.tools.response.ApiResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;

@Slf4j
@RestController
@RequestMapping("/safeRest/ebr/Broadcast")
@Api(tags = "播出系统信息")
@AuthRest
public class EbrBroadcastController extends AuthController {

    @Autowired
    private IEbrBroadcastService ebrBroadcastService;

    @Autowired
    private ISystemFeginService systemFeginService;

    @Autowired
    private IResoFeginService resoFeginService;

    @Autowired
    private IPlatformService platformService;

    @GetMapping("/{ebrId}")
    @ApiOperation(value = "根据Id查询播出系统详情", notes = "根据Id查询播出系统详情")
    public ApiEntityResponse<EbrBroadcastInfo> getById(@PathVariable(name = "ebrId") String ebrId){
        if(log.isDebugEnabled()){
            log.debug("[!~] get ebrBroadcast by ebrId. ebrId=[{}]",ebrId);
        }
        EbrBroadcastInfo ebrBroadcastInfo = ebrBroadcastService.getById(ebrId);
        AccessNodeInfo accessNodeInfo = systemFeginService.getAccessNodeByPlatformId(ebrBroadcastInfo.getBsEbrId());
        if(accessNodeInfo != null){
            ebrBroadcastInfo.setSrcHost(accessNodeInfo.getIp());
        }
        return ApiEntityResponse.ok(ebrBroadcastInfo);
    }

    @GetMapping("/page")
    @ApiOperation(value = "分页查询播出系统", notes = "分页查询播出系统")
    public ApiPageResponse<EbrBroadcastInfo> getByPage(@Valid BroadcastPageRequest request){
        if(log.isDebugEnabled()){
            log.debug("[!~] get ebrBroadcast  by page. request=[{}]",request);
        }
        request.setQueryType("");
        if(StringUtils.isNotBlank(request.getQueryType())){
            SysParamsInfo sysParamsInfo = systemFeginService.getSysParamsInfoByKey(Constants.EBR_PLATFORM_ID);
            EbrPlatformInfo ebrPlatformInfo = platformService.findById(sysParamsInfo.getParamValue());
            if("1".equals(request.getQueryType())){
                //查询上级播放系统
                request.setChildAreaCode(ebrPlatformInfo.getAreaCode());
            }else if("2".equals(request.getQueryType())){
                //查询本级和下级播放系统
                request.setParentAreaCode(ebrPlatformInfo.getAreaCode());
            }
        }
        return ebrBroadcastService.getEbrBroadcastInfoByPage(request);
    }

    @PostMapping("/save")
    @ApiOperation(value = "保存播出系统信息", notes = "保存播出系统信息")
    public ApiEntityResponse<EbrBroadcastInfo> save(@RequestBody @Valid EbrBroadcastAddRequest request){
        if(log.isDebugEnabled()){
            log.debug("[!~] save ebrBroadcastInfo. request=[{}]",request);
        }
       /* if(StringUtils.isBlank(request.getAreaCode())){
            request.setAreaCode(EbUtil.getAreaCodeFromEbrId(request.getBsEbrId()));
        }*/
        SysParamsInfo sysParamsInfo = systemFeginService.getByKey(Constants.EBR_PLATFORM_ID);
        EbrPlatformInfo ebrPlatformInfo = resoFeginService.getEbrPlatformById(sysParamsInfo.getParamValue());

        if(StringUtils.isBlank(request.getBsEbrId())){
            if(StringUtils.isBlank(request.getAreaCode()) && ebrPlatformInfo != null){
                request.setAreaCode(ebrPlatformInfo.getAreaCode());
            }

            //生成EbrId
            EbrIdCreatorInfo ebrIdCreatorInfo = new EbrIdCreatorInfo();
            ebrIdCreatorInfo.setSourceLevel(ebrPlatformInfo.getPlatLevel());    //新增的播出系统跟本级平台级别一致
            ebrIdCreatorInfo.setAreaCode(request.getAreaCode());              //新增的播出系统的区域编码跟本级平台级别一致
            ebrIdCreatorInfo.setSourceTypeCode(request.getResType());
            ebrIdCreatorInfo.setSourceSubTypeCode(Constants.EBR_SUB_SOURCE_TYPE_BROADCAST);
            String ebrId = resoFeginService.getEbrId(ebrIdCreatorInfo);
            request.setBsEbrId(ebrId);
        }else{
            request.setAreaCode(EbUtil.getAreaCodeFromEbrId(request.getBsEbrId()));
        }
        if(request.getSquare()==null){
            request.setSquare(0.0);
        }
        if(request.getPopulation()==null){
            request.setPopulation(0.0);
        }
        request.setUpdateTime(new Date());
        EbrBroadcastInfo ebrBroadcastInfo = ebrBroadcastService.save(request);
        if(StringUtils.isNotBlank(request.getSrcHost())){
            AccessNodeRequest accessNodeRequest = new AccessNodeRequest();
            accessNodeRequest.setPlatformId(ebrBroadcastInfo.getBsEbrId());
            accessNodeRequest.setIp(request.getSrcHost());
            accessNodeRequest.setStatus(StateDictEnum.ACCESS_NODE_STATUS_NORMAL.getKey());
            systemFeginService.saveAccessNode(accessNodeRequest);
        }
        return ApiEntityResponse.ok(ebrBroadcastInfo);
    }

    @PutMapping("/update")
    @ApiOperation(value = "更新播出系统信息", notes = "更新播出系统信息")
    public ApiResponse update(@RequestBody @Valid EbrBroadcastUpdateRequest request){
        if(log.isDebugEnabled()){
            log.debug("[!~] update EbrBroadcast. request=[{}]",request);
        }
        request.setUpdateTime(new Date());
        request.setSyncFlag(StateDictEnum.SYNC_STATUS_NOT.getKey());   //修改后设置平台资源信息同步状态为 未同步
        ebrBroadcastService.update(request);

        if(StringUtils.isBlank(request.getSrcHost())){
            systemFeginService.deleteAccessNodeByEbrId(request.getBsEbrId());
        }else{
            AccessNodeInfo accessNodeInfo = systemFeginService.getAccessNodeByPlatformId(request.getBsEbrId());
            if(accessNodeInfo != null){
                AccessNodeRequest accessNodeRequest = new AccessNodeRequest();
                accessNodeRequest.setIp(request.getSrcHost());
                accessNodeRequest.setId(accessNodeInfo.getId());
                systemFeginService.updateAccessNode(accessNodeRequest);
            }else {
            	AccessNodeRequest accessNodeRequest = new AccessNodeRequest();
                accessNodeRequest.setPlatformId(request.getBsEbrId());
                accessNodeRequest.setIp(request.getSrcHost());
                accessNodeRequest.setStatus(StateDictEnum.ACCESS_NODE_STATUS_NORMAL.getKey());
            	systemFeginService.saveAccessNode(accessNodeRequest);
            }
        }
        return ApiResponse.ok();
    }

    @DeleteMapping("/delete/{ebrId}")
    @ApiOperation(value = "删除播出系统", notes = "删除播出系统")
    public ApiResponse delete(@PathVariable(name = "ebrId")  String ebrId){
        if(log.isDebugEnabled()){
            log.debug("[!~] delete Broadcast  by ebrId. ebrId=[{}]",ebrId);
        }
        Boolean success = ebrBroadcastService.delete(ebrId);
        if(success){
            systemFeginService.deleteAccessNodeByEbrId(ebrId);
        }
        return ApiResponse.ok();
    }
}
