package org.smart4j.plugin.soap;

import java.util.ArrayList;
import java.util.List;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.frontend.ClientProxyFactoryBean;
import org.apache.cxf.frontend.ServerFactoryBean;
import org.apache.cxf.interceptor.Interceptor;
import org.apache.cxf.interceptor.LoggingInInterceptor;
import org.apache.cxf.interceptor.LoggingOutInterceptor;
import org.apache.cxf.jaxws.endpoint.dynamic.JaxWsDynamicClientFactory;
import org.apache.cxf.message.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 用于发布 SOAP 服务
 *
 * @since 1.0
 * @author huangyong
 */
public class SoapHelper {

    private static final Logger logger = LoggerFactory.getLogger(SoapHelper.class);

    private static final List<Interceptor<? extends Message>> inInterceptorList = new ArrayList<Interceptor<? extends Message>>();
    private static final List<Interceptor<? extends Message>> outInterceptorList = new ArrayList<Interceptor<? extends Message>>();

    static {
        addLogingInterceptor();
    }

    private static void addLogingInterceptor() {
        if (SoapConfig.isLog()) {
            LoggingInInterceptor loggingInInterceptor = new LoggingInInterceptor();
            inInterceptorList.add(loggingInInterceptor);
            LoggingOutInterceptor loggingOutInterceptor = new LoggingOutInterceptor();
            outInterceptorList.add(loggingOutInterceptor);
        }
    }

    public static void publishService(String address, Class<?> interfaceClass, Object implementInstance) {
        ServerFactoryBean factory = new ServerFactoryBean();
        factory.setAddress(address);
        factory.setServiceClass(interfaceClass);
        factory.setServiceBean(implementInstance);
        factory.setInInterceptors(inInterceptorList);
        factory.setOutInterceptors(outInterceptorList);
        factory.create();
        logger.debug("Publish SOAP Service: " + interfaceClass.getName());
    }

    /**
     * 创建 SOAP 客户端
     */
    public static <T> T createClient(String wsdl, Class<? extends T> interfaceClass) {
        ClientProxyFactoryBean factory = new ClientProxyFactoryBean();
        factory.setAddress(wsdl);
        factory.setServiceClass(interfaceClass);
        factory.setInInterceptors(inInterceptorList);
        factory.setOutInterceptors(outInterceptorList);
        return factory.create(interfaceClass);
    }

    /**
     * 调用 SOAP WS 操作
     *
     * @since 1.1
     */
    public static Object[] invokeOperation(String wsdl, String operationName, Object... operationParams) {
        Object[] results = null;
        JaxWsDynamicClientFactory factory = JaxWsDynamicClientFactory.newInstance();
        Client client = factory.createClient(wsdl);
        if (client != null) {
            try {
                results = client.invoke(operationName, operationParams);
            } catch (Exception e) {
                logger.error("调用 SOAP WS 操作失败！", e);
            }
        }
        return results;
    }
}
