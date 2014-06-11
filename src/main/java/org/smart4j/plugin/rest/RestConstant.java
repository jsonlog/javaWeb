package org.smart4j.plugin.rest;

/**
 * 定义 REST 常量
 *
 * @since 1.0
 * @author huangyong
 */
public interface RestConstant {

    String SERVLET_URL = "/rest/*";

    String REST_LOG = "smart.plugin.rest.log";

    String REST_JSONP = "smart.plugin.rest.jsonp";
    String REST_JSONP_FUNCTION = "smart.plugin.rest.jsonp.function";

    String REST_CORS = "smart.plugin.rest.cors";
    String REST_CORS_ORIGIN = "smart.plugin.rest.cors.origin";
}
