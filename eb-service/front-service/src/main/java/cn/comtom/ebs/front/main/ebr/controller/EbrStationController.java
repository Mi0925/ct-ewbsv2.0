package cn.comtom.ebs.front.main.ebr.controller;

import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.comtom.domain.reso.ebr.info.EbrIdCreatorInfo;
import cn.comtom.domain.reso.ebr.info.EbrPlatformInfo;
import cn.comtom.domain.reso.ebr.info.EbrStationInfo;
import cn.comtom.domain.reso.ebr.request.EbrStationPageRequest;
import cn.comtom.domain.reso.ebr.request.StationAddRequest;
import cn.comtom.domain.reso.ebr.request.StationUpdateRequest;
import cn.comtom.domain.system.accessnode.info.AccessNodeInfo;
import cn.comtom.domain.system.accessnode.request.AccessNodeRequest;
import cn.comtom.domain.system.sysparam.info.SysParamsInfo;
import cn.comtom.ebs.front.common.AuthRest;
import cn.comtom.ebs.front.fegin.service.IResoFeginService;
import cn.comtom.ebs.front.fegin.service.ISystemFeginService;
import cn.comtom.ebs.front.fw.AuthController;
import cn.comtom.ebs.front.main.ebr.service.IEbrStationService;
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

/**
 * @author:liuhy
 * @date: 2019/5/14
 * @time: 下午 7:38
 */

@Slf4j
@RestController
@RequestMapping("/safeRest/ebr/ebrStation")
@Api(tags = "台站信息", description = "台站信息")
@AuthRest
public class EbrStationController extends AuthController {

    @Autowired
    private  IEbrStationService stationService;

    @Autowired
    private ISystemFeginService systemFeginService;

    @Autowired
    private IResoFeginService resoFeginService;

    @Autowired
    private IPlatformService platformService;
    
