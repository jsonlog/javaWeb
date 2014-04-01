package com.isea.tools.args;

import com.isea.controller.User;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.CharUtils;
import org.apache.commons.lang3.math.NumberUtils;
import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl;

import java.lang.reflect.*;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;

/**
 * Created by liuzh on 14-3-21.
 */
public class TypeConverter {


    /**
     * 判断类是否为简单类
     *
     * @param clazz
     * @return
     */
    public static boolean isSimpleProperty(Class<?> clazz) {
        return isSimpleValueType(clazz) || (clazz.isArray() && isSimpleValueType(clazz.getComponentType()));
    }

    /**
     * 判断类是否为简单类
     *
     * @param clazz
     * @return
     */
    public static boolean isSimpleValueType(Class<?> clazz) {
        return clazz.isPrimitive() || clazz.isEnum() ||
                CharSequence.class.isAssignableFrom(clazz) ||
                Number.class.isAssignableFrom(clazz) ||
                Date.class.isAssignableFrom(clazz) ||
                clazz.equals(URI.class) || clazz.equals(URL.class) ||
                clazz.equals(Locale.class) || clazz.equals(Class.class);
    }

    /**
     * 基本类型转换
     *
     * @param value
     * @param requiredType
     * @param <T>
     * @return
     * @throws IllegalArgumentException
     */
    public static <T> T convertToBasic(Object value, Class<T> requiredType)  throws IllegalArgumentException {
        if(requiredType.isInstance(value)){
            return (T)value;
        }
        Object convertedValue = value;
        String strValue = String.valueOf(value);
        if(String.class.isAssignableFrom(requiredType)){
            convertedValue = strValue;
        }
        else if(int.class.equals(requiredType)||Integer.class.equals(requiredType)){
            convertedValue = NumberUtils.toInt(strValue);
        }
        else if(long.class.equals(requiredType)||Long.class.equals(requiredType)){
            convertedValue = NumberUtils.toLong(strValue);
        }
        else if(short.class.equals(requiredType)||Short.class.equals(requiredType)){
            convertedValue = NumberUtils.toShort(strValue);
        }
        else if(float.class.equals(requiredType)||Float.class.equals(requiredType)){
            convertedValue = NumberUtils.toFloat(strValue);
        }
        else if(double.class.equals(requiredType)||Double.class.equals(requiredType)){
            convertedValue = NumberUtils.toDouble(strValue);
        }
        else if(BigDecimal.class.equals(requiredType)){
            convertedValue = NumberUtils.createBigDecimal(strValue);
        }
        else if(boolean.class.equals(requiredType)||Boolean.class.equals(requiredType)){
            convertedValue = BooleanUtils.toBoolean(strValue);
        }
        else if(char.class.equals(requiredType)||Character.class.equals(requiredType)){
            CharUtils.toChar(strValue);
        }
        else if(URL.class.equals(requiredType)){
            try {
                convertedValue = new URL(strValue);
            } catch (MalformedURLException e) {

            }
        }
        else if(URI.class.equals(requiredType)){
            try {
                convertedValue = new URI(strValue);
            } catch (URISyntaxException e) {

            }
        }
        else {
            return null;
        }
        return (T)convertedValue;
    }


    /**
     * 转换为集合或数组
     *
     * @param value
     * @param requiredType
     * @param type
     * @param <T>
     * @return
     * @throws IllegalArgumentException
     */
    public static <T> T convertToCollection(Object value, Class<T> requiredType, Type type)  throws IllegalArgumentException {
        Class<?> clazz = null;
        Object convertedValue = null;
        //数组
        if(requiredType.isArray()){
            clazz = requiredType.getComponentType();
            if(value.getClass().isArray()){
                int length = Array.getLength(value);
                convertedValue = Array.newInstance(clazz,length);
                for(int i=0;i<length;i++){
                    Array.set(convertedValue,i,convertToBasic(Array.get(value,i),clazz));
                }
            }
            else if(Collection.class.isAssignableFrom(value.getClass())){
                Collection collection = (Collection)value;
                Iterator iterator = collection.iterator();
                int length = collection.size();
                convertedValue = Array.newInstance(clazz,length);
                for(int i=0;i<length;i++){
                    Array.set(convertedValue,i,convertToBasic(iterator.next(),clazz));
                }
            }
            else if(isSimpleValueType(value.getClass())){
                convertedValue = Array.newInstance(clazz,1);
                Array.set(convertedValue,0,convertToBasic(value,clazz));
            }
        }
        //集合
        else if(Collection.class.isAssignableFrom(requiredType)){
            ParameterizedType ptype = (ParameterizedType)type;
            clazz = (Class<?>) ptype.getActualTypeArguments()[0];
            if(LinkedList.class.isAssignableFrom(requiredType)){
                convertedValue = new LinkedList();
            }
            else if(List.class.isAssignableFrom(requiredType)){
                convertedValue = new ArrayList();
            }
            else if(SortedSet.class.isAssignableFrom(requiredType)){
                convertedValue = new TreeSet();
            }
            else {
                convertedValue = new LinkedHashSet();
            }
            Collection convertCollection = (Collection)convertedValue;
            //存值
            if(value.getClass().isArray()){
                int length = Array.getLength(value);
                for(int i=0;i<length;i++){
                    convertCollection.add(convertToBasic(Array.get(value,i),clazz));
                }
            }
            else if(Collection.class.isAssignableFrom(value.getClass())){
                Collection collection = (Collection)value;
                Iterator iterator = collection.iterator();
                while(iterator.hasNext()){
                    convertCollection.add(convertToBasic(iterator.next(),clazz));
                }
            }
            else if(isSimpleProperty(value.getClass())){
                convertCollection.add(convertToBasic(value,clazz));
            }
        }
        return (T)convertedValue;
    }

//    测试代码
//    public static void main(String[] args) throws Exception {
//        Method method = TypeConverter.class.getDeclaredMethod("show",List.class);
//        MethodParameter parameter = new MethodParameter(method,0);
//
//        String strs = "1234";
//
//        List<Integer> list = (List<Integer>) convertToCollection(strs,parameter.getParameterType(),parameter.getType());
//
//        System.out.println(list.toString());
//
//    }
//
//    public void show(List<Integer> users){}
}
