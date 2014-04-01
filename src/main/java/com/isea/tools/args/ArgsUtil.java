package com.isea.tools.args;

import com.isea.tools.args.annotation.Param;
import com.isea.tools.args.annotation.Path;
import com.isea.tools.args.annotation.ValueConstants;
import com.smart.framework.annotation.Request;
import com.smart.framework.bean.Multipart;
import com.smart.framework.bean.Multiparts;
import com.smart.framework.helper.UploadHelper;
import com.smart.framework.util.ArrayUtil;
import com.smart.framework.util.StringUtil;
import org.apache.commons.beanutils.BeanUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URL;
import java.security.Principal;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by liuzh on 14-3-11.
 */
public class ArgsUtil {
    protected Map<String, Object> requestMap;
    protected Map<String, Object> pathValues;
    private Multiparts multiparts;

    /**
     * 获取全部参数 - 该方法适用于使用了<code>RequestMapping</code>注解的方法
     *
     * @param request
     * @param response
     * @param method
     * @return
     * @throws Exception
     */
    public Object[] resolveHandlerArguments(HttpServletRequest request, HttpServletResponse response, Method method) throws Exception {
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

        return resolveHandlerArguments(request, response, method, requestPath);
    }

    /**
     * 获取全部参数
     *
     * @param request
     * @param response
     * @param method
     * @param mappingUrl 使用@PathVariable注解时，需要该参数，该参数值形如：/login/{username}/{password}
     * @return
     * @throws Exception
     */
    public Object[] resolveHandlerArguments(HttpServletRequest request, HttpServletResponse response, Method method, String mappingUrl) throws Exception {
        Class[] paramTypes = method.getParameterTypes();
        Object[] args = new Object[paramTypes.length];

        //处理上传和普通request的情况
        if (UploadHelper.isMultipart(request)) {
            resolveMultiparts(request);
        } else {
            resolveRequestMap(request);
        }

        //逐个处理参数
        for (int i = 0; i < args.length; i++) {
            MethodParameter methodParam = new MethodParameter(method, i);

            String paramName = null;
            Class<?> paramType = methodParam.getParameterType();
            String pathVarName = null;
            String attrName = null;
            boolean required = false;
            String defaultValue = null;
            int annotationsFound = 0;
            Annotation[] paramAnns = methodParam.getParameterAnnotations();

            for (Annotation paramAnn : paramAnns) {
                if (Param.class.isInstance(paramAnn)) {
                    Param requestParam = (Param) paramAnn;
                    paramName = requestParam.value();
                    required = requestParam.required();
                    defaultValue = parseDefaultValueAttribute(requestParam.defaultValue());
                    annotationsFound++;
                } else if (Path.class.isInstance(paramAnn)) {
                    Path pathVar = (Path) paramAnn;
                    pathVarName = pathVar.value();
                    annotationsFound++;
                }
            }

            if (annotationsFound > 1) {
                throw new IllegalStateException("参数注解是可选的 - " + "不能对同一个参数使用多个注解: " + method);
            }

            if (annotationsFound == 0) {
                //处理标准Http类型
                Object argValue = resolveStandardArgument(request, response, methodParam);
                if (argValue != ValueConstants.UNRESOLVED) {
                    args[i] = argValue;
                } else if (defaultValue != null) {
                    args[i] = defaultValue;
                } else {
                    //尝试使用参数名来构建对象
                    if (isSimpleProperty(paramType)) {
                        //简单对象
                        paramName = "";
                    } else {
                        //复杂对象
                        attrName = "";
                    }
                }

            }

            if (paramName != null) {
                args[i] = resolveRequestParam(paramName, required, defaultValue, methodParam, request);
            } else if (pathVarName != null) {
                args[i] = resolvePathVariable(pathVarName, mappingUrl, paramType, methodParam, request);
            } else if (attrName != null) {
                args[i] = resolveComplexParam(paramType);
            }
        }

        return args;
    }


    /**
     * 判断类是否为简单类
     *
     * @param clazz
     * @return
     */
    public static boolean isSimpleProperty(Class<?> clazz) {
        return isSimpleValueType(clazz) || (clazz.isArray() && isSimpleValueType(clazz.getComponentType()));
    }

    /**
     * 判断类是否为简单类
     *
     * @param clazz
     * @return
     */
    public static boolean isSimpleValueType(Class<?> clazz) {
        return clazz.isPrimitive() || clazz.isEnum() ||
                CharSequence.class.isAssignableFrom(clazz) ||
                Number.class.isAssignableFrom(clazz) ||
                Date.class.isAssignableFrom(clazz) ||
                clazz.equals(URI.class) || clazz.equals(URL.class) ||
                clazz.equals(Locale.class) || clazz.equals(Class.class);
    }

