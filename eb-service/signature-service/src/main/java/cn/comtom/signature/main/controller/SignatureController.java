package cn.comtom.signature.main.controller;

import java.io.File;
import java.io.IOException;

import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;

import cn.comtom.domain.signature.req.CertApplyReq;
import cn.comtom.domain.signature.req.CertImportReq;
import cn.comtom.domain.signature.req.DataReq;
import cn.comtom.domain.signature.resp.CertResp;
//git.comtom.cn:18000/ct-ewbsv2.0/ct-ewbsv2.0.git
import cn.comtom.signature.main.constants.SignErrorEnum;
import cn.comtom.signature.main.fegin.service.SystemFeginService;
import cn.comtom.signature.main.hepler.SignatureHelper;
import cn.comtom.signature.main.service.ISignatureService;
import cn.comtom.tools.constants.Constants;
import cn.comtom.tools.enums.BasicError;
import cn.comtom.tools.response.ApiEntityResponse;
import cn.comtom.tools.response.ApiResponse;
import cn.comtom.tools.response.ApiResponseBuilder;
import cn.comtom.tools.utils.UUIDGenerator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/sign")
@Api(tags = "数字签名服务")
public class SignatureController {

    @Autowired
    private ISignatureService signatureService;
    
    @Autowired
    private SystemFeginService systemFeginService;
    
    /**
     * @return
     * true-使用签名服务器
     * false-使用签名密码器
     */
    private boolean isSignService () {
    	String sign_type = systemFeginService.getByKey(Constants.SIGNATURE_TYPE);
    	if(StringUtils.isNotBlank(sign_type) && sign_type.equals("0")) {
    		return false;
    	}
    	return true;
    }
    
    /**  
          * 删除  
     * @param files  
     */  
    private void deleteFile(File... files) {  
        for (File file : files) {  
            if (file.exists()) {  
                file.delete();  
            }  
        }  
    }
    
