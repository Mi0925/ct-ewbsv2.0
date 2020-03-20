package cn.comtom.linkage.main.access.controller;

import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import cn.comtom.linkage.main.fegin.service.ISignatureFeginService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(value = "test")
@Slf4j
@Api(tags = "测试", description = "测试")
public class TestController {

	@Autowired
	private ISignatureFeginService signatureFeginService;
	
	   @SuppressWarnings({ "unchecked", "rawtypes" })
	    @ApiImplicitParams({
	        @ApiImplicitParam(value = "待签名文件",name = "xmlFile",required = true,allowMultiple = true,dataType = "MultipartFile")
	    })
		@PostMapping(value="/getSignByFile",consumes = "multipart/*")
	    @ApiOperation(value = "获取签名", notes = "文件不能超过30M大小")
	public String getSignByFile(@RequestParam(name = "xmlFile")  MultipartFile xmlFile) throws IOException{
		java.io.File file = new java.io.File("./"+xmlFile.getOriginalFilename());
		FileUtils.copyInputStreamToFile(xmlFile.getInputStream(), file);
		signatureFeginService.getSign(file);
        return "ok";
    }

}
