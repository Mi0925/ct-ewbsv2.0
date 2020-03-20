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
import cn.comtom.domain.reso.ebr.info.EbrManufacturerInfo;
import cn.comtom.domain.reso.ebr.info.EbrPlatformInfo;
import cn.comtom.domain.reso.ebr.request.AdapterAddRequest;
import cn.comtom.domain.reso.ebr.request.AdapterUpdateRequest;
import cn.comtom.domain.reso.ebr.request.EbrManufactPageRequest;
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
/**
 * @author:liuhy
 * @date: 2019/5/14
 * @time: 下午 7:38
 */

@Slf4j
@RestController
@RequestMapping("/safeRest/ebr/EbrManufact")
@Api(tags = "厂商信息")
public class EbrManufactController extends AuthController {


    @Autowired
    private ISystemFeginService systemFeginService;

    @Autowired
    private IResoFeginService resoFeginService;

    @Autowired
    private IPlatformService platformService;

    @GetMapping("/{ebrId}")
    @ApiOperation(value = "根据Id查询厂商详情", notes = "根据Id查询厂商详情")
    public ApiEntityResponse<EbrManufacturerInfo> getById(@PathVariable(name = "ebrId") String ebrId){
        if(log.isDebugEnabled()){
            log.debug("[!~] get EbrManufacturerInfo by ebrId. ebrId=[{}]",ebrId);
        }
        EbrManufacturerInfo ebrBroadcastInfo = resoFeginService.getEbrManufacturerById(ebrId);
        return ApiEntityResponse.ok(ebrBroadcastInfo);
    }

    @GetMapping("/page")
    @ApiOperation(value = "分页查询厂商信息", notes = "分页查询厂商信息")
    public ApiPageResponse<EbrManufacturerInfo> getByPage(@Valid EbrManufactPageRequest request){
        if(log.isDebugEnabled()){
            log.debug("[!~] get EbrManufacturerInfo  by page. request=[{}]",request);
        }
        ApiPageResponse<EbrManufacturerInfo> ebrManufacturerInfoByPage = resoFeginService.getEbrManufacturerInfoByPage(request);
        return ebrManufacturerInfoByPage;
    }

    @PostMapping("/save")
    @ApiOperation(value = "保存厂商信息", notes = "保存厂商信息")
    public ApiEntityResponse<EbrManufacturerInfo> save(@RequestBody @Valid EbrManufacturerInfo request){
        if(log.isDebugEnabled()){
            log.debug("[!~] save EbrManufacturerInfo. request=[{}]",request);
        }
        EbrManufacturerInfo result = resoFeginService.saveEbrManufacturerInfo(request);
        return ApiEntityResponse.ok(result);
    }

    @PutMapping("/update")
    @ApiOperation(value = "更新厂商信息", notes = "更新厂商信息")
    public ApiResponse update(@RequestBody @Valid EbrManufacturerInfo request){
        if(log.isDebugEnabled()){
            log.debug("[!~] update EbrManufacturerInfo. request=[{}]",request);
        }
        resoFeginService.updateEbrManufacturerInfo(request);

        return ApiResponse.ok();
    }

    @DeleteMapping("/delete/{id}")
    @ApiOperation(value = "删除厂商", notes = "删除厂商")
    public ApiResponse delete(@PathVariable(name = "id")  String id){
        if(log.isDebugEnabled()){
            log.debug("[!~] delete EbrManufacturerInfo  by ebrId. ebrId=[{}]",id);
        }
        Boolean success = resoFeginService.deleteEbrManufacturerInfo(id);
        return ApiResponse.ok();
    }
}
