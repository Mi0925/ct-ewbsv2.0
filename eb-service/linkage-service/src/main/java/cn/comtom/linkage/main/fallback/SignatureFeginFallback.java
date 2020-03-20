package cn.comtom.linkage.main.fallback;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import cn.comtom.linkage.main.fegin.SignatureFegin;
import cn.comtom.tools.enums.BasicError;
import cn.comtom.tools.response.ApiEntityResponse;
import cn.comtom.tools.response.ApiResponse;
import cn.comtom.tools.response.ApiResponseBuilder;

@Component
public class SignatureFeginFallback implements SignatureFegin {
    @Override
    public ApiResponse verifySign(MultipartFile xmlFile, String signValue) {
        return ApiResponseBuilder.buildError(BasicError.FEIGN_ERROR);
    }

    @Override
    public ApiEntityResponse<String> getSign(MultipartFile xmlFile) {
        return ApiResponseBuilder.buildEntityError(BasicError.FEIGN_ERROR);
    }
}
