package com.smart.cache.redis;

import com.smart.cache.ISmartCache;
import com.smart.cache.ISmartCacheManager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import com.smart.model.Person;
import org.junit.Test;

public class RedisCacheTest {
    /**
     * 默认cacheManager.getCache("redis");  默认对应config.properties中的cache.redis.ip
     * cacheManager.getCache("xxx"); 对应config.properties中的cache.redis.ip.xxx
     *
     * */

    @Test
    public void test() {
        ISmartCacheManager cacheManager = new RedisCacheManager();
        ISmartCache<String, Object> cache = cacheManager.getCache("redis");
        System.out.println(cache.get("lu"));
        System.out.println(cache.put("lu","heihei"));
        System.out.println(cache.get("lu"));


    }

    @Test
    public void test2() {
        ISmartCacheManager cacheManager = new RedisCacheManager();
        ISmartCache<Integer, Integer> cache = cacheManager.getCache("one");


        System.out.println(cache.put(123,123));
        System.out.println(cache.get(123)+1);

    }

    @Test
    public void testRemove(){
        ISmartCacheManager cacheManager = new RedisCacheManager();
        ISmartCache<Object, Object> cache = cacheManager.getCache("xxx");
        System.out.println(cache.get(123));
        System.out.println(cache.remove(123));
        System.out.println(cache.get(123));
    }

    @Test
    public void testClear(){
        ISmartCacheManager cacheManager = new RedisCacheManager();
        ISmartCache<Object, Object> cache = cacheManager.getCache("cache_name");
        System.out.println(cache.get(123));
        cache.clear();
        System.out.println(cache.get(123));
        System.out.println(cache.get("lu"));
    }

    @Test
    public void testSize(){
        ISmartCacheManager cacheManager = new RedisCacheManager();
        ISmartCache<Object, Object> cache = cacheManager.getCache("cache_name");
        System.out.println(cache.size());
    }

    @Test
    public void testKeys(){
        ISmartCacheManager cacheManager = new RedisCacheManager();
        ISmartCache<Object, Object> cache = cacheManager.getCache("cache_name");
        Set<Object> keys =cache.keys();
        for(Object obj:keys){
            System.out.println(obj.toString());
        }
    }

    @Test
    public void testValues(){
        ISmartCacheManager cacheManager = new RedisCacheManager();
        ISmartCache<Object, Object> cache = cacheManager.getCache("cache_name");
        Collection<Object> list =cache.values();
        for(Object obj:list){
            System.out.println(obj);
        }
    }

    @Test
    public void testSerializable2(){
        ISmartCacheManager cacheManager = new RedisCacheManager();
        ISmartCache<String, Person> cache = cacheManager.getCache("person");

        Person p = new Person();
        p.setName("lujianing");
        p.setAge(24);
        cache.put("p1",p);
        Person p2 = cache.get("p1");
        System.out.println(p2);
    }

    @Test
    public void testSerializable3(){
        ISmartCacheManager cacheManager = new RedisCacheManager();
        ISmartCache<String, List<Person>> cache = cacheManager.getCache("person");

        Person p1 = new Person();
        p1.setName("lu");
        p1.setAge(1);

        Person p2 = new Person();
        p2.setName("l1");
        p2.setAge(2);

        List<Person> list = new ArrayList<Person>();
        list.add(p1);
        list.add(p2);

        cache.put("list1",list);

        List<Person> list2 = cache.get("list1");
        for(Person person:list2){
            System.out.println(person);
        }


    }



//    @Test
//    public void listTest() {
//        List<String> list = new ArrayList<String>();
//        list.add("A");
//        list.add("B");
//        list.add("C");
//    }
}
