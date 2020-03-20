package cn.comtom.ebs.front.main.ebr.controller;

import cn.comtom.domain.reso.ebr.info.EbrIdCreatorInfo;
import cn.comtom.domain.reso.ebr.info.EbrPlatformInfo;
import cn.comtom.domain.reso.ebr.request.EbrPlatformPageRequest;
import cn.comtom.domain.reso.ebr.request.PlatformAddRequest;
import cn.comtom.domain.reso.ebr.request.PlatformUpdateRequest;
import cn.comtom.domain.system.accessnode.info.AccessNodeInfo;
import cn.comtom.domain.system.accessnode.request.AccessNodeRequest;
import cn.comtom.domain.system.sysparam.info.SysParamsInfo;
import cn.comtom.ebs.front.common.AuthRest;
import cn.comtom.ebs.front.fegin.service.IResoFeginService;
import cn.comtom.ebs.front.fegin.service.ISystemFeginService;
import cn.comtom.ebs.front.fw.AuthController;
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
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

/**
 * @author:WJ
 * @date: 2018/12/25 0025
 * @time: 下午 7:38
 */

@Slf4j
@RestController
@RequestMapping("/safeRest")
@Api(tags = "平台信息", description = "平台信息")
@AuthRest
public class PlatformController extends AuthController {

    @Autowired
    private  IPlatformService platformService;

    @Autowired
    private ISystemFeginService systemFeginService;

    @Autowired
    private IResoFeginService resoFeginService;

    @GetMapping("/ebrPlatform/list")
    @ApiOperation(value = "查询所有的平台信息", notes = "查询所有的平台信息")
    public ApiEntityResponse<List<EbrPlatformInfo>> list(){
        return ApiEntityResponse.ok(platformService.list());
    }

    @GetMapping("/ebrPlatform/page")
    @ApiOperation(value = "分页查询平台信息", notes = "分页查询平台信息")
    public ApiPageResponse<EbrPlatformInfo> page(@Valid EbrPlatformPageRequest pageRequest, BindingResult bResult){
        if(log.isDebugEnabled()){
            log.debug("[!~] get ebrPlatform info list by page. pageRequest=[{}]",pageRequest);
        }
        if(StringUtils.isNotBlank(pageRequest.getQueryType())){
            SysParamsInfo sysParamsInfo = systemFeginService.getSysParamsInfoByKey(Constants.EBR_PLATFORM_ID);
            EbrPlatformInfo ebrPlatformInfo = platformService.findById(sysParamsInfo.getParamValue());
            if("1".equals(pageRequest.getQueryType())){
                //查询上级平台
                pageRequest.setChildAreaCode(ebrPlatformInfo.getAreaCode());
            }else if("2".equals(pageRequest.getQueryType())){
                //查询下级平台
                pageRequest.setParentAreaCode(ebrPlatformInfo.getAreaCode());
            }
        }
        return platformService.getEbrPlatformInfoByPage(pageRequest);
    }

    @GetMapping("/ebrPlatform/{ebrId}")
    @ApiOperation(value = "根据Id查询平台详情", notes = "根据Id查询平台详情")
    public ApiEntityResponse<EbrPlatformInfo> page(@PathVariable(name = "ebrId") String ebrId){
        if(log.isDebugEnabled()){
            log.debug("[!~] get ebrPlatform by ebrId. ebrId=[{}]",ebrId);
        }
        EbrPlatformInfo ebrPlatformInfo = platformService.findById(ebrId);
        if(ebrPlatformInfo != null){
            AccessNodeInfo accessNodeInfo = systemFeginService.getAccessNodeByPlatformId(ebrPlatformInfo.getPsEbrId());
            if(accessNodeInfo != null){
                ebrPlatformInfo.setSrcHost(accessNodeInfo.getIp());
            }
        }
        return ApiEntityResponse.ok(ebrPlatformInfo);
    }

    @GetMapping("/ebrPlatform/this")
    @ApiOperation(value = "查询本级平台信息", notes = "查询本级平台信息")
    public ApiEntityResponse<EbrPlatformInfo> getThisPlatformInfo(){
        if(log.isDebugEnabled()){
            log.debug("[!~] get this ebrPlatform info. ebrId=[{}]");
        }
        EbrPlatformInfo ebrPlatformInfo = null;
        SysParamsInfo sysParamsInfo = systemFeginService.getSysParamsInfoByKey(Constants.EBR_PLATFORM_ID);
        String ebrId = Optional.ofNullable(sysParamsInfo).map(SysParamsInfo::getParamValue).orElseGet(String::new);
        if(StringUtils.isNotBlank(ebrId)){
            ebrPlatformInfo = resoFeginService.getEbrPlatformById(ebrId);
        }
        return ApiEntityResponse.ok(ebrPlatformInfo);
    }

