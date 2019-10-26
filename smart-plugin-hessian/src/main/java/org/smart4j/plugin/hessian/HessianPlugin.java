package org.smart4j.plugin.hessian;

import com.caucho.hessian.server.HessianServlet;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smart4j.framework.plugin.Plugin;

/**
 * 用于实现 Hessian 插件接口
 *
 * @since 1.1
 * @author huangyong
 */
public class HessianPlugin implements Plugin {

    private static final Logger logger = LoggerFactory.getLogger(HessianPlugin.class);

    /**
     * 用于管理 URL 与 HessianServlet 之间的映射关系
     */
    private final static Map<String, HessianServlet> hessianServletMap = new HashMap<String, HessianServlet>();

    @Override
    public void init() {
    }

    public static void publishService(String address, Class<?> interfaceClass, Object implementInstance, ServletConfig config) throws ServletException {
        // 创建 HessianServlet
        HessianServlet hessianServlet = new HessianServlet();
        hessianServlet.setHomeAPI(interfaceClass); // 设置接口
        hessianServlet.setHome(implementInstance); // 设置实现类实例
        hessianServlet.init(config); // 初始化 Servlet
        // 将 URL 与 HessianServlet 的映射关系放入 HessianServlet Map 中
        hessianServletMap.put(address, hessianServlet);
        logger.debug("Publish Hessian Service: " + interfaceClass.getName());
    }

    public static HessianServlet getHessianServlet(String url) {
        return hessianServletMap.get(url);
    }

    @Override
    public void destroy() {
    }
}
