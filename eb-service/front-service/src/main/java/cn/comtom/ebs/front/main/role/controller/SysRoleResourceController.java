package cn.comtom.ebs.front.main.role.controller;

import cn.comtom.domain.system.roleresource.request.RoleResAddBatchRequest;
import cn.comtom.ebs.front.common.AuthRest;
import cn.comtom.ebs.front.common.FrontErrorEnum;
import cn.comtom.ebs.front.fw.AuthController;
import cn.comtom.ebs.front.main.role.service.ISysRoleResourceService;
import cn.comtom.tools.response.ApiResponse;
import cn.comtom.tools.response.ApiResponseBuilder;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/safeRest/roleRes")
@Api(tags = "角色资源管理", description = "角色资源管理")
@AuthRest
public class SysRoleResourceController extends AuthController {


    @Autowired
    private ISysRoleResourceService sysRoleResourceService;

    @PostMapping("/saveRoleResBatch")
    @ApiOperation(value = "角色批量绑定系统资源", notes = "角色批量绑定系统资源")
    public ApiResponse saveRoleResBatch(@RequestBody @Valid RoleResAddBatchRequest request, BindingResult result) {
        if(log.isDebugEnabled()){
            log.debug("[!~] save sysRole system resource batch .request =[{}] ",request);
        }
        if(sysRoleResourceService.deleteRoleResourceByRoleId(request.getRoleId())){
            if(sysRoleResourceService.saveRoleResBatch(request)){
                return ApiResponse.ok();
            }
        }
        return ApiResponseBuilder.buildError(FrontErrorEnum.ROLE_SYSRESOURCE_BIND_FAILURE);
    }

}
