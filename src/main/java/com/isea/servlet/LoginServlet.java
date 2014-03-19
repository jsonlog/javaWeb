package com.isea.servlet;

import com.isea.controller.LoginController;
import com.isea.controller.User;
import com.isea.tools.parameter.ArgsUtil;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by liuzh on 14-3-17.
 */
public class LoginServlet extends HttpServlet {
    @Override
    public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException {
        HttpServletRequest request;
        HttpServletResponse response;

        try {
            request = (HttpServletRequest) req;
            response = (HttpServletResponse) res;
            System.out.println(request.getRequestURL());
            LoginController loginController = new LoginController();

            Method method = null;
            Method[] methods = LoginController.class.getDeclaredMethods();
            for (Method m : methods) {
                if (m.getName().equals("login")) {
                    method = m;
                    break;
                }
            }
            if (method == null) {
                System.out.println("没有找到方法");
            } else {
                ArgsUtil argsUtil = new ArgsUtil();
                Object[] args = new Object[0];
                try {
                    request.getSession().setAttribute("user",new User("手动放入","Session中的"));
                    args = argsUtil.resolveHandlerArguments(request, response, method, "/login/{userid}/{roleName}");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    String result = (String) method.invoke(loginController, args);
                    System.out.println(result);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        } catch (ClassCastException e) {
            throw new ServletException("non-HTTP request or response");
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doGet(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }
}
