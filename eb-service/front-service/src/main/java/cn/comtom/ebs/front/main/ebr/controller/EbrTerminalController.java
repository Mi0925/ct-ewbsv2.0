package cn.comtom.ebs.front.main.ebr.controller;

import cn.comtom.domain.reso.ebr.info.EbrPlatformInfo;
import cn.comtom.domain.reso.ebr.info.EbrTerminalInfo;
import cn.comtom.domain.reso.ebr.request.EbrTerminalPageRequest;
import cn.comtom.domain.system.sysparam.info.SysParamsInfo;
import cn.comtom.ebs.front.common.AuthRest;
import cn.comtom.ebs.front.fegin.service.ISystemFeginService;
import cn.comtom.ebs.front.fw.AuthController;
import cn.comtom.ebs.front.main.ebr.service.IEbrTerminalService;
import cn.comtom.ebs.front.main.ebr.service.IPlatformService;
import cn.comtom.tools.constants.Constants;
import cn.comtom.tools.response.ApiEntityResponse;
import cn.comtom.tools.response.ApiPageResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/safeRest/ebr/terminal")
@Api(tags = "终端资源信息")
@AuthRest
public class EbrTerminalController extends AuthController {

    @Autowired
    private IEbrTerminalService ebrTerminalService;

    @Autowired
    private ISystemFeginService systemFeginService;

    @Autowired
    private IPlatformService platformService;

    @GetMapping("/{ebrId}")
    @ApiOperation(value = "根据Id查询终端资源信息", notes = "根据Id查询终端资源信息")
    public ApiEntityResponse<EbrTerminalInfo> getById(@PathVariable(name = "ebrId") String ebrId) {
        if (log.isDebugEnabled()) {
            log.debug("[!~] get ebrTerminalInfo by ebrId. ebrId=[{}]", ebrId);
        }
        EbrTerminalInfo ebrTerminalInfo = ebrTerminalService.getById(ebrId);
        return ApiEntityResponse.ok(ebrTerminalInfo);
    }

    @GetMapping("/page")
    @ApiOperation(value = "分页查询终端资源信息", notes = "分页查询终端资源信息")
    public ApiPageResponse<EbrTerminalInfo> getByPage(@Valid EbrTerminalPageRequest request) {
        if (log.isDebugEnabled()) {
            log.debug("[!~] get ebrTerminalInfo by page. request=[{}]", request);
        }
        if (StringUtils.isNotBlank(request.getQueryType())) {
            SysParamsInfo sysParamsInfo = systemFeginService.getSysParamsInfoByKey(Constants.EBR_PLATFORM_ID);
            EbrPlatformInfo ebrPlatformInfo = platformService.findById(sysParamsInfo.getParamValue());
            if ("1".equals(request.getQueryType())) {
                //查询上级终端
                request.setChildAreaCode(ebrPlatformInfo.getAreaCode());
            } else if ("2".equals(request.getQueryType())) {
                //查询本级和下级终端
                request.setParentAreaCode(ebrPlatformInfo.getAreaCode());
            }
        }
        return ebrTerminalService.getByPage(request);
    }
}
