package cn.comtom.tools.exception;

public class ApiException extends RRException {


    public ApiException(String code, String msg) {
        this(code, msg, null);
    }

    public ApiException(String code, String msg, Throwable e) {
        super(code, msg, e);
    }

}
