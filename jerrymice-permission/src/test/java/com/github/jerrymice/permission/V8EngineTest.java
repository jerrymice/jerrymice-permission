package com.github.jerrymice.permission;

import com.eclipsesource.v8.V8;
import com.eclipsesource.v8.V8Array;
import com.eclipsesource.v8.V8Object;
import com.eclipsesource.v8.utils.V8ObjectUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.jerrymice.permission.engine.GoogleV8ScriptEngine;
import com.github.jerrymice.permission.resource.Property;
import com.github.jerrymice.permission.resource.Resource;
import com.github.jerrymice.permission.resource.User;
import org.junit.Test;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author tumingjian
 * @date 2018/9/19
 * 说明:
 */
public class V8EngineTest {


    @Test
    public void test1(){
        V8 engine = V8.createV8Runtime();
        V8Array array=new V8Array(engine);
        array.push(123);
        array.push(124);
        array.push(125);
        array.push(126);
        array.push(127);
        array.push(128);
        engine.add("test",array);
        V8Object obj = engine.executeObjectScript("test.filter(i => i>126)");
        System.out.println(obj);
        obj.release();
        array.release();
        engine.release();
    }
    @Test
    public void test3()throws Exception{
        List<Property> a=null;
        Set<Property> collect = Optional.ofNullable(a).orElseGet(ArrayList::new).stream().filter(i -> i.getCode().equals("1")).collect(Collectors.toSet());
        Set<Property> x = Optional.ofNullable(collect).orElseGet(null);
        System.out.println(x);
//        Optional<List<Property>> a1 = Optional.ofNullable(a).orElseGet();
//        List<Property> properties = a1.orElseGet(null);
    }

    @Test
    public void test2()throws Exception{
        V8 engine = V8.createV8Runtime();
        User user = new User("123", "12312343434");
        Resource resource = new Resource("5678", "资源");
        user.setResource(resource);
        ObjectMapper build = new Jackson2ObjectMapperBuilder().build();
        HashMap hashMap = build.convertValue(user, HashMap.class);
        V8Object v8Object = V8ObjectUtils.toV8Object(engine, hashMap);
        Map<String, Object> stringMap = V8ObjectUtils.toMap(v8Object);
        System.out.println(stringMap);
        engine.add("user",v8Object);
        Object x = engine.executeScript("user.resource");
        if(x instanceof V8Object){
            System.out.println("v8object");
        }
        Map<String, ? super Object> b= V8ObjectUtils.toMap(v8Object);
//        System.out.println(x.isUndefined());
//        stringMap = V8ObjectUtils.toMap(x);
        System.out.println(b);
    }


    @Test
    public void test4()throws Exception{
        GoogleV8ScriptEngine engine = new GoogleV8ScriptEngine(true);
        ArrayList<Integer> list = new ArrayList<>();
        list.add(1001);
        list.add(1002);
        list.add(1003);
        list.add(1004);
        list.add(1005);
        engine.put("arr",list);
        Object eval = engine.eval("arr.merge(12344,12345).removes(i=>i>1003)");
        System.out.println(eval);
    }
}
