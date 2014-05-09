<%@ page pageEncoding="UTF-8" %>

<nav class="navbar navbar-default navbar-fixed-top navbar-inverse">
    <div class="container">
        <div class="navbar-header">
            <button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#navbar">
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
            <a class="navbar-brand" href="${BASE}/welcome"><f:message key="common.title"/></a>
        </div>
        <div class="collapse navbar-collapse" id="navbar">
            <ul class="nav navbar-nav">
                <li><a href="#"><f:message key="menu.menu1"/></a></li>
                <li><a href="#"><f:message key="menu.menu2"/></a></li>
            </ul>
            <ul class="nav navbar-nav navbar-right">
                <li class="dropdown">
                    <a class="dropdown-toggle" href="" data-toggle="dropdown"><f:message key="common.setting"/> <b class="caret"></b></a>
                    <ul class="dropdown-menu">
                        <li><a href="#"><f:message key="common.edit_profile"/></a></li>
                        <li><a href="#"><f:message key="common.change_password"/></a></li>
                    </ul>
                </li>
                <li><a href="#" id="logout"><f:message key="common.logout"/></a></li>
            </ul>
        </div>
    </div>
</nav>