package org.smart4j.plugin.hessian;

import com.caucho.hessian.server.HessianServlet;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import org.smart4j.framework.core.ClassHelper;
import org.smart4j.framework.ioc.BeanHelper;
import org.smart4j.framework.ioc.IocHelper;
import org.smart4j.framework.util.CollectionUtil;
import org.smart4j.framework.util.StringUtil;
import org.smart4j.framework.util.WebUtil;

/**
 * 用于处理 Hessian 请求
 *
 * @since 1.0
 * @author huangyong
 */
@WebServlet(urlPatterns = HessianConstant.URL_PREFIX + "/*", loadOnStartup = 0)
public class HessianDispatcherServlet extends HessianServlet {

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);

        // 遍历所有标注了 Hessian 注解的接口
        List<Class<?>> interfaceClassList = ClassHelper.getClassListByAnnotation(Hessian.class);
        if (CollectionUtil.isNotEmpty(interfaceClassList)) {
            // 遍历所有 Hessian 接口
            for (Class<?> interfaceClass : interfaceClassList) {
                // 获取 Hessian 地址
                String address = getAddress(interfaceClass);
                // 获取 Hessian 实现类（找到唯一的实现类）
                Class<?> implementClass = IocHelper.findImplementClass(interfaceClass);
                // 获取实现类的实例
                Object implementInstance = BeanHelper.getBean(implementClass);
                // 发布 Hessian 服务
                HessianPlugin.publishService(address, interfaceClass, implementInstance, config);
            }
        }
    }

    private String getAddress(Class<?> interfaceClass) {
        String address;
        // 若 Hessian 注解的 value 属性不为空，则获取当前值，否则获取简单类名
        String value = interfaceClass.getAnnotation(Hessian.class).value();
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
        address = HessianConstant.URL_PREFIX + address;
        return address;
    }

    @Override
    public void service(ServletRequest request, ServletResponse response) throws IOException, ServletException {
        // 获取请求 URL
        HttpServletRequest req = (HttpServletRequest) request;
        String url = WebUtil.getRequestPath(req);
        // 根据 URL 获取 HessianServlet
        HessianServlet hessianServlet = HessianPlugin.getHessianServlet(url);
        if (hessianServlet != null) {
            // 执行 Servlet
            hessianServlet.service(request, response);
        }
    }
}
