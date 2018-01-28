package org.smart4j.framwork.helper;


import org.smart4j.framwork.annotation.Inject;
import org.smart4j.framwork.util.ArrayUtil;
import org.smart4j.framwork.util.CollectionUtil;
import org.smart4j.framwork.util.ReflectionUtil;

import java.lang.reflect.Field;
import java.util.Map;

public class IoCHelper {

    static {
        Map<Class<?>,Object> beanMap = BeanHelper.getBeanMap();
        if(CollectionUtil.isNotEmpty(beanMap)) {
            // 遍历Map
            for(Map.Entry<Class<?>,Object> beanEntry : beanMap.entrySet()) {
                Class<?> beanClass = beanEntry.getKey();
                Object beanInstance = beanEntry.getValue();

                // 获取所有成员变量
                Field[] beanFields = beanClass.getDeclaredFields();
                if(ArrayUtil.isNotEmpty(beanFields)) {
                    for(Field beanField:beanFields) {
                        if(beanField.isAnnotationPresent(Inject.class)) {
                            // 需要注入
                            Class<?> beanFieldClass = beanField.getType();
                            Object beanFieldInstance = beanMap.get(beanFieldClass);
                            if(beanFieldInstance!=null) {
                                // 反射初始化BeanField值

                                // 将beanInstance对象中的beanField变量设置为beanFieldInstace
                                ReflectionUtil.setField(beanInstance,beanField,beanFieldInstance);
                            }
                        }
                    }
                }
            }
        }
    }
}
