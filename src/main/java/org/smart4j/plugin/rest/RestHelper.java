package org.smart4j.plugin.rest;

import org.apache.cxf.jaxrs.client.JAXRSClientFactory;

/**
 * 用于提供 REST 插件相关工具方法
 *
 * @since 1.0
 * @author huangyong
 */
@Deprecated
public class RestHelper {

    /**
     * 创建 REST 客户端
     */
    @Deprecated
    public static <T> T createClient(String wadl, Class<? extends T> resourceClass) {
        return JAXRSClientFactory.create(wadl, resourceClass);
    }
}
