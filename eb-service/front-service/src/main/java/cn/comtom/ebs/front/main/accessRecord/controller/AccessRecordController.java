package cn.comtom.ebs.front.main.accessRecord.controller;

import cn.comtom.domain.core.access.info.AccessRecordInfo;
import cn.comtom.domain.core.access.request.AccessRecordPageRequest;
import cn.comtom.ebs.front.common.AuthRest;
import cn.comtom.ebs.front.fw.AuthController;
import cn.comtom.ebs.front.main.accessRecord.service.IAccessRecordService;
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
 * @date: 2018/12/25 0025
 * @time: 下午 7:38
 */

@Slf4j
@RestController
@RequestMapping("/safeRest")
@Api(tags = "接入记录", description = "接入记录")
@AuthRest
public class AccessRecordController extends AuthController {

    @Autowired
    private IAccessRecordService accessRecordService;


    @GetMapping("/access/record/page")
    @ApiOperation(value = "分页查询接入记录", notes = "分页查询接入记录")
    @RequiresPermissions(Permissions.CORE_ACCESS_QUERY)
    public ApiPageResponse<AccessRecordInfo> page(@Valid AccessRecordPageRequest req){
        return accessRecordService.findAccessRecordPage(req);
    }

}