    @PostMapping("/ebrPlatform/save")
    @ApiOperation(value = "保存平台信息", notes = "保存平台信息")
    public ApiEntityResponse<EbrPlatformInfo> save(@RequestBody @Valid PlatformAddRequest request){
        if(log.isDebugEnabled()){
            log.debug("[!~] save EbrPlatformInfo. request=[{}]",request);
        }
    /*    if(StringUtils.isBlank(request.getAreaCode())){
            request.setAreaCode(EbUtil.getAreaCodeFromEbrId(request.getPsEbrId()));
        }*/
    if(StringUtils.isBlank(request.getPsEbrId())){
        if(StringUtils.isBlank(request.getAreaCode())){
            SysParamsInfo sysParamsInfo = systemFeginService.getByKey(Constants.EBR_PLATFORM_ID);
            if(sysParamsInfo != null){
                EbrPlatformInfo ebrPlatformInfo = resoFeginService.getEbrPlatformById(sysParamsInfo.getParamValue());
                if(ebrPlatformInfo != null){
                    request.setAreaCode(ebrPlatformInfo.getAreaCode());
                }
            }
        }
        EbrIdCreatorInfo ebrIdCreatorInfo = new EbrIdCreatorInfo();
        ebrIdCreatorInfo.setSourceLevel(request.getPlatLevel());
        ebrIdCreatorInfo.setAreaCode(request.getAreaCode());
        ebrIdCreatorInfo.setSourceTypeCode(request.getResType());
        ebrIdCreatorInfo.setSourceSubTypeCode(Constants.EBR_SUB_SOURCE_TYPE_PLATEFORM);
        String ebrId = resoFeginService.getEbrId(ebrIdCreatorInfo);
        request.setPsEbrId(ebrId);
    }else{
        request.setAreaCode(EbUtil.getAreaCodeFromEbrId(request.getPsEbrId()));
    }
    EbrPlatformInfo ebrPlatformInfo = platformService.save(request);
    if(ebrPlatformInfo != null && StringUtils.isNotBlank(request.getSrcHost())){
        AccessNodeRequest accessNodeRequest = new AccessNodeRequest();
        accessNodeRequest.setPlatformId(ebrPlatformInfo.getPsEbrId());
        accessNodeRequest.setIp(request.getSrcHost());
        accessNodeRequest.setStatus(StateDictEnum.ACCESS_NODE_STATUS_NORMAL.getKey());
        systemFeginService.saveAccessNode(accessNodeRequest);
    }
    return ApiEntityResponse.ok(ebrPlatformInfo);
    }

    @PutMapping("/ebrPlatform/update")
    @ApiOperation(value = "更新平台信息", notes = "更新平台信息")
    public ApiResponse update(@RequestBody @Valid PlatformUpdateRequest request){
        if(log.isDebugEnabled()){
            log.debug("[!~] update EbrPlatformInfo. request=[{}]",request);
        }
        request.setSyncFlag(Integer.valueOf(StateDictEnum.SYNC_STATUS_NOT.getKey()));   //修改后设置平台资源信息同步状态为 未同步
        platformService.update(request);

        if(StringUtils.isBlank(request.getSrcHost())){
            systemFeginService.deleteAccessNodeByEbrId(request.getPsEbrId());
        }else{
            AccessNodeInfo accessNodeInfo = systemFeginService.getAccessNodeByPlatformId(request.getPsEbrId());
            if(accessNodeInfo != null){
                AccessNodeRequest accessNodeRequest = new AccessNodeRequest();
                accessNodeRequest.setIp(request.getSrcHost());
                accessNodeRequest.setId(accessNodeInfo.getId());
                systemFeginService.updateAccessNode(accessNodeRequest);
            }else {
            	AccessNodeRequest accessNodeRequest = new AccessNodeRequest();
                accessNodeRequest.setPlatformId(request.getPsEbrId());
                accessNodeRequest.setIp(request.getSrcHost());
                accessNodeRequest.setStatus(StateDictEnum.ACCESS_NODE_STATUS_NORMAL.getKey());
            	systemFeginService.saveAccessNode(accessNodeRequest);
            }
        }
        return ApiResponse.ok();
    }

    @DeleteMapping("/ebrPlatform/delete/{ebrId}")
    @ApiOperation(value = "更新平台信息", notes = "更新平台信息")
    public ApiResponse delete(@PathVariable(name = "ebrId")  String ebrId){
        if(log.isDebugEnabled()){
            log.debug("[!~] delete EbrPlatformInfo by ebrId. ebrId=[{}]",ebrId);
        }
        Boolean success = platformService.delete(ebrId);
        if(success){
            systemFeginService.deleteAccessNodeByEbrId(ebrId);
        }
        return ApiResponse.ok();
    }
}
