package com.smart.bootstrap.action;

import com.smart.framework.DataContext;
import com.smart.framework.annotation.Action;
import com.smart.framework.annotation.Request;
import com.smart.framework.bean.Page;
import com.smart.framework.bean.Result;

@Action
public class SystemAction {

    @Request("post:/login")
    public Page login() {
        return new Page("/welcome");
    }

    @Request("get:/logout")
    public Result logout() {
        DataContext.Session.removeAll();
        return new Result(true);
    }
}
