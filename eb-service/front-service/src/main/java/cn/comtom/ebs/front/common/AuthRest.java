package cn.comtom.ebs.front.common;

import java.lang.annotation.*;

@Inherited
@Target({ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface AuthRest {

}
