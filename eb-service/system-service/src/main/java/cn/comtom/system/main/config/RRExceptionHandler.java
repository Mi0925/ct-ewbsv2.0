package cn.comtom.system.main.config;


import cn.comtom.system.fw.FieldValidErrorException;
import cn.comtom.tools.enums.BasicError;
import cn.comtom.tools.exception.ApiException;
import cn.comtom.tools.exception.RRException;
import cn.comtom.tools.response.ApiEntityResponse;
import cn.comtom.tools.response.ApiResponse;
import cn.comtom.tools.response.ApiResponseBuilder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLException;

/**
 * 异常处理器
 *
 * @author guomao
 */
@RestControllerAdvice
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


    @ExceptionHandler(SQLException.class)
    public ApiResponse handleApiException(SQLException e) {
        return ApiResponseBuilder.buildError(BasicError.INNER_ERROR);
    }


    @ExceptionHandler(FieldValidErrorException.class)
    public ApiEntityResponse handleApiException(FieldValidErrorException e) {
        ApiEntityResponse response = ApiResponseBuilder.buildEntityError(BasicError.VALID_ERROR);
        response.setErrorInfo(e.getErrors());
        return response;
    }

    @ExceptionHandler(ApiException.class)
    public ApiResponse handleApiException(ApiException e) {
        ApiEntityResponse response = ApiResponseBuilder.buildEntityError(BasicError.API_ERROR);
        response.setErrorInfo(buildData(e));
        return response;
    }

    private Object buildData(ApiException e) {
        return e.getCode() + ":" + e.getMsg();
    }

}
