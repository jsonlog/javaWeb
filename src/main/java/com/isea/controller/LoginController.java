package com.isea.controller;

import com.isea.tools.parameter.annotation.*;
import com.sun.org.apache.xpath.internal.SourceTree;
import org.apache.commons.fileupload.FileItem;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

/**
 * Created by liuzh on 14-3-17.
 */
public class LoginController {

    @RequestMapping("/login/{userid}/{roleName}")
    public String login(HttpServletRequest request,
                        User user,
                        @SessionAttributes("user")User user2,
                        @RequestParam("hehe")FileItem item,
                        @CookieValue("Hello")String hello,
                        @RequestHeader("Accept")String accept,
                        @PathVariable("userid")int userid,
                        @PathVariable()String roleName) {

        System.out.println("request:"+request.toString());
        System.out.println("User:"+user.toString());
        System.out.println("File:"+item.getFieldName()+","+item.getName());
        System.out.println("Cookie:Hello="+hello);
        System.out.println("Header:Accept="+accept);
        System.out.println("userid:"+userid);
        System.out.println("roleName:"+roleName);

        return "SUCCESS";
    }

    @RequestMapping("/login/{userid}/{roleName}")
    public String login2(HttpServletRequest request,
                        User user,
                        @SessionAttributes("user")User user2,
                        @RequestParam("hehe")FileItem item,
                        @CookieValue("Hello")String hello,
                        @RequestHeader("Accept")String accept,
                        @PathVariable("userid")int userid,
                        @PathVariable()String roleName) {

        System.out.println("request:"+request.toString());
        System.out.println("User:"+user.toString());
        System.out.println("File:"+item.getFieldName()+","+item.getName());
        System.out.println("Cookie:Hello="+hello);
        System.out.println("Header:Accept="+accept);
        System.out.println("userid:"+userid);
        System.out.println("roleName:"+roleName);

        return "SUCCESS";
    }
}
