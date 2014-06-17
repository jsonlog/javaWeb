package org.smart4j.plugin.rest;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用于标注 REST 接口
 *
 * @since 1.0
 * @author huangyong
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Rest {

    /**
     * 相对路径
     */
    String value() default "";
}
