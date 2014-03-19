package com.isea.tools.smart;

import com.isea.tools.args.ArgsUtil;
import com.isea.tools.args.MethodParameter;
import com.isea.tools.args.annotation.ValueConstants;
import com.smart.framework.annotation.Request;
import com.smart.framework.bean.Multipart;
import com.smart.framework.bean.Multiparts;
import com.smart.framework.helper.UploadHelper;
import com.smart.framework.util.ArrayUtil;
import com.smart.framework.util.StringUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Description: SmartArgsUtil
 * Author: liuzh
 * Update: liuzh(2014-03-19 10:57)
 */
public class SmartArgsUtil extends ArgsUtil{
    private Multiparts multiparts;

    public final Object[] resolveHandlerArguments(HttpServletRequest request, HttpServletResponse response, Method method) throws Exception {
        //获取mappingUrl
        String requestPath = null;
        // 判断当前 Action 方法是否带有 @Request 注解
        if (method.isAnnotationPresent(Request.class)) {
            // 获取 @Requet 注解中的 URL 字符串
            String[] urlArray = StringUtil.splitString(method.getAnnotation(Request.class).value(), ":");
            if (ArrayUtil.isNotEmpty(urlArray)) {
                // 获取请求路径
                requestPath = urlArray[1];
            }
        } else if (method.isAnnotationPresent(Request.Get.class)) {
            requestPath = method.getAnnotation(Request.Get.class).value();
        } else if (method.isAnnotationPresent(Request.Post.class)) {
            requestPath = method.getAnnotation(Request.Post.class).value();
        } else if (method.isAnnotationPresent(Request.Put.class)) {
            requestPath = method.getAnnotation(Request.Put.class).value();
        } else if (method.isAnnotationPresent(Request.Delete.class)) {
            requestPath = method.getAnnotation(Request.Delete.class).value();
        }

        // 判断 Request Path 中是否带有占位符,如果有，则该请求支持@PathVarible注解，否则不支持
        if (!requestPath.matches(".+\\{\\w+\\}.*")) {
            requestPath = null;
        }

        //处理上传情况
        if(UploadHelper.isMultipart(request)){
            List<Object> paramList = UploadHelper.createMultipartParamList(request);
            for (Object obj : paramList) {
                if(Map.class.isAssignableFrom(obj.getClass())){
                    //因为smart中的List里面只有一个map,所以这里只赋值一次.
                    if(multiParts == null) {
                        multiParts = new HashMap<String, List<Object>>();
                        for (Iterator<Map.Entry> iterator = ((Map) obj).entrySet().iterator(); iterator.hasNext(); ) {
                            Map.Entry entry = iterator.next();
                            String fname = entry.getKey().toString();
                            if(multiParts.get(fname)==null){
                                multiParts.put(fname,new ArrayList<Object>());
                            }
                            multiParts.get(fname).add(entry.getValue());
                        }
                    }
                }
                if(Multiparts.class.isInstance(obj)){
                    multiparts = (Multiparts)obj;
                }
            }
        }

        return resolveHandlerArguments(request, response, method,requestPath);
    }

    @Override
    protected Object resolveStandardArgument(HttpServletRequest request, HttpServletResponse response, MethodParameter methodParam) throws Exception {
        Object object = super.resolveStandardArgument(request, response, methodParam);
        if (object != ValueConstants.UNRESOLVED) {
            return object;
        }
        //支持参数直接写Multipart或Multiparts
        if (multiparts != null) {
            Class<?> parameterType = methodParam.getParameterType();

            if (Multipart.class.isAssignableFrom(parameterType)) {
                return multiparts.getOne();
            } else if (Multiparts.class.isAssignableFrom(parameterType)) {
                return multiparts;
            }
        }

        return object;
    }
}