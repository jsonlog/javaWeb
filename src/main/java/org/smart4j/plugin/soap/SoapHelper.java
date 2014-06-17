package org.smart4j.plugin.soap;

import org.apache.cxf.frontend.ClientProxyFactoryBean;

/**
 * 用于提供 SOAP 插件相关工具方法
 *
 * @since 1.0
 * @author huangyong
 */
@Deprecated
public class SoapHelper {

    /**
     * 创建 SOAP 客户端
     */
    @Deprecated
    public static <T> T createClient(String wsdl, Class<? extends T> serviceClass) {
        ClientProxyFactoryBean factory = new ClientProxyFactoryBean();
        factory.setAddress(wsdl);
        factory.setServiceClass(serviceClass);
        return factory.create(serviceClass);
    }
}
