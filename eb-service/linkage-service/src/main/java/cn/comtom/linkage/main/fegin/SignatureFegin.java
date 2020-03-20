package cn.comtom.linkage.main.fegin;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import cn.comtom.tools.response.ApiEntityResponse;
import cn.comtom.tools.response.ApiResponse;

/**
 * Create By wujiang on 2018/11/20
 */
@FeignClient(value = "signature-service")
public interface SignatureFegin {

    @RequestMapping(value = "/sign/verifySignByFile",method = RequestMethod.POST,produces = {MediaType.APPLICATION_JSON_UTF8_VALUE},consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ApiResponse verifySign(@RequestPart("xmlFile") MultipartFile xmlFile, @RequestParam(value = "signValue")String signValue);

    @RequestMapping(value = "/sign/getSignByFile",method = RequestMethod.POST,produces = {MediaType.APPLICATION_JSON_UTF8_VALUE},consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiEntityResponse<String> getSign(@RequestPart("xmlFile") MultipartFile xmlFile);
    
}
