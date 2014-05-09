package org.smart4j.bootstrap.action;

import org.smart4j.framework.mvc.DataContext;
import org.smart4j.framework.mvc.annotation.Action;
import org.smart4j.framework.mvc.annotation.Request;
import org.smart4j.framework.mvc.bean.Result;
import org.smart4j.framework.mvc.bean.View;

@Action
public class SystemAction {

    @Request.Get("/login")
    public View login() {
        return new View("login.jsp");
    }

    @Request.Post("/login")
    public View loginSubmit() {
        return new View("/welcome");
    }

    @Request.Get("/welcome")
    public View welcome() {
        return new View("welcome.jsp");
    }

    @Request.Get("/logout")
    public Result logout() {
        DataContext.Session.removeAll();
        return new Result(true);
    }
}
