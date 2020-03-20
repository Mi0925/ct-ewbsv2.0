package cn.comtom.ebs.front.main.ebr.controller;

import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.comtom.domain.reso.ebr.info.EbrAdapterInfo;
import cn.comtom.domain.reso.ebr.info.EbrIdCreatorInfo;
import cn.comtom.domain.reso.ebr.info.EbrPlatformInfo;
import cn.comtom.domain.reso.ebr.request.AdapterAddRequest;
import cn.comtom.domain.reso.ebr.request.AdapterUpdateRequest;
import cn.comtom.domain.reso.ebr.request.EbrAdapterPageRequest;
import cn.comtom.domain.system.accessnode.info.AccessNodeInfo;
import cn.comtom.domain.system.accessnode.request.AccessNodeRequest;
import cn.comtom.domain.system.sysparam.info.SysParamsInfo;
import cn.comtom.ebs.front.common.AuthRest;
import cn.comtom.ebs.front.fegin.service.IResoFeginService;
import cn.comtom.ebs.front.fegin.service.ISystemFeginService;
import cn.comtom.ebs.front.fw.AuthController;
import cn.comtom.ebs.front.main.ebr.service.IEbrAdapterService;
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
@RequestMapping("/safeRest/ebr/ebrAdapter")
@Api(tags = "适配器信息")
@AuthRest
public class EbrAdapterController extends AuthController {

    @Autowired
    private IEbrAdapterService ebrAdapterService;

    @Autowired
    private ISystemFeginService systemFeginService;

    @Autowired
    private IResoFeginService resoFeginService;

    @Autowired
    private IPlatformService platformService;

    @GetMapping("/{ebrId}")
    @ApiOperation(value = "根据Id查询适配器详情", notes = "根据Id查询适配器详情")
    public ApiEntityResponse<EbrAdapterInfo> getById(@PathVariable(name = "ebrId") String ebrId){
        if(log.isDebugEnabled()){
            log.debug("[!~] get ebrAdapter by ebrId. ebrId=[{}]",ebrId);
        }
        EbrAdapterInfo ebrBroadcastInfo = ebrAdapterService.getById(ebrId);
        AccessNodeInfo accessNodeInfo = systemFeginService.getAccessNodeByPlatformId(ebrBroadcastInfo.getEbrAsId());
        if(accessNodeInfo != null){
            ebrBroadcastInfo.setSrcHost(accessNodeInfo.getIp());
        }
        return ApiEntityResponse.ok(ebrBroadcastInfo);
    }

    @GetMapping("/page")
    @ApiOperation(value = "分页查询适配器", notes = "分页查询适配器")
    public ApiPageResponse<EbrAdapterInfo> getByPage(@Valid EbrAdapterPageRequest request){
        if(log.isDebugEnabled()){
            log.debug("[!~] get ebrAdapter  by page. request=[{}]",request);
        }
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
        return ebrAdapterService.getEbrAdapterInfoByPage(request);
    }

