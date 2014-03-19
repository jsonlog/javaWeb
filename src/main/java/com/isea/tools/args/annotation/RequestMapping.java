package com.isea.tools.args.annotation;

import java.lang.annotation.*;

/**
 * Description: RequestMapping
 * Author: liuzh
 * Update: liuzh(2014-03-18 14:09)
 */

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequestMapping {
    /**
     * url
     */
    String value() default "";
}
