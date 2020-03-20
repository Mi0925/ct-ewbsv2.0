package cn.comtom.ebs.front.fegin;

import java.io.File;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import cn.comtom.domain.signature.req.CertImportReq;
import cn.comtom.tools.response.ApiEntityResponse;
import cn.comtom.tools.response.ApiResponse;

/**
 * Create By wujiang on 2018/11/20
 */
@FeignClient(value = "signature-service")
public interface SignatureFegin {

    @RequestMapping(value = "/sign/verify",method = RequestMethod.POST,produces = {MediaType.APPLICATION_JSON_UTF8_VALUE},consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ApiResponse verifySign(@RequestParam(name = "xmlFile") File xmlFile, @RequestParam(name = "signValue") String signValue);

    @RequestMapping(value = "/sign/get",method = RequestMethod.GET,produces = {MediaType.APPLICATION_JSON_UTF8_VALUE},consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiEntityResponse<String> getSign(@RequestParam(name = "xmlFile") File xmlFile);
    
    @PostMapping(value = "/sign/importCerth")
    ApiEntityResponse<Boolean> importCerth(@RequestBody CertImportReq request);
    
    @PostMapping(value = "/sign/importCertauth")
    ApiEntityResponse<Boolean> importCertauth(@RequestBody CertImportReq request);
}
