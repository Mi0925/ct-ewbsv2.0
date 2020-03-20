package cn.comtom.ebs.front.main.core.contoller;

import cn.comtom.domain.core.ebd.info.EbdInfo;
import cn.comtom.domain.core.ebd.request.EbdPageRequest;
import cn.comtom.ebs.front.common.AuthRest;
import cn.comtom.ebs.front.fw.AuthController;
import cn.comtom.ebs.front.main.core.service.IEbdService;
import cn.comtom.ebs.front.main.oauth2.Permissions;
import cn.comtom.tools.response.ApiPageResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @author:WJ
 * @date: 2019/1/3 0003
 * @time: 下午 2:19
 */

@Slf4j
@RestController
@RequestMapping("/safeRest")
@Api(tags = "信息中心", description = "信息中心")
@AuthRest
public class EbdController extends AuthController {

    @Autowired
    private IEbdService ebdService;

    @GetMapping("/ebd/page")
    @ApiOperation(value = "分页查询信息记录", notes = "分页查息记录")
    @RequiresPermissions(Permissions.CORE_EBD_QUERY)
    public ApiPageResponse<EbdInfo> page(@Valid EbdPageRequest req){
        return ebdService.findPage(req);
    }

}
