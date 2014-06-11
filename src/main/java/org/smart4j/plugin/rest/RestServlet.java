package org.smart4j.plugin.rest;

import java.util.List;
import javax.servlet.ServletConfig;
import javax.servlet.annotation.WebServlet;
import org.apache.cxf.Bus;
import org.apache.cxf.BusFactory;
import org.apache.cxf.transport.servlet.CXFNonSpringServlet;
import org.smart4j.framework.core.ClassHelper;
import org.smart4j.framework.util.CollectionUtil;
import org.smart4j.framework.util.StringUtil;

/**
 * 发布 REST 服务
 *
 * @since 1.0
 * @author huangyong
 */
@WebServlet(urlPatterns = RestConstant.SERVLET_URL, loadOnStartup = 0)
public class RestServlet extends CXFNonSpringServlet {

    @Override
    protected void loadBus(ServletConfig sc) {
        // 初始化 CXF 总线
        super.loadBus(sc);
        Bus bus = getBus();
        BusFactory.setDefaultBus(bus);
        // 发布 REST 服务
        publishRestService();
    }

    private void publishRestService() {
        // 遍历所有标注了 Rest 注解的类
        List<Class<?>> resourceClassList = ClassHelper.getClassListByAnnotation(Rest.class);
        if (CollectionUtil.isNotEmpty(resourceClassList)) {
            for (Class<?> resourceClass : resourceClassList) {
                // 获取 REST 地址
                String address = getAddress(resourceClass);
                // 发布 REST 服务
                RestHelper.publishService(address, resourceClass);
            }
        }
    }

    private String getAddress(Class<?> resourceClass) {
        String address;
        // 若 Rest 注解的 value 属性不为空，则获取当前值，否则获取类名
        String value = resourceClass.getAnnotation(Rest.class).value();
        if (StringUtil.isNotEmpty(value)) {
            address = value;
        } else {
            address = resourceClass.getSimpleName();
        }
        // 确保最前面只有一个 /
        if (!address.startsWith("/")) {
            address = "/" + address;
        }
        address = address.replaceAll("\\/+", "/");
        return address;
    }
}
