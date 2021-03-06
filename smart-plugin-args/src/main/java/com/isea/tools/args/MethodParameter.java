package com.isea.tools.args;

import javassist.*;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.LocalVariableAttribute;
import javassist.bytecode.MethodInfo;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;

/**
 * Created by liuzh on 14-3-11.
 */
public class MethodParameter {
    private static final ClassPool pool = new ClassPool();

    private final Method method;

    private final int parameterIndex;

    private Class<?> parameterType;

    private Type type;

    private Annotation[] parameterAnnotations;

    private String parameterName;

    static{
        pool.appendClassPath(new ClassClassPath(MethodParameter.class));
    }

    /**
     * Create a new MethodParameter for the given method, with nesting level 1.
     * @param method the Method to specify a args for
     * @param parameterIndex the index of the args
     */
    public MethodParameter(Method method, int parameterIndex) {
        this.method = method;
        this.parameterIndex = parameterIndex;
    }

    /**
     * Return the type of the method/constructor args.
     * @return the args type (never <code>null</code>)
     */
    public Type getType() {
        if (this.type == null) {
            if (this.parameterIndex < 0) {
                this.type = (this.method != null ? this.method.getReturnType() : null);
            }
            else {
                this.type = (this.method != null ? this.method.getGenericParameterTypes()[this.parameterIndex] :null);
            }
        }
        return this.type;
    }

    /**
     * Return the type of the method/constructor args.
     * @return the args type (never <code>null</code>)
     */
    public Class<?> getParameterType() {
        if (this.parameterType == null) {
            if (this.parameterIndex < 0) {
                this.parameterType = (this.method != null ? this.method.getReturnType() : null);
            }
            else {
                this.parameterType = (this.method != null ? this.method.getParameterTypes()[this.parameterIndex] :null);
            }
        }
        return this.parameterType;
    }


    /**
     * Return the annotations associated with the specific method/constructor args.
     */
    public Annotation[] getParameterAnnotations() {
        if (this.parameterAnnotations == null) {
            Annotation[][] annotationArray = (this.method != null ? this.method.getParameterAnnotations() : null);
            if (this.parameterIndex >= 0 && this.parameterIndex < annotationArray.length) {
                this.parameterAnnotations = annotationArray[this.parameterIndex];
            }
            else {
                this.parameterAnnotations = new Annotation[0];
            }
        }
        return this.parameterAnnotations;
    }

    /**
     * 返回方法上的注解
     * @param clazz
     * @return
     */
    public Annotation getMethodAnnotation(Class<? extends Annotation> clazz) {
        if(this.method!=null){
            return this.method.getAnnotation(clazz);
        }
        return null;
    }

    /**
     * 获取参数名
     * @return
     */
    public String getParameterName() {
        if ((parameterName == null || parameterName.equals(""))
                &&method != null){
            String[] parameterNames = null;
            try {
                parameterNames = getMethodParamNames(method.getDeclaringClass(),method.getName(),method.getParameterTypes());
            } catch (Exception e) {
                //异常不处理
            }
            if (parameterNames != null) {
                parameterName = parameterNames[this.parameterIndex];
            }
        }
        return parameterName;
    }

    /**
     * 获取方法参数名
     * @param cm
     * @return
     * @throws Exception
     */
    protected String[] getMethodParamNames(CtMethod cm) throws Exception {
        CtClass cc = cm.getDeclaringClass();
        MethodInfo methodInfo = cm.getMethodInfo();
        CodeAttribute codeAttribute = methodInfo.getCodeAttribute();
        LocalVariableAttribute attr = (LocalVariableAttribute) codeAttribute
                .getAttribute(LocalVariableAttribute.tag);
        if (attr == null) {
            throw new Exception(cc.getName());
        }

        String[] paramNames = null;
        try {
            paramNames = new String[cm.getParameterTypes().length];
        } catch (NotFoundException e) {
            throw e;
        }
        int pos = Modifier.isStatic(cm.getModifiers()) ? 0 : 1;
        for (int i = 0; i < paramNames.length; i++) {
            paramNames[i] = attr.variableName(i + pos);
        }
        return paramNames;
    }

    /**
     * 获取方法参数名称，按给定的参数类型匹配方法
     * @param clazz
     * @param method
     * @param paramTypes
     * @return
     * @throws Exception
     */
    protected String[] getMethodParamNames(Class<?> clazz, String method,
                                               Class<?>... paramTypes) throws Exception {
        CtMethod cm = null;
        try {
            CtClass cc = pool.get(clazz.getName());

            String[] paramTypeNames = new String[paramTypes.length];
            for (int i = 0; i < paramTypes.length; i++)
                paramTypeNames[i] = paramTypes[i].getName();

            cm = cc.getDeclaredMethod(method, pool.get(paramTypeNames));
        } catch (NotFoundException e) {
            throw e;
        }
        return getMethodParamNames(cm);
    }

    /**
     * 获取方法参数名称，匹配同名的某一个方法
     * @param clazz
     * @param method
     * @return
     * @throws Exception
     */
    protected String[] getMethodParamNames(Class<?> clazz, String method) throws Exception {
        CtClass cc;
        CtMethod cm = null;
        try {
            cc = pool.get(clazz.getName());
            cm = cc.getDeclaredMethod(method);
        } catch (NotFoundException e) {
            throw e;
        }
        return getMethodParamNames(cm);
    }
}
