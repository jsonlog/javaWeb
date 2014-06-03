package org.smart4j.plugin.security.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 判断当前用户是否拥有某种权限
 *
 * @author huangyong
 * @since 2.3
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface HasPermissions {

    /**
     * 权限字符串
     */
    String value();
}