    @GetMapping("/page")
    @ApiOperation(value = "分页查询台站信息", notes = "分页查询台站信息")
    public ApiPageResponse<EbrStationInfo> page(@Valid EbrStationPageRequest pageRequest, BindingResult bResult){
        if(log.isDebugEnabled()){
            log.debug("[!~] get ebrStation info list by page. pageRequest=[{}]",pageRequest);
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
        return stationService.getEbrStationInfoByPage(pageRequest);
    }

    @GetMapping("/{ebrId}")
    @ApiOperation(value = "根据Id查询台站详情", notes = "根据Id查询台站详情")
    public ApiEntityResponse<EbrStationInfo> getById(@PathVariable(name = "ebrId") String ebrId){
        if(log.isDebugEnabled()){
            log.debug("[!~] get ebrStation by ebrId. ebrId=[{}]",ebrId);
        }
        EbrStationInfo ebrStationInfo = stationService.getById(ebrId);
        if(ebrStationInfo != null){
            AccessNodeInfo accessNodeInfo = systemFeginService.getAccessNodeByPlatformId(ebrStationInfo.getEbrStId());
            if(accessNodeInfo != null){
            	ebrStationInfo.setSrcHost(accessNodeInfo.getIp());
            }
        }
        return ApiEntityResponse.ok(ebrStationInfo);
    }

    @PostMapping("/save")
    @ApiOperation(value = "保存台站信息", notes = "保存台站信息")
    public ApiEntityResponse<EbrStationInfo> save(@RequestBody @Valid StationAddRequest request){
        if(log.isDebugEnabled()){
            log.debug("[!~] save ebrStation. request=[{}]",request);
        }
       /* if(StringUtils.isBlank(request.getAreaCode())){
            request.setAreaCode(EbUtil.getAreaCodeFromEbrId(request.getBsEbrId()));
        }*/
        SysParamsInfo sysParamsInfo = systemFeginService.getByKey(Constants.EBR_PLATFORM_ID);
        EbrPlatformInfo ebrPlatformInfo = resoFeginService.getEbrPlatformById(sysParamsInfo.getParamValue());

        if(StringUtils.isBlank(request.getEbrStId())){
            if(StringUtils.isBlank(request.getAreaCode()) && ebrPlatformInfo != null){
                request.setAreaCode(ebrPlatformInfo.getAreaCode());
            }

            //生成EbrId
            EbrIdCreatorInfo ebrIdCreatorInfo = new EbrIdCreatorInfo();
            ebrIdCreatorInfo.setSourceLevel(ebrPlatformInfo.getPlatLevel());    //新增的播出系统跟本级平台级别一致
            ebrIdCreatorInfo.setAreaCode(request.getAreaCode());              //新增的播出系统的区域编码跟本级平台级别一致
            ebrIdCreatorInfo.setSourceTypeCode(request.getResType());
            ebrIdCreatorInfo.setSourceSubTypeCode(Constants.EBR_SUB_SOURCE_TYPE_PLATEFORM);
            String ebrId = resoFeginService.getEbrId(ebrIdCreatorInfo);
            request.setEbrStId(ebrId);
        }else{
            request.setAreaCode(EbUtil.getAreaCodeFromEbrId(request.getEbrStId()));
        }
        /*if(request.getSquare()==null){
            request.setSquare(0.0);
        }
        if(request.getPopulation()==null){
            request.setPopulation(0.0);
        }*/
        EbrStationInfo ebrStationInfo = stationService.save(request);
        if(StringUtils.isNotBlank(request.getSrcHost())){
            AccessNodeRequest accessNodeRequest = new AccessNodeRequest();
            accessNodeRequest.setPlatformId(ebrStationInfo.getEbrStId());
            accessNodeRequest.setIp(request.getSrcHost());
            accessNodeRequest.setStatus(StateDictEnum.ACCESS_NODE_STATUS_NORMAL.getKey());
            systemFeginService.saveAccessNode(accessNodeRequest);
        }
        return ApiEntityResponse.ok(ebrStationInfo);
    }

    @PutMapping("/update")
    @ApiOperation(value = "更新台站信息", notes = "更新台站信息")
    public ApiResponse update(@RequestBody @Valid StationUpdateRequest request){
        if(log.isDebugEnabled()){
            log.debug("[!~] update EbrBroadcast. request=[{}]",request);
        }
        request.setSyncFlag(StateDictEnum.SYNC_STATUS_NOT.getKey());   //修改后设置平台资源信息同步状态为 未同步
        stationService.update(request);

        if(StringUtils.isBlank(request.getSrcHost())){
            systemFeginService.deleteAccessNodeByEbrId(request.getEbrStId());
        }else{
            AccessNodeInfo accessNodeInfo = systemFeginService.getAccessNodeByPlatformId(request.getEbrStId());
            if(accessNodeInfo != null){
                AccessNodeRequest accessNodeRequest = new AccessNodeRequest();
                accessNodeRequest.setIp(request.getSrcHost());
                accessNodeRequest.setId(accessNodeInfo.getId());
                systemFeginService.updateAccessNode(accessNodeRequest);
            }else {
            	AccessNodeRequest accessNodeRequest = new AccessNodeRequest();
                accessNodeRequest.setPlatformId(request.getEbrStId());
                accessNodeRequest.setIp(request.getSrcHost());
                accessNodeRequest.setStatus(StateDictEnum.ACCESS_NODE_STATUS_NORMAL.getKey());
            	systemFeginService.saveAccessNode(accessNodeRequest);
            }
        }
        return ApiResponse.ok();
    }

    @DeleteMapping("/delete/{ebrId}")
    @ApiOperation(value = "删除台站信息", notes = "删除台站信息")
    public ApiResponse delete(@PathVariable(name = "ebrId")  String ebrId){
        if(log.isDebugEnabled()){
            log.debug("[!~] delete ebrStation by ebrId. ebrId=[{}]",ebrId);
        }
        Boolean success = stationService.delete(ebrId);
        if(success){
            systemFeginService.deleteAccessNodeByEbrId(ebrId);
        }
        return ApiResponse.ok();
    }
}
