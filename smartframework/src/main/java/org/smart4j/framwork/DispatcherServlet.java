package org.smart4j.framwork;

import com.mysql.jdbc.SocketMetadata;
import org.apache.commons.lang3.StringUtils;
import org.smart4j.framwork.bean.Data;
import org.smart4j.framwork.bean.Handler;
import org.smart4j.framwork.bean.Param;
import org.smart4j.framwork.bean.View;
import org.smart4j.framwork.helper.BeanHelper;
import org.smart4j.framwork.helper.ConfigHelper;
import org.smart4j.framwork.helper.ControllerHelper;
import org.smart4j.framwork.util.*;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

@WebServlet(urlPatterns = {"/*"},loadOnStartup = 0)
public class DispatcherServlet extends HttpServlet{
    @Override
    public void init(ServletConfig config) throws ServletException {
        // 先初始化
        HelperLoader.init();
        // 注册Servlet
        ServletContext servletContext = config.getServletContext();

        // 注册JSPServlet
        ServletRegistration jspServlet = servletContext.getServletRegistration("jsp");
        jspServlet.addMapping(ConfigHelper.getAppJspPath()+"*");
        // 注册静态资源
        ServletRegistration defaultServlet = servletContext.getServletRegistration("default");

        defaultServlet.addMapping(ConfigHelper.getAppAssertPath()+"*");
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 获取请求的方法和请求路径
        String requestMethod = req.getMethod().toLowerCase();
        String requestPath = req.getPathInfo();

        // 获取Action处理器
        Handler handler = ControllerHelper.getHandler(requestMethod,requestPath);
        if(handler!=null) {
            // 获取Controller类 及实例
            Class<?> controllerClass = handler.getControllerClass();
            Object controllerBean = BeanHelper.getBean(controllerClass);
            // 创建请求参数请求
            Map<String,Object> paramMap = new HashMap<String, Object>();
            // 得到get请求
            Enumeration<String> paramNames = req.getParameterNames();
            while(paramNames.hasMoreElements()) {
                String paramsName = paramNames.nextElement();
                String paramsValue = req.getParameter(paramsName);
                paramMap.put(paramsName,paramsValue);

            }

            String body = CodecUtil.decodeURL(StreamUtil.getString(req.getInputStream()));
            if(StringUtil.isNotEmpty(body)) {
                String[] params = StringUtils.split(body,"&");
                if(ArrayUtil.isNotEmpty(params)) {
                    for(String param : params){
                        String[] array = StringUtils.split(param,"=");
                        if(ArrayUtil.isNotEmpty(array) && array.length==2) {
                            String paramName = array[0];
                            String paramValue = array[1];
                            paramMap.put(paramName,paramValue);
                        }
                    }
                }
            }
            Param param = new Param(paramMap);
            Method actionMethod = handler.getActionMethod();
            Object result = ReflectionUtil.invokeMethod(controllerBean,actionMethod,param);
            // 处理Action方法返回值
            if(result instanceof View) {
                View view = (View) result;
                String path = view.getPath();
                if(StringUtil.isNotEmpty(path)) {
                    if(path.startsWith("/")) {
                        resp.sendRedirect(req.getContextPath()+path);
                    } else {
                        Map<String,Object> model = view.getModel();
                        for(Map.Entry<String,Object> entry:model.entrySet()) {
                            req.setAttribute(entry.getKey(),entry.getValue());
                        }
                        req.getRequestDispatcher(ConfigHelper.getAppJspPath()+path).forward(req,resp);
                    }
                }
            }else if(result instanceof Data) {
                Data data = (Data)result;
                Object model = data.getModel();
                if(model != null) {
                    resp.setContentType("application/json");
                    resp.setCharacterEncoding("UTF-8");
                    PrintWriter writer = resp.getWriter();
                    String json = JsonUtil.toJson(model);
                    writer.write(json);
                    writer.flush();
                    writer.close();
                }
            }

        }
    }
}
