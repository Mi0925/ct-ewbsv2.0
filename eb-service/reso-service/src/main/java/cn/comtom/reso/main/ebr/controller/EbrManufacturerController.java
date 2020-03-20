package cn.comtom.reso.main.ebr.controller;

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

import cn.comtom.domain.reso.ebr.info.EbrAdapterInfo;
import cn.comtom.domain.reso.ebr.info.EbrManufacturerInfo;
import cn.comtom.domain.reso.ebr.request.AdapterAddRequest;
import cn.comtom.domain.reso.ebr.request.EbrManufactPageRequest;
import cn.comtom.reso.fw.BaseController;
import cn.comtom.reso.main.constants.ResErrorEnum;
import cn.comtom.reso.main.ebr.entity.dbo.EbrAdapter;
import cn.comtom.reso.main.ebr.entity.dbo.EbrManufacturer;
import cn.comtom.reso.main.ebr.service.IEbrManufacturerService;
import cn.comtom.tools.enums.StateDictEnum;
import cn.comtom.tools.response.ApiEntityResponse;
import cn.comtom.tools.response.ApiPageResponse;
import cn.comtom.tools.response.ApiResponse;
import cn.comtom.tools.response.ApiResponseBuilder;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/res/ebr/manufacturer")
@Api(tags = "厂商管理")
@Slf4j
public class EbrManufacturerController extends BaseController{
	
    @Autowired
    private IEbrManufacturerService ebrManufacturerService;

	    @GetMapping("/{id}")
	    @ApiOperation(value = "根据id查询厂商信息", notes = "根据id查询厂商信息")
	    public ApiEntityResponse<EbrManufacturerInfo> getById(@PathVariable String id){
		    EbrManufacturer bean = ebrManufacturerService.selectById(id);
	        if(bean == null){
	            return ApiResponseBuilder.buildEntityError(ResErrorEnum.QUERY_NO_DATA);
	        }
	        EbrManufacturerInfo info = new EbrManufacturerInfo();
	        BeanUtils.copyProperties(bean,info);
	        return ApiEntityResponse.ok(info);
	    }

	    @GetMapping("/page")
	    @ApiOperation(value = "分页查厂商信息", notes = "分页查询厂商信息")
	    public ApiPageResponse<EbrManufacturerInfo> getByPage(@ModelAttribute EbrManufactPageRequest request){
	        if(log.isDebugEnabled()){
	            log.debug("get ebr EbrManufacturerInfo info by page . request=[{}]",request);
	        }
	        startPage(request);
	        List<EbrManufacturer> list = ebrManufacturerService.getList(request);
	        List copyList = copyList(list,EbrManufacturerInfo.class);
	        return ApiPageResponse.ok(copyList,page);
	    }

	    @PutMapping("/update")
	    @ApiOperation(value = "更新厂商信息", notes = "更新厂商信息")
	    public ApiEntityResponse<Integer> update(@RequestBody @Valid EbrManufacturer request){
	        if(log.isDebugEnabled()){
	            log.debug("update ebr EbrManufacturerInfo info by ebrId . request=[{}]  ",request);
	        }
	        if(org.apache.commons.lang3.StringUtils.isBlank(request.getId())) {
	        	 return ApiResponseBuilder.buildEntityError(ResErrorEnum.REQUIRED_PARAMS_EMPTY);
	        }
	        request.setUpdateTime(new Date());
	        return ApiEntityResponse.ok(ebrManufacturerService.update(request));
	    }

	    @PostMapping("/save")
	    @ApiOperation(value = "新增厂商信息", notes = "新增厂商信息")
	    public ApiEntityResponse<EbrManufacturer> save(@RequestBody @Valid EbrManufacturer request){
	        if(log.isDebugEnabled()){
	            log.debug("save ebr EbrManufacturerInfo info . request=[{}]  ",request);
	        }
	        request.setCreateTime(new Date());
	        ebrManufacturerService.save(request);
	        return ApiEntityResponse.ok(request);
	    }

	    @DeleteMapping("/delete/{id}")
	    @ApiOperation(value = "删除适配器信息", notes = "删除适配器信息")
	    public ApiResponse delete(@PathVariable(name = "id") String id){
	        if(log.isDebugEnabled()){
	            log.debug("delete  EbrManufacturerInfo info by ebrId . id=[{}]  ",id);
	        }
	        if(StringUtils.isBlank(id)){
	            return ApiResponseBuilder.buildError(ResErrorEnum.REQUIRED_PARAMS_EMPTY);
	        }
	        ebrManufacturerService.deleteById(id);
	        return ApiResponse.ok();
	    }
}
