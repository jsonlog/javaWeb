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

<nav class="navbar navbar-default navbar-fixed-top navbar-inverse">
    <div class="container">
        <div class="navbar-header">
            <div class="navbar-brand"><f:message key="common.title"/></div>
        </div>
    </div>
</nav>

<div class="container">
    <div class="row">
        <div class="col-sm-6 col-sm-offset-3">
            <div class="panel panel-default">
                <div class="panel-heading"><f:message key="login"/></div>
                <div class="panel-body">
                    <form class="form-horizontal" action="${BASE}/login" method="post">
                        <div class="form-group">
                            <label class="col-sm-4 control-label text-right" for="username"><f:message key="login.username"/>:</label>
                            <div class="col-sm-6">
                                <input type="text" class="form-control" id="username">
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-4 control-label text-right" for="password"><f:message key="login.password"/>:</label>
                            <div class="col-sm-6">
                                <input type="password" class="form-control" id="password">
                            </div>
                        </div>
                        <div class="form-group">
                            <div class="col-sm-6 col-sm-offset-4">
                                <div class="checkbox">
                                    <label>
                                        <input type="checkbox"><f:message key="login.remeber_username"/>
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