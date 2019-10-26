package org.smart4j.plugin.rest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.cxf.interceptor.Interceptor;
import org.apache.cxf.interceptor.LoggingInInterceptor;
import org.apache.cxf.interceptor.LoggingOutInterceptor;
import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.apache.cxf.jaxrs.lifecycle.SingletonResourceProvider;
import org.apache.cxf.jaxrs.provider.jsonp.JsonpInInterceptor;
import org.apache.cxf.jaxrs.provider.jsonp.JsonpPostStreamInterceptor;
import org.apache.cxf.jaxrs.provider.jsonp.JsonpPreStreamInterceptor;
import org.apache.cxf.message.Message;
import org.apache.cxf.rs.security.cors.CrossOriginResourceSharingFilter;
import org.codehaus.jackson.jaxrs.JacksonJsonProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smart4j.framework.plugin.Plugin;
import org.smart4j.framework.util.StringUtil;

/**
 * 用于实现 REST 插件接口
 *
 * @since 1.1
 * @author huangyong
 */
public class RestPlugin implements Plugin {

    private static final Logger logger = LoggerFactory.getLogger(RestPlugin.class);

    private static final List<Object> providerList = new ArrayList<Object>();
    private static final List<Interceptor<? extends Message>> inInterceptorList = new ArrayList<Interceptor<? extends Message>>();
    private static final List<Interceptor<? extends Message>> outInterceptorList = new ArrayList<Interceptor<? extends Message>>();

    @Override
    public void init() {
        addJsonProvider();
        addLogingInterceptor();
        addJsonpInterceptor();
        addCorsProvider();
    }

    private static void addJsonProvider() {
//        JSONProvider jsonProvider = new JSONProvider(); // 基于 Jettison 实现
        JacksonJsonProvider jsonProvider = new JacksonJsonProvider(); // 基于 Jackson 实现
        providerList.add(jsonProvider);
    }

    private static void addLogingInterceptor() {
        if (RestConfig.isLog()) {
            LoggingInInterceptor loggingInInterceptor = new LoggingInInterceptor();
            inInterceptorList.add(loggingInInterceptor);
            LoggingOutInterceptor loggingOutInterceptor = new LoggingOutInterceptor();
            outInterceptorList.add(loggingOutInterceptor);
        }
    }

    private static void addJsonpInterceptor() {
        if (RestConfig.isJsonp()) {
            JsonpInInterceptor jsonpInInterceptor = new JsonpInInterceptor();
            String jsonpFunction = RestConfig.getJsonpFunction();
            jsonpInInterceptor.setCallbackParam(jsonpFunction);
            inInterceptorList.add(jsonpInInterceptor);
            JsonpPreStreamInterceptor jsonpPreStreamInterceptor = new JsonpPreStreamInterceptor();
            outInterceptorList.add(jsonpPreStreamInterceptor);
            JsonpPostStreamInterceptor jsonpPostStreamInterceptor = new JsonpPostStreamInterceptor();
            outInterceptorList.add(jsonpPostStreamInterceptor);
        }
    }

    private static void addCorsProvider() {
        if (RestConfig.isCors()) {
            CrossOriginResourceSharingFilter corsProvider = new CrossOriginResourceSharingFilter();
            String corsOrigin = RestConfig.getCorsOrigin();
            corsProvider.setAllowOrigins(Arrays.asList(StringUtil.splitString(corsOrigin, ",")));
            providerList.add(corsProvider);
        }
    }

    public static void publishService(String address, Class<?> interfaceClass, Object implementInstance) {
        JAXRSServerFactoryBean factory = new JAXRSServerFactoryBean();
        factory.setAddress(address);
        factory.setResourceClasses(implementInstance.getClass());
        factory.setResourceProvider(new SingletonResourceProvider(implementInstance));
        factory.setProviders(providerList);
        factory.setInInterceptors(inInterceptorList);
        factory.setOutInterceptors(outInterceptorList);
        factory.create();
        logger.debug("Publish REST Service: " + interfaceClass.getName());
    }

    @Override
    public void destroy() {
    }
}
