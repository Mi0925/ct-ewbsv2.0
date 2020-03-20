package cn.comtom.tools.response;

import cn.comtom.tools.enums.BasicError;
import cn.comtom.tools.enums.ErrorCode;

/**
 * @author guomao
 * @Date 2018-10-29 11:11
 */
public class ApiResponseBuilder {

    public static ApiResponse buildOk(){
        return buildError(BasicError.OK_KEY_VALUE);
    }

    public static ApiResponse buildError(){
        return buildError(BasicError.INNER_ERROR);
    }

    public static ApiResponse buildError(ErrorCode error) {
        String code = error.getCode();
        String msg=error.getMsg();
        return new ApiResponse(false, code,msg);
    }

    public static ApiEntityResponse buildEntityError(ErrorCode error){
        String code = error.getCode();
        String msg=error.getMsg();
        return ApiEntityResponse.error(code,msg);
    }

    public static ApiListResponse buildListError(ErrorCode error){
        String code = error.getCode();
        String msg=error.getMsg();
        return ApiListResponse.error(code,msg);
    }

}
