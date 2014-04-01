package com.isea.tools.args;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.CharUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * Created by liuzh on 14-3-21.
 */
public class TypeConverter {

    /**
     * 8种基本类型转换
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
        else if(Number.class.isAssignableFrom(requiredType)){
            if(NumberUtils.isNumber(strValue)){
                if(int.class.equals(requiredType)||Integer.class.equals(requiredType)){
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
            }
            else {
                convertedValue = 0;
            }
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

}
