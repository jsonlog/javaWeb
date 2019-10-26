package org.smart4j.plugin.hessian;

import com.caucho.hessian.client.HessianProxyFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 用于提供 Hessian 插件相关工具方法
 *
 * @since 1.0
 * @author huangyong
 */
@Deprecated
public class HessianHelper {

    private static final Logger logger = LoggerFactory.getLogger(HessianHelper.class);

    /**
     * 创建 Hessian 客户端
     */
    @Deprecated
    @SuppressWarnings("unchecked")
    public static <T> T createClient(String hessianUrl) {
        T client = null;
        try {
            HessianProxyFactory factory = new HessianProxyFactory();
            client = (T) factory.create(hessianUrl);
        } catch (Exception e) {
            logger.error("创建 Hessian 客户端出错！", e);
        }
        return client;
    }
}
