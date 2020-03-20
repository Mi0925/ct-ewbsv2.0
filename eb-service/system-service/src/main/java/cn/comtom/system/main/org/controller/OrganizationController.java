package cn.comtom.system.main.org.controller;

import java.util.Date;
import java.util.List;

import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.comtom.domain.system.org.OrgPageRequest;
import cn.comtom.domain.system.org.OrganizationInfo;
import cn.comtom.system.fw.BaseController;
import cn.comtom.system.main.constants.SystemErrorEnum;
import cn.comtom.system.main.org.entity.dbo.Organization;
import cn.comtom.system.main.org.service.IOrganizationService;
import cn.comtom.tools.response.ApiEntityResponse;
import cn.comtom.tools.response.ApiPageResponse;
import cn.comtom.tools.response.ApiResponse;
import cn.comtom.tools.response.ApiResponseBuilder;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/system/org")
@Api(tags = "组织机构管理")
@Slf4j
public class OrganizationController extends BaseController{
	
    @Autowired
    private IOrganizationService organizationService;

	    @GetMapping("/get/{id}")
	    @ApiOperation(value = "根据id查询组织机构信息", notes = "根据id查询组织机构信息")
	    public ApiEntityResponse<OrganizationInfo> getById(@PathVariable(name="id",required = true) String id){
	    	OrganizationInfo info = organizationService.getOrgById(id);
	        if(info == null){
	            return ApiResponseBuilder.buildEntityError(SystemErrorEnum.QUERY_NO_DATA);
	        }
	        return ApiEntityResponse.ok(info);
	    }

	    @ApiOperation(value = "分页查组织机构信息", notes = "分页查询组织机构信息")
	    @GetMapping("/page")
	    public ApiPageResponse<OrganizationInfo> getByPage(@ModelAttribute OrgPageRequest request){
	        if(log.isDebugEnabled()){
	            log.debug("get Organization info by page . request=[{}]",request);
	        }
	        startPage(request);
	        List<OrganizationInfo> list = organizationService.getList(request);
	        return ApiPageResponse.ok(list,page);
	    }

	    @PutMapping("/update")
	    @ApiOperation(value = "更新组织机构信息", notes = "更新组织机构信息")
	    public ApiEntityResponse<Integer> update(@RequestBody @Valid OrganizationInfo request){
	        if(log.isDebugEnabled()){
	            log.debug("update Organization info by ebrId . request=[{}]  ",request);
	        }
	        if(org.apache.commons.lang3.StringUtils.isBlank(request.getId())) {
	        	 return ApiResponseBuilder.buildEntityError(SystemErrorEnum.REQUIRED_PARAM_EMPTY);
	        }
	        request.setUpdateTime(new Date());
	        Organization  organization = new Organization();
	        BeanUtils.copyProperties(request, organization);
	        organization.setUpdateTime(new Date());
	        return ApiEntityResponse.ok(organizationService.update(organization));
	    }

	    @PostMapping("/save")
	    @ApiOperation(value = "新增组织机构信息", notes = "新增组织机构信息")
	    public ApiEntityResponse<Organization> save(@RequestBody @Valid Organization request){
	        if(log.isDebugEnabled()){
	            log.debug("save Organization info . request=[{}]  ",request);
	        }
	        request.setCreateTime(new Date());
	        organizationService.save(request);
	        return ApiEntityResponse.ok(request);
	    }

	    @DeleteMapping("/delete/{id}")
	    @ApiOperation(value = "删除组织机构信息", notes = "删除组织机构信息")
	    public ApiResponse delete(@PathVariable(name = "id",required = true) String id){
	        if(log.isDebugEnabled()){
	            log.debug("delete Organization info by ebrId . id=[{}]  ",id);
	        }
	        if(StringUtils.isBlank(id)){
	            return ApiResponseBuilder.buildError(SystemErrorEnum.REQUIRED_PARAM_EMPTY);
	        }
	        organizationService.deleteById(id);
	        return ApiResponse.ok();
	    }
}
