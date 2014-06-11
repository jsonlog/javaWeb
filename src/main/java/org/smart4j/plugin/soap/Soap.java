package org.smart4j.plugin.soap;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * SOAP 注解
 * <br/>
 * 注意：必须定义在接口上
 *
 * @since 1.0
 * @author huangyong
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Soap {

    /**
     * 相对路径
     */
    String value() default "";
}
