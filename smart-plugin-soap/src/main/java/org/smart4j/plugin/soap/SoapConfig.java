package org.smart4j.plugin.soap;

import org.smart4j.framework.core.ConfigHelper;

/**
 * 用于获取 SOAP 插件相关配置项
 *
 * @since 1.1
 * @author huangyong
 */
public class SoapConfig {

    public static boolean isLog() {
        return ConfigHelper.getBoolean(SoapConstant.SOAP_LOG);
    }
}
