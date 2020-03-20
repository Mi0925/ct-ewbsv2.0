package cn.comtom.ebs.front.config;


import cn.comtom.ebs.front.fw.FieldValidErrorException;
import cn.comtom.tools.enums.BasicError;
import cn.comtom.tools.exception.ApiException;
import cn.comtom.tools.exception.RRException;
import cn.comtom.tools.response.ApiEntityResponse;
import cn.comtom.tools.response.ApiResponse;
import cn.comtom.tools.response.ApiResponseBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLException;

/**
 * 异常处理器
 *
 * @author guomao
 */
@RestControllerAdvice
@Slf4j
public class RRExceptionHandler {

    /**
     * 自定义异常
     */
    @ExceptionHandler(RRException.class)
    public ApiResponse handleRRException(RRException e) {
        ApiResponse response = new ApiResponse();
        response.setCode(e.getCode());
        String message = e.getMessage();
        if(message == null){
            message = e.getCode();
        }
        response.setMsg(message);
        response.setSuccessful(false);
        return response;
    }

    @ExceptionHandler(FieldValidErrorException.class)
    public ApiResponse handleApiException(FieldValidErrorException e) {
        return ApiResponse.error(e.getCode(),e.getMsg());
    }

    @ExceptionHandler(ApiException.class)
    public ApiResponse handleApiException(ApiException e) {
        ApiEntityResponse response = ApiResponseBuilder.buildEntityError(BasicError.API_ERROR);
        response.setErrorInfo(buildData(e));
        return response;
    }

    @ExceptionHandler(Exception.class)
    public ApiResponse handleException(Exception e) {
        log.error(e.getMessage(), e);
        return ApiResponseBuilder.buildError(BasicError.INNER_ERROR);
    }

    private Object buildData(ApiException e) {
        return e.getCode() + ":" + e.getMsg();
    }

}
