package org.smart4j.plugin.soap;

import java.util.ArrayList;
import java.util.List;
import org.apache.cxf.frontend.ServerFactoryBean;
import org.apache.cxf.interceptor.Interceptor;
import org.apache.cxf.interceptor.LoggingInInterceptor;
import org.apache.cxf.interceptor.LoggingOutInterceptor;
import org.apache.cxf.message.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smart4j.framework.plugin.Plugin;

/**
 * 用于实现 SOAP 插件接口
 *
 * @since 1.1
 * @author huangyong
 */
public class SoapPlugin implements Plugin {

    private static final Logger logger = LoggerFactory.getLogger(SoapPlugin.class);

    private static final List<Interceptor<? extends Message>> inInterceptorList = new ArrayList<Interceptor<? extends Message>>();
    private static final List<Interceptor<? extends Message>> outInterceptorList = new ArrayList<Interceptor<? extends Message>>();

    @Override
    public void init() {
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

    @Override
    public void destroy() {
    }
}
