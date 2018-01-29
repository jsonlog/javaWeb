package org.smart4j.framwork.helper;

import org.smart4j.framwork.annotation.Action;
import org.smart4j.framwork.bean.Handler;
import org.smart4j.framwork.bean.Request;
import org.smart4j.framwork.util.ArrayUtil;
import org.smart4j.framwork.util.CollectionUtil;

import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ControllerHelper {

    private static final Map<Request,Handler> ACTION_MAP = new HashMap<Request, Handler>();

    static {
        Set<Class<?>> controllerClassSet = ClassHelper.getControllerClassSet();
        if(CollectionUtil.isNotEmpty(controllerClassSet)) {
            // 遍历Controller类的方法
            for(Class<?> controllerClass:controllerClassSet) {
                Method[] methods = controllerClass.getDeclaredMethods();
                if(ArrayUtil.isNotEmpty(methods)) {
                    for(Method method:methods) {
                        if(method.isAnnotationPresent(Action.class)){
                            Action action = method.getAnnotation(Action.class);
                            String mapping = action.value();
                            // 验证URL
                            if(mapping.matches("\\w+:/\\w*")) {
                                String[] array = mapping.split(":");
                                if(ArrayUtil.isNotEmpty(array) && array.length == 2) {
                                    String requestMethod = array[0];
                                    String requestPath = array[1];
                                    Request request = new Request(requestMethod,requestPath);
                                    Handler handler = new Handler(controllerClass,method);
                                    ACTION_MAP.put(request,handler);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public static Handler getHandler(String requestMethod,String requestPath) {
        Request request = new Request(requestMethod,requestPath);
        return ACTION_MAP.get(request);
    }
}
