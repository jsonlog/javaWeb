package org.smart4j.framwork;

import org.smart4j.framwork.annotation.Controller;
import org.smart4j.framwork.helper.BeanHelper;
import org.smart4j.framwork.helper.ClassHelper;
import org.smart4j.framwork.helper.ControllerHelper;
import org.smart4j.framwork.helper.IoCHelper;
import org.smart4j.framwork.util.ClassUtil;

public class HelperLoader {
    /**
     * ClassHelper        保存所有的加载类
     * BeanHelper         Bean容器 单例模式
     * IoCHelper          实现IoC功能
     * ControllerHelper   处理URL与Controller的关系
     */
    public static void init() {
        Class<?>[] classList = {
                ClassHelper.class,
                BeanHelper.class,
                IoCHelper.class,
                ControllerHelper.class
        };
        for(Class<?> cls : classList) {
            ClassUtil.loadClass(cls.getName(),true);
        }
    }
}
