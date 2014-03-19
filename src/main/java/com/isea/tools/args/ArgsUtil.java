package com.isea.tools.args;

import com.isea.tools.args.annotation.*;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.activation.UnsupportedDataTypeException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
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
    private UploadFileItem uploadFileItem;
    private Map<String, List<Object>> multiParts;
    private Map<String,Object> pathValues;
    /**
     * 建议使用<code>ArgsUtil(UploadFileItem uploadFileItem, String mappingUrl)</code>
     */
    @Deprecated
    public ArgsUtil() {
        this(new DefaultUploadFileItem());
    }

    /**
     * 建议使用该方法，需要自己集成<code>UploadFileItem</code>接口
     *
     * @param uploadFileItem   上传文件的处理方法，实现该接口
     */
    public ArgsUtil(UploadFileItem uploadFileItem) {
        this.uploadFileItem = uploadFileItem;
    }

    /**
     * 获取全部参数
     * @param request
     * @param response
     * @param method
     * @param mappingUrl 使用@PathVariable注解时，需要该参数，该参数值形如：/login/{username}/{password}
     * @return
     * @throws Exception
     */
    public final Object[] resolveHandlerArguments(HttpServletRequest request, HttpServletResponse response, Method method, String mappingUrl) throws Exception {
        Class[] paramTypes = method.getParameterTypes();
        Object[] args = new Object[paramTypes.length];

        if (ServletFileUpload.isMultipartContent(request)) {
            if (multiParts == null) {
                multiParts = uploadFileItem.getFieldObjects(request);
            }
        }

        for (int i = 0; i < args.length; i++) {
            MethodParameter methodParam = new MethodParameter(method, i);

            String paramName = null;
            Class<?> paramType = methodParam.getParameterType();
            String sessionName = null;
            String headerName = null;
            String cookieName = null;
            String pathVarName = null;
            String attrName = null;
            boolean required = false;
            String defaultValue = null;
            int annotationsFound = 0;
            Annotation[] paramAnns = methodParam.getParameterAnnotations();

            for (Annotation paramAnn : paramAnns) {
                if (RequestParam.class.isInstance(paramAnn)) {
                    RequestParam requestParam = (RequestParam) paramAnn;
                    paramName = requestParam.value();
                    required = requestParam.required();
                    defaultValue = parseDefaultValueAttribute(requestParam.defaultValue());
                    annotationsFound++;
                } else if (SessionAttributes.class.isInstance(paramAnn)) {
                    SessionAttributes sessionAttributes = (SessionAttributes) paramAnn;
                    sessionName = sessionAttributes.value();
                    required = sessionAttributes.required();
                    defaultValue = parseDefaultValueAttribute(sessionAttributes.defaultValue());
                    annotationsFound++;
                }else if (RequestHeader.class.isInstance(paramAnn)) {
                    RequestHeader requestHeader = (RequestHeader) paramAnn;
                    headerName = requestHeader.value();
                    required = requestHeader.required();
                    defaultValue = parseDefaultValueAttribute(requestHeader.defaultValue());
                    annotationsFound++;
                } else if (CookieValue.class.isInstance(paramAnn)) {
                    CookieValue cookieValue = (CookieValue) paramAnn;
                    cookieName = cookieValue.value();
                    required = cookieValue.required();
                    defaultValue = parseDefaultValueAttribute(cookieValue.defaultValue());
                    annotationsFound++;
                } else if (PathVariable.class.isInstance(paramAnn)) {
                    PathVariable pathVar = (PathVariable) paramAnn;
                    pathVarName = pathVar.value();
                    annotationsFound++;
                }
                //对象级别的，需要.赋值
                else if (ModelAttribute.class.isInstance(paramAnn)) {
                    ModelAttribute attr = (ModelAttribute) paramAnn;
                    attrName = attr.value();
                    annotationsFound++;
                }
            }

            if (annotationsFound > 1) {
                throw new IllegalStateException("参数注解是可选的 - " + "不要对同一个参数使用多个注解: " + method);
            }

            if (annotationsFound == 0) {
                //处理POJO
                Object argValue = resolveStandardArgument(request, response, methodParam);
                if (argValue != ValueConstants.UNRESOLVED) {
                    args[i] = argValue;
                } else if (defaultValue != null) {
                    args[i] = defaultValue;
                } else {
                    //如果是简单类，就从request中根据参数名字取值
                    if (isSimpleProperty(paramType)) {
                        paramName = "";
                    } else {
                        //复杂对象
                        attrName = "";
                    }
                }

            }

            if (paramName != null) {
                args[i] = resolveRequestParam(paramName, required, defaultValue, methodParam, request);
            } else if (sessionName != null) {
                args[i] = resolveSessionAttributes(sessionName, required, defaultValue, methodParam, request);
            } else if (headerName != null) {
                args[i] = resolveRequestHeader(headerName, required, defaultValue, methodParam, request);
            } else if (cookieName != null) {
                args[i] = resolveCookieValue(cookieName, paramType, required, request);
            } else if (pathVarName != null) {
                args[i] = resolvePathVariable(pathVarName, mappingUrl, paramType, methodParam, request);
            } else if (attrName != null) {
                args[i] = resolveComplexParam(paramType, request);
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
     * 处理标准类型参数
     *
     * @param request
     * @return
     * @throws Exception
     */
    private Object resolveStandardArgument(HttpServletRequest request, HttpServletResponse response, MethodParameter methodParam) throws Exception {
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
        }
        //s对一些特殊的类进行处理...需要外部jar支持
        try {
            Class<?> clazz = Class.forName("org.json.JSONWriter");
            if (clazz.isAssignableFrom(parameterType)) {
                Constructor constructor = clazz.getConstructor(Writer.class);
                return constructor.newInstance(response.getWriter());
            }
        } catch (Exception e) {
            //不做处理
        }
        return ValueConstants.UNRESOLVED;
    }

    private Object resolveComplexParam(Class<?> paramType, HttpServletRequest request) throws Exception {
        Object object = paramType.newInstance();
        if(multiParts!=null){
            BeanUtils.populate(object, multiParts);
        }
        HashMap map = new HashMap();
        Enumeration names = request.getParameterNames();
        while (names.hasMoreElements()) {
            String name = (String) names.nextElement();
            map.put(name, request.getParameterValues(name));
        }
        BeanUtils.populate(object, map);
        return object;
    }

    private Object resolveRequestParam(String paramName, boolean required, String defaultValue,
                                       MethodParameter methodParam, HttpServletRequest request)
            throws Exception {
        Class<?> paramType = methodParam.getParameterType();
        if (Map.class.isAssignableFrom(paramType) && paramName.length() == 0) {
            return resolveRequestParamMap(request);
        }
        if (paramName.length() == 0) {
            paramName = methodParam.getParameterName();
        }

        Object paramValue = null;

        if (multiParts != null) {
            if (Collection.class.isAssignableFrom(paramType)) {
                paramValue = multiParts.get(paramName);
            } else {
                paramValue = multiParts.get(paramName)!=null&&multiParts.get(paramName).size() > 0 ? multiParts.get(paramName).get(0) : null;
            }
            if (paramValue != null && !paramType.isAssignableFrom(paramValue.getClass())) {
                paramValue = null;
            }
        }

        if (paramValue == null) {
            String[] paramValues = request.getParameterValues(paramName);
            if (paramValues != null) {
                paramValue = (paramValues.length == 1 ? paramValues[0] : paramValues);
            }
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

    private Object resolveSessionAttributes(String sessionName, boolean required, String defaultValue,
                                       MethodParameter methodParam, HttpServletRequest request)
            throws Exception {
        HttpSession session = request.getSession();
        Class<?> paramType = methodParam.getParameterType();
        if (Map.class.isAssignableFrom(paramType) && sessionName.length() == 0) {
            return resolveSessionMap(request);
        }
        if (sessionName.length() == 0) {
            sessionName = methodParam.getParameterName();
        }

        Object paramValue = session.getAttribute(sessionName);

        if (paramValue == null) {
            if (defaultValue != null) {
                paramValue = defaultValue;
            } else if (required) {
                raiseSessionRequiredException(sessionName, paramType);
            }
            paramValue = checkValue(sessionName, paramValue, paramType);
        }
        return checkType(sessionName, paramValue,paramType);
    }

    private Object resolveRequestHeader(String headerName, boolean required, String defaultValue,
                                        MethodParameter methodParam, HttpServletRequest request)
            throws Exception {
        Class<?> paramType = methodParam.getParameterType();
        if (Map.class.isAssignableFrom(paramType)) {
            return resolveRequestHeaderMap(request);
        }
        if (headerName.length() == 0) {
            headerName = methodParam.getParameterName();
            if (headerName == null) {
                throw new IllegalStateException(
                        "没有指定参数名称为参数的类型 [" + methodParam.getParameterType().getName() +
                                "], 而且没有在类中找到该参数的名字.");
            }
        }
        Object headerValue = null;
        String[] headerValues = getHeaderValues(request, headerName);
        if (headerValues != null) {
            headerValue = (headerValues.length == 1 ? headerValues[0] : headerValues);
        }
        if (headerValue == null) {
            if (defaultValue != null) {
                headerValue = defaultValue;
            } else if (required) {
                raiseMissingHeaderException(headerName, paramType);
            }
            headerValue = checkValue(headerName, headerValue, paramType);
        }
        return checkType(headerName, headerValue, paramType);
    }

    private String[] getHeaderValues(HttpServletRequest request, String headerName) {
        List<String> list = Collections.list(request.getHeaders(headerName));
        String[] headerValues = list.toArray(new String[list.size()]);
        if (headerValues.length == 0) {
            return null;
        } else {
            return headerValues;
        }
    }

    public Cookie getCookie(HttpServletRequest request, String key) {
        Cookie[] cookieArray = request.getCookies();
        if (cookieArray != null & cookieArray.length > 0) {
            for (Cookie cookie : cookieArray) {
                if (key.equals(cookie.getName())) {
                    return cookie;
                }
            }
        }
        return null;
    }

    protected Object resolveCookieValue(String cookieName, Class paramType, boolean required, HttpServletRequest request)
            throws Exception {
        Cookie cookieValue = getCookie(request, cookieName);
        if (Cookie.class.isAssignableFrom(paramType)) {
            return cookieValue;
        } else if (cookieValue != null) {
            return DecodeUtil.decodeInternal(request, cookieValue.getValue());
        } else if (required) {
            raiseMissingCookieException(cookieName, paramType);
        }
        return null;
    }

    protected Object resolvePathVariable(String pathVarName, String mappingUrl, Class paramType, MethodParameter methodParam, HttpServletRequest request)
            throws Exception {
        if(mappingUrl==null||mappingUrl.equals("")){
            throw new IllegalStateException("缺少mappingUrl值，不能使用@PathVariable");
        }
        if(pathValues==null){
            String servletPath = request.getServletPath();
            String pathInfo = request.getPathInfo();
            if(pathInfo!=null){
                servletPath += pathInfo;
            }
            // 去掉当前请求路径末尾的“/”
            if (servletPath.endsWith("/")) {
                servletPath = servletPath.substring(0, servletPath.length() - 1);
            }
            pathValues = getPathValueMap(mappingUrl, servletPath);
        }
        if(pathVarName.length()==0){
            pathVarName = methodParam.getParameterName();
        }
        Object pathValue = pathValues.get(pathVarName);
        if(pathValue == null){
            raisePathVarException(pathVarName,paramType);
        }
        if (paramType.equals(int.class) || paramType.equals(Integer.class)) {
            pathValue = Integer.parseInt(String.valueOf(pathValue));
        } else if (paramType.equals(long.class) || paramType.equals(Long.class)) {
            pathValue = Long.parseLong(String.valueOf(pathValue));
        } else if (paramType.equals(double.class) || paramType.equals(Double.class)) {
            pathValue = Double.parseDouble(String.valueOf(pathValue));
        } else if (!paramType.equals(String.class)) {
            //pathValue不支持其他类型的参数
            raisePathVarUnsupportedException(pathVarName, paramType);
        }
        return pathValue;
    }

    public static Map<String,Object> getPathValueMap(final String requestPath, final String requestUrl) throws Exception{
        String path = requestPath;

        String reg = path.replaceAll("\\{\\w+\\}","((\\\\w|[\\\\u4e00-\\\\u9fa5])+)");

        path = path.replaceAll("\\{","").replaceAll("\\}", "");

        Pattern pattern = Pattern.compile(reg);
        Matcher name_matcher = pattern.matcher(path);
        Matcher value_matcher = pattern.matcher(requestUrl);
        Map<String,Object> pathValue = new HashMap<String,Object>();
        // 部分匹配find
        // 完整匹配matches
        if(name_matcher.matches()&&value_matcher.matches()){
            if(name_matcher.groupCount() == value_matcher.groupCount()){
                for(int i=1;i<=name_matcher.groupCount();i++){
                    pathValue.put(name_matcher.group(i),value_matcher.group(i));
                }
            }
            else {
                throw new RuntimeException("错误:参数个数不一致");
            }
        }
        return pathValue;
    }

    /**
     * 将header转成Map返回
     * @param request
     * @return
     */
    private Map resolveRequestHeaderMap(HttpServletRequest request) {
        Map<String, String> result = new LinkedHashMap<String, String>();
        for (Enumeration<String> iterator = request.getHeaderNames(); iterator.hasMoreElements(); ) {
            String headerName = iterator.nextElement();
            String headerValue = request.getHeader(headerName);
            result.put(headerName, headerValue);
        }
        return result;
    }

    /**
     * 将request转成Map返回
     * @param request
     * @return
     */
    private Map resolveRequestParamMap(HttpServletRequest request) {
        Map<String, String[]> parameterMap = request.getParameterMap();
        Map<String, String> result = new LinkedHashMap<String, String>(parameterMap.size());
        for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
            if (entry.getValue().length > 0) {
                result.put(entry.getKey(), entry.getValue()[0]);
            }
        }
        return result;
    }

    /**
     * 将session转为map返回
     * @param request
     * @return
     */
    private Map resolveSessionMap(HttpServletRequest request) {
        Map<String, Object> result = new LinkedHashMap<String, Object>();
        HttpSession session = request.getSession();

        for (Enumeration<String> iterator = session.getAttributeNames();iterator.hasMoreElements();) {
            String sessionKey = iterator.nextElement();
            result.put(sessionKey, session.getAttribute(sessionKey));
        }
        return result;
    }

    private Object checkType(String name, Object value, Class paramType) {
        if(!paramType.isInstance(value)){
            throw new IllegalStateException("类型:"+paramType+" 参数 '"+name+"' 值的类型:"+value.getClass()+"和当前要求的类型不一致");
        }
        return value;
    }

    /**
     * 对于required=false的参数，如果没有值，就返回一个默认值，false或者null，8种基本类型中的数字类型不能使用
     * @param name
     * @param value
     * @param paramType
     * @return
     */
    private Object checkValue(String name, Object value, Class paramType) {
        if (value == null) {
            if (boolean.class.equals(paramType)) {
                return Boolean.FALSE;
            } else if (paramType.isPrimitive()) {
                throw new IllegalStateException("类型:"+paramType+" 参数 '"+name
                        +"' 是一个原始类型，程序不能将它转换为NULL，建议使用基本类型的包装类");
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

    protected void raiseMissingHeaderException(String headerName, Class paramType) throws Exception {
        throw new IllegalStateException("缺少header: '" + headerName + "' 类型为 [" + paramType.getName() + "]");
    }

    protected void raiseMissingCookieException(String cookieName, Class paramType) throws Exception {
        throw new IllegalStateException(
                "缺少cookie: '" + cookieName + "' 类型为 [" + paramType.getName() + "]");
    }

    protected void raiseSessionRequiredException(String sessionName, Class paramType) throws Exception {
        throw new IllegalStateException("缺少session属性: '" + sessionName + "' 类型为 [" + paramType.getName() + "]");
    }

    protected void raisePathVarException(String parthVarName, Class paramType) throws Exception {
        throw new IllegalStateException("缺少pathValue属性: '" + parthVarName + "' 类型为 [" + paramType.getName() + "]");
    }

    protected void raisePathVarUnsupportedException(String parthVarName, Class paramType) throws Exception {
        throw new UnsupportedDataTypeException("不支持的类型pathValue类型，属性: '" + parthVarName + "' 类型为 [" + paramType.getName() + "]");
    }
}
