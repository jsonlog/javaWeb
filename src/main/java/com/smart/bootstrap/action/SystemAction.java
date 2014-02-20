package com.smart.bootstrap.action;

import com.smart.framework.DataContext;
import com.smart.framework.annotation.Action;
import com.smart.framework.annotation.Request;
import com.smart.framework.bean.Result;
import com.smart.framework.bean.View;

@Action
public class SystemAction {

    @Request("POST:/login")
    public View login() {
        return new View("/welcome");
    }

    @Request("GET:/logout")
    public Result logout() {
        DataContext.Session.removeAll();
        return new Result(true);
    }
}
