<%@ page pageEncoding="UTF-8" %>
<%@ include file="common/global.jsp" %>

<!DOCTYPE html>
<html>
<head>
    <%@ include file="common/meta.jsp" %>
    <title><f:message key="common.title"/> - <f:message key="welcome"/></title>
    <%@ include file="common/css.jsp" %>
</head>
<body>
<%@ include file="common/header.jsp" %>

<div class="container">
    <div class="jumbotron">
        <h1><f:message key="welcome.word"/></h1>
    </div>
</div>

<%@ include file="common/footer.jsp" %>
<%@ include file="common/js.jsp" %>
</body>
</html>