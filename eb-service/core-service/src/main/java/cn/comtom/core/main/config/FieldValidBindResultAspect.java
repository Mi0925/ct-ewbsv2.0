package cn.comtom.core.main.config;

import cn.comtom.core.fw.FieldValidError;
import cn.comtom.core.fw.FieldValidErrorException;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * controller入参核验切面
 * @author wj
 */
@Aspect
@Component
public class FieldValidBindResultAspect {

    @Pointcut(value = "execution(* cn.comtom.core..*.controller..*Controller.*(..,org.springframework.validation.BindingResult,..))")
    public void validPointCut() {

    }

	@Before(value="validPointCut()" )
	public void valid(JoinPoint joinPoint) {
        //请求的参数
		Object[] args = joinPoint.getArgs();
		for(int i=0 ;i<args.length;i++){
		    Object obj = args[i];
		    if(obj instanceof BindingResult){
		        BindingResult bResult = (BindingResult)obj;
                if (bResult.hasErrors()) {
                    FieldValidErrorException fieldValidErrorException;
                    fieldValidErrorException = fieldValidErrorResponse(bResult);
                    throw fieldValidErrorException;
                }
                break;
            }
        }
	}

    private FieldValidErrorException fieldValidErrorResponse(BindingResult bResult){
        List<FieldValidError> errors = new ArrayList<>();
        List<ObjectError> allErrors = bResult.getAllErrors();
        for (Iterator<ObjectError> it = allErrors.iterator(); it.hasNext(); ) {
            ObjectError error = it.next();
            if (error instanceof FieldError) {
                FieldError fieldError = (FieldError) error;
                FieldValidError fieldValidError = new FieldValidError();
                fieldValidError.setFieldId(fieldError.getField());
                fieldValidError.setValue(fieldError.getRejectedValue());
                fieldValidError.setErrorMessage(fieldError.getDefaultMessage());
                errors.add(fieldValidError);
            }
        }
        return new FieldValidErrorException(errors);
    }

}
