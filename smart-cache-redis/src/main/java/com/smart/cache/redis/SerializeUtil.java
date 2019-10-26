package com.smart.cache.redis;

import org.smart4j.cache.SmartCacheException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * 序列化工具类
 */
public class SerializeUtil {
    /**
     * 序列化
     */
    @SuppressWarnings("unchecked")
    public static byte[] serialize(Object object) {
        ObjectOutputStream oos;
        ByteArrayOutputStream baos;
        try {
            baos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(baos);
            oos.writeObject(object);
            return baos.toByteArray();
        } catch (Throwable t) {
            throw new SmartCacheException(t);
        }
    }

    /**
     * 反序列化
     */
    @SuppressWarnings("unchecked")
    public static Object unserialize(byte[] bytes) {
        if(bytes==null){
            return null;
        }
        ByteArrayInputStream bais;
        try {
            bais = new ByteArrayInputStream(bytes);
            ObjectInputStream ois = new ObjectInputStream(bais);
            return ois.readObject();
        }  catch (Exception e) {
            return new String(bytes);
        }

    }
}