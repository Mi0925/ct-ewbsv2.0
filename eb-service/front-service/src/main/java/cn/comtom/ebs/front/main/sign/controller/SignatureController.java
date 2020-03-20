package cn.comtom.ebs.front.main.sign.controller;

import java.io.IOException;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import cn.comtom.ebs.front.common.FrontErrorEnum;
import cn.comtom.ebs.front.fw.AuthController;
import cn.comtom.ebs.front.main.sign.service.ISignatureService;
import cn.comtom.tools.response.ApiEntityResponse;
import cn.comtom.tools.response.ApiResponse;
import cn.comtom.tools.response.ApiResponseBuilder;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

@RequestMapping("/safeRest/cert")
@RestController
@Api(tags = "证书管理", description = "证书管理")
@Slf4j
public class SignatureController  extends AuthController{
	
	@Autowired
	private ISignatureService signatureService;
	
    @PostMapping("/importCert")
    @ApiImplicitParams({@ApiImplicitParam(value = "证书文件",name = "multipartFile",required = true,allowMultiple = true,dataType = "MultipartFile")})
    @ApiOperation(value = "导入证书/证书链", notes = "导入证书/证书链")
    public ApiResponse importCert(Integer filetype, MultipartFile multipartFile){
        if(log.isDebugEnabled()){
            log.debug("[!~] importCert files ");
        }
        if(filetype == null){
            return  ApiResponseBuilder.buildEntityError(FrontErrorEnum.REQUIRED_PARAM_EMPTY);
        }
        boolean importResult = false;
        try {
			byte[] certh = multipartFile.getBytes();
			if(filetype == 1) {
				importResult = signatureService.importCerth(certh);
			}else {
				importResult = signatureService.importCertauth(certh);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
            return ApiEntityResponse.ok();
    }
    
}
