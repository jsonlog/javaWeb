Args
==================

Args 是用来获取Web中Action(或Controller)参数的,可以通过反射传入参数调用Action方法,Args的作用是获取参数,不提供其他功能

Args支持以下参数注解：<br>
@CookieValue<br>
@ModelAttribute<br>
@PathVariable<br>
@RequestHeader<br>
@RequestParam<br>
@SessionAttributes<br>

Args支持下面的方法注解:<br>
@RequestMapping:该注解用来支持@PathVariable注解<br>

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
org.json.JSONWriter（需要org.json包)<br>

具体使用方法见最下面的例子。<br>


分支:Smart_args
=======
该分支提供了一个便于smart框架使用的方法类：com.isea.tools.smart.SmartArgsUtil<br>

调用方法：
<pre><code>SmartArgsUtil argsUtil = new SmartArgsUtil();
Object[] args = argsUtil.resolveHandlerArguments(request, response, actionBean.getActionMethod());
</code></pre>
将上面返回的args参数作为反射调用invoke中的参数.

然后在action中的方法可以像下面这样写(例子):
<pre><code>@RequestMapping("/login/{userid}/{roleName}")
public String login2(HttpServletRequest request,
                    User user,
                    @SessionAttributes("user")User user2,
                    @RequestParam("hehe")FileItem item,
                    @CookieValue("Hello")String hello,
                    @RequestHeader("Accept")String accept,
                    @PathVariable("userid")int userid,
                    @PathVariable()String roleName) {

    System.out.println("request:"+request.toString());
    System.out.println("User:"+user.toString());
    System.out.println("File:"+item.getFieldName()+","+item.getName());
    System.out.println("Cookie:Hello="+hello);
    System.out.println("Header:Accept="+accept);
    System.out.println("userid:"+userid);
    System.out.println("roleName:"+roleName);

    return "SUCCESS";
}</code></pre>



需要在smart-framework（最新代码）中做如下修改:

原代码:
<pre><code>private void invokeActionMethod(HttpServletRequest request, HttpServletResponse response, ActionBean actionBean, Matcher requestPathMatcher) throws Exception {
   // 获取 Action 相关信息
   Class<?> actionClass = actionBean.getActionClass();
   Method actionMethod = actionBean.getActionMethod();
   // 从 BeanHelper 中创建 Action 实例
   Object actionInstance = BeanHelper.getBean(actionClass);
   // 调用 Action 方法
   Object actionMethodResult;
   Class<?>[] paramTypes = actionMethod.getParameterTypes();
   List<Object> paramList = createActionMethodParamList(request, actionBean, requestPathMatcher);
   if (paramTypes.length != paramList.size()) {
       throw new RuntimeException("由于参数不匹配，无法调用 Action 方法！");
   }
   actionMethod.setAccessible(true); // 取消类型安全检测（可提高反射性能）
   actionMethodResult = actionMethod.invoke(actionInstance, paramList.toArray());
   // 处理 Action 方法返回值
   handleActionMethodReturn(request, response, actionMethodResult);
}</code></pre>


修改为:
<pre><code>private void invokeActionMethod(HttpServletRequest request, HttpServletResponse response, ActionBean actionBean, Matcher requestPathMatcher) throws Exception {
   // 获取 Action 相关信息
   Class<?> actionClass = actionBean.getActionClass();
   Method actionMethod = actionBean.getActionMethod();
   // 从 BeanHelper 中创建 Action 实例
   Object actionInstance = BeanHelper.getBean(actionClass);
   // 调用 Action 方法
   Object actionMethodResult;
   Class<?>[] paramTypes = actionMethod.getParameterTypes();

   Object[] args = new SmartArgsUtil().resolveHandlerArguments(request,response,actionMethod);

   if (paramTypes.length != args.length) {
       throw new RuntimeException("由于参数不匹配，无法调用 Action 方法！");
   }
   actionMethod.setAccessible(true); // 取消类型安全检测（可提高反射性能）
   actionMethodResult = actionMethod.invoke(actionInstance, args);
   // 处理 Action 方法返回值
   handleActionMethodReturn(request, response, actionMethodResult);
}</code></pre>
