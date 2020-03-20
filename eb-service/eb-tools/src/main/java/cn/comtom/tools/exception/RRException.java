package cn.comtom.tools.exception;


import cn.comtom.tools.enums.ErrorCode;
import lombok.Data;

/**
 * 自定义异常
 *
 * @author comtom
 */
@Data
public class RRException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    private String code;

    private String msg;

    public RRException(ErrorCode errorCode){
        this(errorCode.getCode(), null, null);
    }

    public RRException(String code, String msg) {
        this(code, msg, null);
    }

    public RRException(String code, String msg, Throwable e) {
        super(msg, e);
        this.msg = msg;
        this.code = code;
    }

}
