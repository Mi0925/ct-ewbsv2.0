package cn.comtom.ebs.front.main.org.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.comtom.domain.system.org.OrgPageRequest;
import cn.comtom.domain.system.org.OrganizationInfo;
import cn.comtom.ebs.front.fw.AuthController;
import cn.comtom.ebs.front.main.org.service.IOrganizationService;
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
@RequestMapping("/safeRest/sys/org")
@Api(tags = "组织机构管理")
public class OrganizationController extends AuthController {


    @Autowired
    private IOrganizationService organizationService;


    @GetMapping("/get/{id}")
    @ApiOperation(value = "根据Id查询组织机构详情", notes = "根据Id查询组织机构详情")
    public ApiEntityResponse<OrganizationInfo> getById(@PathVariable(name = "id",required = true) String id){
        if(log.isDebugEnabled()){
            log.debug("[!~] get Organization by ebrId. ebrId=[{}]",id);
        }
        OrganizationInfo result = organizationService.getOrgInfoById(id);
        return ApiEntityResponse.ok(result);
    }

    @GetMapping("/page")
    @ApiOperation(value = "分页查询组织机构信息", notes = "分页查询组织机构信息")
    public ApiPageResponse<OrganizationInfo> getByPage(@Valid OrgPageRequest request){
        if(log.isDebugEnabled()){
            log.debug("[!~] get Organization  by page. request=[{}]",request);
        }
        ApiPageResponse<OrganizationInfo> result = organizationService.getOrgInfoByPage(request);
        return result;
    }

    @PostMapping("/save")
    @ApiOperation(value = "保存组织机构信息", notes = "保存组织机构信息")
    public ApiEntityResponse<OrganizationInfo> save(@RequestBody @Valid OrganizationInfo request){
        if(log.isDebugEnabled()){
            log.debug("[!~] save Organization. request=[{}]",request);
        }
        OrganizationInfo result = organizationService.saveOrgInfo(request);
        return ApiEntityResponse.ok(result);
    }

    @PutMapping("/update")
    @ApiOperation(value = "更新组织机构信息", notes = "更新组织机构信息")
    public ApiResponse update(@RequestBody @Valid OrganizationInfo request){
        if(log.isDebugEnabled()){
            log.debug("[!~] update Organization. request=[{}]",request);
        }
        organizationService.updateOrgInfo(request);

        return ApiResponse.ok();
    }

    @DeleteMapping("/delete/{id}")
    @ApiOperation(value = "删除组织机构", notes = "删除组织机构")
    public ApiResponse delete(@PathVariable(name = "id",required = true)  String id){
        if(log.isDebugEnabled()){
            log.debug("[!~] delete Organization  by ebrId. ebrId=[{}]",id);
        }
        Boolean success = organizationService.deleteOrgInfo(id);
        return ApiResponse.ok();
    }
}
