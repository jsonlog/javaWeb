#smart_plugin_args
==================
###说明
Args 是用来获取Web中Action(或Controller)参数的,
可以通过反射传入参数调用Action方法.  
Args的作用是获取参数,不提供其他功能,
Args的目的是给其他的WEB(Servlet)框架提供获取方法参数值的功能。  

###支持smart 2.3及以上版本  

###下一步会在smart-sample基础上增加该插件的使用方法

###Args支持以下参数注解:  
@Param  
@Path  

###Args支持下面的直接参数类型:  
ServletRequest  
ServletResponse  
HttpSession  
Principal  
Locale  
InputStream  
Reader  
OutputStream  
Writer  

##使用方法:  
###在smart.properties中配置:  
`smart.handler_invoker=com.isea.tools.args.ArgsHandlerInvoker`  