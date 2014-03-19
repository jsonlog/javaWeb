package com.isea.tools.parameter.annotation;

import java.lang.annotation.*;

/**
 * Description: SessionAttributes
 * Author: liuzh
 * Update: liuzh(2014-03-18 13:11)
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SessionAttributes {

    /**
     * The name of the request session to bind to.
     */
    String value() default "";

    /**
     * Whether the session is required.
     * <p>Default is <code>true</code>, leading to an exception thrown in case
     * of the session missing in the request. Switch this to <code>false</code>
     * if you prefer a <code>null</value> in case of the session missing.
     * <p>Alternatively, provide a {@link #defaultValue() defaultValue},
     * which implicitely sets this flag to <code>false</code>.
     */
    boolean required() default true;

    /**
     * The default value to use as a fallback. Supplying a default value implicitely
     * sets {@link #required()} to false.
     */
    String defaultValue() default ValueConstants.DEFAULT_NONE;
}
