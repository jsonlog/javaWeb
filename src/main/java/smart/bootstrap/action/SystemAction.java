package smart.bootstrap.action;

import smart.framework.DataContext;
import smart.framework.annotation.Action;
import smart.framework.annotation.Request;
import smart.framework.bean.Result;
import smart.framework.bean.View;

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
