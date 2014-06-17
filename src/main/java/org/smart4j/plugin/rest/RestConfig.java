package org.smart4j.plugin.rest;

import org.smart4j.framework.core.ConfigHelper;

/**
 * 用于获取 REST 插件相关配置项
 *
 * @since 1.1
 * @author huangyong
 */
public class RestConfig {

    public static boolean isLog() {
        return ConfigHelper.getBoolean(RestConstant.REST_LOG);
    }

    public static boolean isJsonp() {
        return ConfigHelper.getBoolean(RestConstant.REST_JSONP);
    }

    public static String getJsonpFunction() {
        return ConfigHelper.getString(RestConstant.REST_JSONP_FUNCTION);
    }

    public static boolean isCors() {
        return ConfigHelper.getBoolean(RestConstant.REST_CORS);
    }

    public static String getCorsOrigin() {
        return ConfigHelper.getString(RestConstant.REST_CORS_ORIGIN);
    }
}
