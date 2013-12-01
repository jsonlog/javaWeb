package com.smart.bootstrap.action;

import com.smart.framework.DataContext;
import com.smart.framework.annotation.Bean;
import com.smart.framework.annotation.Request;
import com.smart.framework.base.BaseAction;
import com.smart.framework.bean.Page;
import com.smart.framework.bean.Result;

@Bean
public class SystemAction extends BaseAction {

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
