<%@ page pageEncoding="UTF-8" %>
<%@ include file="common/global.jsp" %>

<!DOCTYPE html>
<html>
<head>
    <%@ include file="common/meta.jsp" %>
    <title><f:message key="common.title"/> - <f:message key="login"/></title>
    <%@ include file="common/css.jsp" %>
</head>
<body>
<%@ include file="common/header_none.jsp" %>

<div class="container">
    <div class="row">
        <div class="col-sm-6 col-sm-offset-3">
            <div class="panel panel-default">
                <div class="panel-heading"><f:message key="login"/></div>
                <div class="panel-body">
                    <form action="${BASE}/login" method="post" class="form-horizontal">
                        <div class="form-group">
                            <label for="username" class="col-sm-4 control-label text-right"><f:message key="login.username"/>:</label>
                            <div class="col-sm-6">
                                <input type="text" id="username" class="form-control">
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="password" class="col-sm-4 control-label text-right"><f:message key="login.password"/>:</label>
                            <div class="col-sm-6">
                                <input type="password" id="password" class="form-control">
                            </div>
                        </div>
                        <div class="form-group">
                            <div class="col-sm-6 col-sm-offset-4">
                                <div class="checkbox">
                                    <label>
                                        <input type="checkbox"> <f:message key="login.remeber_username"/>
                                    </label>
                                </div>
                            </div>
                        </div>
                        <div class="form-group">
                            <div class="col-sm-8 col-sm-offset-4 text-right">
                                <button type="submit" class="btn btn-default btn-success btn-lg"><f:message key="login"/></button>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>

<%@ include file="common/footer.jsp" %>
<%@ include file="common/js.jsp" %>
</body>
</html>