    @PostMapping("/save")
    @ApiOperation(value = "保存适配器信息", notes = "保存适配器信息")
    public ApiEntityResponse<EbrAdapterInfo> save(@RequestBody @Valid AdapterAddRequest request){
        if(log.isDebugEnabled()){
            log.debug("[!~] save ebrAdapter. request=[{}]",request);
        }
       /* if(StringUtils.isBlank(request.getAreaCode())){
            request.setAreaCode(EbUtil.getAreaCodeFromEbrId(request.getBsEbrId()));
        }*/
        SysParamsInfo sysParamsInfo = systemFeginService.getByKey(Constants.EBR_PLATFORM_ID);
        EbrPlatformInfo ebrPlatformInfo = resoFeginService.getEbrPlatformById(sysParamsInfo.getParamValue());

        if(StringUtils.isBlank(request.getEbrAsId())){
            if(StringUtils.isBlank(request.getAreaCode()) && ebrPlatformInfo != null){
                request.setAreaCode(ebrPlatformInfo.getAreaCode());
            }

            //生成EbrId
            EbrIdCreatorInfo ebrIdCreatorInfo = new EbrIdCreatorInfo();
            ebrIdCreatorInfo.setSourceLevel(ebrPlatformInfo.getPlatLevel());    //新增的播出系统跟本级平台级别一致
            ebrIdCreatorInfo.setAreaCode(request.getAreaCode());              //新增的播出系统的区域编码跟本级平台级别一致
            ebrIdCreatorInfo.setSourceTypeCode(request.getResType());
            ebrIdCreatorInfo.setSourceSubTypeCode(Constants.EBR_SUB_SOURCE_TYPE_ADAPTOR);
            String ebrId = resoFeginService.getEbrId(ebrIdCreatorInfo);
            request.setEbrAsId(ebrId);
        }else{
            request.setAreaCode(EbUtil.getAreaCodeFromEbrId(request.getEbrAsId()));
        }
        /*if(request.getSquare()==null){
            request.setSquare(0.0);
        }
        if(request.getPopulation()==null){
            request.setPopulation(0.0);
        }*/
        EbrAdapterInfo ebrAdapterInfo = ebrAdapterService.save(request);
        if(StringUtils.isNotBlank(request.getSrcHost())){
            AccessNodeRequest accessNodeRequest = new AccessNodeRequest();
            accessNodeRequest.setPlatformId(ebrAdapterInfo.getEbrAsId());
            accessNodeRequest.setIp(request.getSrcHost());
            accessNodeRequest.setStatus(StateDictEnum.ACCESS_NODE_STATUS_NORMAL.getKey());
            systemFeginService.saveAccessNode(accessNodeRequest);
        }
        return ApiEntityResponse.ok(ebrAdapterInfo);
    }

    @PutMapping("/update")
    @ApiOperation(value = "更新适配器信息", notes = "更新适配器信息")
    public ApiResponse update(@RequestBody @Valid AdapterUpdateRequest request){
        if(log.isDebugEnabled()){
            log.debug("[!~] update ebrAdapter. request=[{}]",request);
        }
        request.setSyncFlag(StateDictEnum.SYNC_STATUS_NOT.getKey());   //修改后设置平台资源信息同步状态为 未同步
        ebrAdapterService.update(request);

        if(StringUtils.isBlank(request.getSrcHost())){
            systemFeginService.deleteAccessNodeByEbrId(request.getEbrAsId());
        }else{
            AccessNodeInfo accessNodeInfo = systemFeginService.getAccessNodeByPlatformId(request.getEbrAsId());
            if(accessNodeInfo != null){
                AccessNodeRequest accessNodeRequest = new AccessNodeRequest();
                accessNodeRequest.setIp(request.getSrcHost());
                accessNodeRequest.setId(accessNodeInfo.getId());
                systemFeginService.updateAccessNode(accessNodeRequest);
            }else {
            	AccessNodeRequest accessNodeRequest = new AccessNodeRequest();
                accessNodeRequest.setPlatformId(request.getEbrAsId());
                accessNodeRequest.setIp(request.getSrcHost());
                accessNodeRequest.setStatus(StateDictEnum.ACCESS_NODE_STATUS_NORMAL.getKey());
            	systemFeginService.saveAccessNode(accessNodeRequest);
            }
        }
        return ApiResponse.ok();
    }

    @DeleteMapping("/delete/{ebrId}")
    @ApiOperation(value = "删除适配器", notes = "删除适配器")
    public ApiResponse delete(@PathVariable(name = "ebrId")  String ebrId){
        if(log.isDebugEnabled()){
            log.debug("[!~] delete ebrAdapter  by ebrId. ebrId=[{}]",ebrId);
        }
        Boolean success = ebrAdapterService.delete(ebrId);
        if(success){
            systemFeginService.deleteAccessNodeByEbrId(ebrId);
        }
        return ApiResponse.ok();
    }
}
