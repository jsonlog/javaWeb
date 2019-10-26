package org.smart4j.plugin.soap;

import java.util.List;
import javax.servlet.ServletConfig;
import javax.servlet.annotation.WebServlet;
import org.apache.cxf.Bus;
import org.apache.cxf.BusFactory;
import org.apache.cxf.transport.servlet.CXFNonSpringServlet;
import org.smart4j.framework.core.ClassHelper;
import org.smart4j.framework.ioc.BeanHelper;
import org.smart4j.framework.ioc.IocHelper;
import org.smart4j.framework.util.CollectionUtil;
import org.smart4j.framework.util.StringUtil;

/**
 * 用于发布 SOAP 服务
 *
 * @since 1.0
 * @author huangyong
 */
@WebServlet(urlPatterns = SoapConstant.SERVLET_URL, loadOnStartup = 0)
public class SoapServlet extends CXFNonSpringServlet {

    @Override
    protected void loadBus(ServletConfig sc) {
        // 初始化 CXF 总线
        initCxfBus(sc);
        // 发布 SOAP 服务
        publishSoapService();
    }

    private void initCxfBus(ServletConfig sc) {
        super.loadBus(sc);
        Bus bus = getBus();
        BusFactory.setDefaultBus(bus);
    }

    private void publishSoapService() {
        // 遍历所有标注了 Soap 注解的接口
        List<Class<?>> interfaceClassList = ClassHelper.getClassListByAnnotation(Soap.class);
        if (CollectionUtil.isNotEmpty(interfaceClassList)) {
            for (Class<?> interfaceClass : interfaceClassList) {
                // 获取 SOAP 地址
                String address = getAddress(interfaceClass);
                // 获取 SOAP 实现类（找到唯一的实现类）
                Class<?> implementClass = IocHelper.findImplementClass(interfaceClass);
                // 获取实现类的实例
                Object implementInstance = BeanHelper.getBean(implementClass);
                // 发布 SOAP 服务
                SoapPlugin.publishService(address, interfaceClass, implementInstance);
            }
        }
    }

    private String getAddress(Class<?> interfaceClass) {
        String address;
        // 若 Soap 注解的 value 属性不为空，则获取当前值，否则获取简单类名
        String value = interfaceClass.getAnnotation(Soap.class).value();
        if (StringUtil.isNotEmpty(value)) {
            address = value;
        } else {
            address = interfaceClass.getSimpleName();
        }
        // 确保最前面只有一个 /
        if (!address.startsWith("/")) {
            address = "/" + address;
        }
        address = address.replaceAll("\\/+", "/");
        return address;
    }
}