    /**
     * 处理上传的情况
     * @param request
     * @throws Exception
     */
    protected void resolveMultiparts(HttpServletRequest request) throws Exception {
        List<Object> paramList = UploadHelper.createMultipartParamList(request);
        if(requestMap == null) {
            requestMap = new LinkedHashMap<String, Object>();
        }
        for (Object obj : paramList) {
            if(obj == null){
                continue;
            }
            if(Map.class.isAssignableFrom(obj.getClass())){
                for (Iterator<Map.Entry> iterator = ((Map) obj).entrySet().iterator(); iterator.hasNext(); ) {
                    Map.Entry entry = iterator.next();
                    String fieldName = entry.getKey().toString();
                    if(requestMap.get(fieldName)==null){
                        requestMap.put(fieldName,entry.getValue());
                    }
                    else {
                        //存在多个值的情况，增加一个fileName[]的Key来存储
                        String fieldArray = fieldName + "[]";
                        if (requestMap.get(fieldArray) == null) {
                            requestMap.put(fieldArray,new ArrayList<Object>());
                        }
                        ((List)requestMap.get(fieldArray)).add(entry.getValue());
                    }
                }
            }
            if(Multiparts.class.isInstance(obj)){
                multiparts = (Multiparts)obj;
                for (Multipart multipart : multiparts.getAll()) {
                    String fieldName = multipart.getFieldName();
                    if(requestMap.get(fieldName)==null){
                        requestMap.put(fieldName,multipart);
                    }
                    else {
                        //存在多个值的情况，增加一个fileName[]的Key来存储
                        String fieldArray = fieldName + "[]";
                        if (requestMap.get(fieldArray) == null) {
                            requestMap.put(fieldArray,new ArrayList<Object>());
                        }
                        ((List)requestMap.get(fieldArray)).add(multipart);
                    }
                }
            }
        }
    }

