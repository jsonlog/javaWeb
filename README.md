smart4j_plugin_Args
==================

Args 是用来获取Web中Action(或Controller)参数的,
可以通过反射传入参数调用Action方法.<br>
Args的作用是获取参数,不提供其他功能,
Args的目的是给其他的WEB(Servlet)框架提供获取方法参数值的功能。

Args支持以下参数注解:<br>
@Param<br>
@Path<br>

Args支持下面的直接参数类型:<br>
ServletRequest<br>
ServletResponse<br>
HttpSession<br>
Principal<br>
Locale<br>
InputStream<br>
Reader<br>
OutputStream<br>
Writer<br>

支持最新版smart4j
=============