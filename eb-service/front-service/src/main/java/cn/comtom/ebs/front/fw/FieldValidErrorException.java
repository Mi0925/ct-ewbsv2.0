package cn.comtom.ebs.front.fw;

import cn.comtom.tools.enums.BasicError;
import cn.comtom.tools.exception.RRException;

import java.util.List;

/**
 * 属性验证异常
 * @author guomao
 */
public class FieldValidErrorException extends RRException {

    private List<FieldValidError> errors;

    public FieldValidErrorException(List<FieldValidError> errors){
        super(BasicError.VALID_ERROR.getCode(),BasicError.VALID_ERROR.getCode());
        this.errors = errors;
    }

    public List<FieldValidError> getErrors(){
        return this.errors;
    }

    public FieldValidErrorException(FieldValidError error){
        super(BasicError.VALID_ERROR.getCode(),error.getErrorMessage());
    }

}