    /**
     * 处理普通的requestMap
     * @param request
     * @throws Exception
     */
    protected void resolveRequestMap(HttpServletRequest request) throws Exception {
        Map<String, String[]> parameterMap = request.getParameterMap();
        if (requestMap == null) {
            requestMap = new LinkedHashMap<String, Object>(parameterMap.size());
        }
        for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
            if (entry.getValue().length > 0) {
                requestMap.put(entry.getKey(), entry.getValue()[0]);
            }
            //如果是数组，这里再保存一个数组的形式，需要用name[]来获取
            if (entry.getValue().length > 1) {
                requestMap.put(entry.getKey()+"[]", entry.getValue());
            }
        }
    }

    /**
     * 处理标准类型参数
     * @param request
     * @param response
     * @param methodParam
     * @return
     * @throws Exception
     */
    protected Object resolveStandardArgument(HttpServletRequest request, HttpServletResponse response, MethodParameter methodParam) throws Exception {
        Class<?> parameterType = methodParam.getParameterType();
        if (ServletRequest.class.isAssignableFrom(parameterType)) {
            return request;
        } else if (ServletResponse.class.isAssignableFrom(parameterType)) {
            return response;
        } else if (HttpSession.class.isAssignableFrom(parameterType)) {
            return request.getSession();
        } else if (Principal.class.isAssignableFrom(parameterType)) {
            return request.getUserPrincipal();
        } else if (Locale.class.equals(parameterType)) {
            return request.getLocale();
        } else if (InputStream.class.isAssignableFrom(parameterType)) {
            return request.getInputStream();
        } else if (Reader.class.isAssignableFrom(parameterType)) {
            return request.getReader();
        } else if (OutputStream.class.isAssignableFrom(parameterType)) {
            return response.getOutputStream();
        } else if (Writer.class.isAssignableFrom(parameterType)) {
            return response.getWriter();
        } else if (Multipart.class.isAssignableFrom(parameterType)) {
            if (multiparts != null) {
                return multiparts.getOne();
            }
        } else if (Multiparts.class.isAssignableFrom(parameterType)) {
            if (multiparts != null) {
                return multiparts;
            }
        }
        return ValueConstants.UNRESOLVED;
    }

    /**
     * 处理POJO参数
     * @param paramType
     * @return
     * @throws Exception
     */
    protected Object resolveComplexParam(Class<?> paramType) throws Exception {
        Object object = null;
        try {
            object = paramType.newInstance();
        } catch (Exception e){
            System.out.println(paramType.getName() + "不包含无参数的构造方法..不能自动创建该对象!");
            return null;
        }
        //TODO 这里测试解决多层次对象赋值的问题
        BeanUtils.populate(object, requestMap);
        return object;
    }

    /**
     * 处理Request参数
     * @param paramName
     * @param required
     * @param defaultValue
     * @param methodParam
     * @param request
     * @return
     * @throws Exception
     */
    protected Object resolveRequestParam(String paramName, boolean required, String defaultValue,
                                         MethodParameter methodParam, HttpServletRequest request)
            throws Exception {
        Class<?> paramType = methodParam.getParameterType();
        //当对象是一个Map类型的时候，返回Map类型,这里匹配无注解和@Param无参数值类型为Map的情况
        if (Map.class.isAssignableFrom(paramType) && paramName.length() == 0) {
            return requestMap;
        }
        //当参数值为空时有两种情况，一种是无注解，一种是注解参数为空
        if (paramName.length() == 0) {
            //获取方法中的参数名
            paramName = methodParam.getParameterName();
        }

        Object paramValue = null;

        //TODO 测试上传时，多个字段同名情况下的list参数
        if (Collection.class.isAssignableFrom(paramType)) {
            paramValue = requestMap.get(paramName+"[]");
        }
        //TODO 测试上传时，多个字段同名情况下的数组参数
        else if(paramType.isArray()&&requestMap.get(paramName+"[]")!=null){
            paramValue = ((List)requestMap.get(paramName+"[]")).toArray();
        } else {
            paramValue = requestMap.get(paramName);
        }

        if (paramValue == null) {
            if (defaultValue != null) {
                paramValue = defaultValue;
            } else if (required) {
                raiseMissingParameterException(paramName, paramType);
            }
            paramValue = checkValue(paramName, paramValue, paramType);
        }
        return checkType(paramName, paramValue, paramType);
    }

    /**
     * 处理路径参数
     * @param pathVarName
     * @param mappingUrl
     * @param paramType
     * @param methodParam
     * @param request
     * @return
     * @throws Exception
     */
    protected Object resolvePathVariable(String pathVarName, String mappingUrl, Class paramType, MethodParameter methodParam, HttpServletRequest request)
            throws Exception {
        if (mappingUrl == null || mappingUrl.equals("")) {
            throw new IllegalStateException("缺少mappingUrl值，不能使用@Path");
        }
        if (pathValues == null) {
            String servletPath = request.getServletPath();
            String pathInfo = request.getPathInfo();
            if (pathInfo != null) {
                servletPath += pathInfo;
            }
            // 去掉当前请求路径末尾的“/”
            if (servletPath.endsWith("/")) {
                servletPath = servletPath.substring(0, servletPath.length() - 1);
            }
            pathValues = getPathValueMap(mappingUrl, servletPath);
        }
        if (pathVarName.length() == 0) {
            pathVarName = methodParam.getParameterName();
        }
        Object pathValue = pathValues.get(pathVarName);
        if (pathValue == null) {
            raisePathVarException(pathVarName, paramType);
        }
        //TODO 测试pathValue类型
        return checkType(pathVarName,pathValue,paramType);
    }

    /**
     * 获取路径参数
     * @param requestPath
     * @param requestUrl
     * @return
     * @throws Exception
     */
    public static Map<String, Object> getPathValueMap(final String requestPath, final String requestUrl) throws Exception {
        String path = requestPath;

        String reg = path.replaceAll("\\{\\w+\\}", "((\\\\w|[\\\\u4e00-\\\\u9fa5])+)");

        path = path.replaceAll("\\{", "").replaceAll("\\}", "");

        Pattern pattern = Pattern.compile(reg);
        Matcher name_matcher = pattern.matcher(path);
        Matcher value_matcher = pattern.matcher(requestUrl);
        Map<String, Object> pathValue = new HashMap<String, Object>();
        // 部分匹配find
        // 完整匹配matches
        if (name_matcher.matches() && value_matcher.matches()) {
            if (name_matcher.groupCount() == value_matcher.groupCount()) {
                for (int i = 1; i <= name_matcher.groupCount(); i++) {
                    pathValue.put(name_matcher.group(i), value_matcher.group(i));
                }
            } else {
                throw new RuntimeException("错误:参数个数不一致");
            }
        }
        return pathValue;
    }

    /**
     * 检查类型
     * @param name
     * @param value
     * @param paramType
     * @return
     */
    protected Object checkType(String name, Object value, Class paramType) {
        //TODO 处理URL,URI,Class<?>,Collection,Array,基本类型
        if (!paramType.isInstance(value)) {
            //对基本类型进行转换
            if(isSimpleProperty(paramType)){
                Object newValue = TypeConverter.convertToBasic(value, paramType);
                if(newValue != null){
                    return newValue;
                }
            }
            throw new IllegalStateException("类型:" + paramType + " 参数 '" + name + "' 值的类型:" + value.getClass() + "和当前要求的类型不一致");

        }
        return value;
    }

    /**
     * 对于required=false的参数，如果没有值，就返回一个默认值，false或者null，8种基本类型中的数字类型不能使用（因为无法消除歧义）
     *
     * @param name
     * @param value
     * @param paramType
     * @return
     */
    protected Object checkValue(String name, Object value, Class paramType) {
        if (value == null) {
            if (boolean.class.equals(paramType)) {
                return Boolean.FALSE;
            } else if (paramType.isPrimitive()) {
                throw new IllegalStateException("类型:" + paramType + " 参数 '" + name
                        + "' 是一个原始类型，程序不能将它转换为NULL，建议使用基本类型的包装类");
            }
        }
        return value;
    }

    protected String parseDefaultValueAttribute(String value) {
        return (ValueConstants.DEFAULT_NONE.equals(value) ? null : value);
    }

    protected void raiseMissingParameterException(String paramName, Class paramType) throws Exception {
        throw new IllegalStateException("缺少参数: '" + paramName + "' 类型为 [" + paramType.getName() + "]");
    }

    protected void raisePathVarException(String parthVarName, Class paramType) throws Exception {
        throw new IllegalStateException("缺少pathValue属性: '" + parthVarName + "' 类型为 [" + paramType.getName() + "]");
    }
}
