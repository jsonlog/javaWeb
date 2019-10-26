package org.smart4j.plugin.hessian;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用于标注 Hessian 接口
 *
 * @since 1.0
 * @author huangyong
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Hessian {

    /**
     * 相对路径
     */
    String value() default "";
}