    @ApiImplicitParams({
        @ApiImplicitParam(value = "待验签文件",name = "xmlFile",required = true,allowMultiple = true,dataType = "MultipartFile")
    })
    @PostMapping(value="/verifySignByFile",consumes = "multipart/*")
    @ApiOperation(value = "验证签名", notes = "文件不能超过30M大小")
    public ApiResponse verifySign(@RequestParam(name = "xmlFile")  MultipartFile xmlFile, @RequestParam(name = "signValue")String signValue){
        if(xmlFile == null || StringUtils.isBlank(signValue)){
            return ApiResponseBuilder.buildEntityError(SignErrorEnum.PARAMS_ERROR);
        }
		try {
			Boolean b = false;
			if(isSignService()) {
				b = signatureService.verifyFileSign(xmlFile.getBytes(),signValue);
			}else {
				// 获取文件名
		        String fileName = xmlFile.getOriginalFilename();
		        // 获取文件后缀
		        String prefix=fileName.substring(fileName.lastIndexOf("."));
		        // 用uuid作为文件名，防止生成的临时文件重复
		        final File file = File.createTempFile(UUIDGenerator.getUUID(), prefix);
		        // MultipartFile to File
		        xmlFile.transferTo(file);
		        b = SignatureHelper.verifySign(file, signValue);
				//程序结束时，删除临时文件
				deleteFile(file);
			}
			log.info("验证签名verifySign,filename:{},signValue:{},result:{}", xmlFile.getOriginalFilename(),signValue,b);
			if(b){
	            return ApiResponse.ok();
	        }
		} catch (Exception e) {
			e.printStackTrace();
			return ApiResponseBuilder.buildError().error(SignErrorEnum.VERIFY_SIGN_FAILURE.getCode(), e.getMessage());
		}
		return ApiResponseBuilder.buildEntityError(SignErrorEnum.VERIFY_SIGN_FAILURE);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @ApiImplicitParams({
        @ApiImplicitParam(value = "待签名文件",name = "xmlFile",required = true,allowMultiple = true,dataType = "MultipartFile")
    })
	@PostMapping(value="/getSignByFile",consumes = "multipart/*")
    @ApiOperation(value = "获取签名", notes = "文件不能超过30M大小")
    public ApiEntityResponse<String> getSign(@RequestParam(name = "xmlFile")  MultipartFile xmlFile){
    	
        if(xmlFile == null){
            return ApiResponseBuilder.buildEntityError(SignErrorEnum.PARAMS_ERROR);
        }
		try {
			String sign = null;
			if(isSignService()) {
				sign = signatureService.getFileSign(xmlFile.getBytes());
			}else {
				// 获取文件名
		        String fileName = xmlFile.getOriginalFilename();
		        // 获取文件后缀
		        String prefix=fileName.substring(fileName.lastIndexOf("."));
		        // 用uuid作为文件名，防止生成的临时文件重复
		        final File file = File.createTempFile(UUIDGenerator.getUUID(), prefix);
		        // MultipartFile to File
		        xmlFile.transferTo(file);
		        sign = SignatureHelper.sign(file);
				//程序结束时，删除临时文件
				deleteFile(file);
			}
			ApiEntityResponse ok = ApiEntityResponse.ok(BasicError.OK_KEY_VALUE);
			ok.setData(sign);
			log.info("生成签名getSign,fileName:{},sign:{}",xmlFile.getOriginalFilename(),sign);
			return ok;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return ApiResponseBuilder.buildEntityError(SignErrorEnum.VERIFY_SIGN_FAILURE);
       
    }
   
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
	@PostMapping("/certApply")
    @ApiOperation(value = "通过证书安全代理申请证书信任列表", notes = "通过证书安全代理申请证书信任列表")
    public ApiEntityResponse<CertResp> certApply(@RequestBody @Valid CertApplyReq certApplyReq){
    	CertResp resp = signatureService.certApply(certApplyReq);
    	log.info("通过证书安全代理申请证书信任列表certApply,params:{}, 签名列表:{}",JSON.toJSONString(certApplyReq), resp);
    	if(resp==null || StringUtils.isBlank(resp.getCertauth())) {
    		return ApiResponseBuilder.buildEntityError(BasicError.OPT_FALID_VALUE);
    	}
    	ApiEntityResponse ok = ApiEntityResponse.ok(BasicError.OK_KEY_VALUE);
    	ok.setData(resp);
    	return ok;
    }
    
    
    @PostMapping("/getSignByStr")
    @ApiOperation(value = "获取签名", notes = "接收文本字符串")
    public ApiEntityResponse<String> getSignByStream(@RequestBody @Valid DataReq data) {
        try {
			String sign = signatureService.getSign(data.getData());
			ApiEntityResponse ok = ApiEntityResponse.ok(BasicError.OK_KEY_VALUE);
			ok.setData(sign);
			log.info("生成签名getSign, sign:{}",sign);
			return ok;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ApiResponseBuilder.buildEntityError(SignErrorEnum.VERIFY_SIGN_FAILURE);
    }

    @PostMapping(value="/verifySignByStr")
    @ApiOperation(value = "验证签名", notes = "接收文本字符串")
    public ApiResponse verifySignByStream(@RequestBody @Valid DataReq data){
        if(StringUtils.isBlank(data.getSignValue())){
            return ApiResponseBuilder.buildEntityError(SignErrorEnum.PARAMS_ERROR);
        }
		try {
			Boolean b = signatureService.verifySign(data.getData(),data.getSignValue());
			log.info("验证签名verifySign,signValue:{},result:{}", data ,data.getSignValue(),b);
			if(b){
	            return ApiResponse.ok();
	        }
		} catch (Exception e) {
			e.printStackTrace();
			return ApiResponseBuilder.buildError().error(SignErrorEnum.VERIFY_SIGN_FAILURE.getCode(), e.getMessage());
		}
		return ApiResponseBuilder.buildEntityError(SignErrorEnum.VERIFY_SIGN_FAILURE);
    }
    
    @PostMapping(value="/importCerth")
    @ApiOperation(value = "导入证书", notes = "导入证书")
    public ApiResponse importCerth(@RequestBody @Valid CertImportReq data){
        if(data.getCerth()==null || data.getCerth().length<1){
            return ApiResponseBuilder.buildEntityError(SignErrorEnum.PARAMS_ERROR);
        }
        try {
        	Boolean b = false;
	        if(isSignService()) {
	        	b = signatureService.importCerth(data.getCerth());
	        }else {
	        	b = SignatureHelper.platformImportTrustedCert(data.getCerth());
	        }
	        log.info("导入证书importCerth,result:{}", b);
			if(b){
	            return ApiResponse.ok();
	        }
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ApiResponseBuilder.buildEntityError(SignErrorEnum.IMPORT_CERT_FAILURE);
    }
    
    @PostMapping(value="/importCertauth")
    @ApiOperation(value = "导入证书链", notes = "导入证书链")
    public ApiResponse importCertauth(@RequestBody @Valid CertImportReq data){
        if(data.getCertauth()==null || data.getCertauth().length<1){
            return ApiResponseBuilder.buildEntityError(SignErrorEnum.PARAMS_ERROR);
        }
		try {
			Boolean b = signatureService.importCertauth(data.getCertauth());
			log.info("导入证书链importCertauth，result:{}", b);
			if(b){
	            return ApiResponse.ok();
	        }
		} catch (Exception e) {
			e.printStackTrace();
			return ApiResponseBuilder.buildError().error(SignErrorEnum.VERIFY_SIGN_FAILURE.getCode(), e.getMessage());
		}
		return ApiResponseBuilder.buildEntityError(SignErrorEnum.IMPORT_CERTH_FAILURE);
    }
    
}
