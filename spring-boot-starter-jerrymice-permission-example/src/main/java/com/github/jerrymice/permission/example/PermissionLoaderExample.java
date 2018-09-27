package com.github.jerrymice.permission.example;

import com.github.jerrymice.permission.annotation.Permission;
import com.github.jerrymice.permission.config.PermissionService;
import com.github.jerrymice.permission.resource.Character;
import com.github.jerrymice.permission.resource.Property;
import com.github.jerrymice.permission.resource.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * @author tumingjian
 * @date 2018/9/14
 * 说明:
 */
@Component
public class PermissionLoaderExample implements PermissionService {
    @Autowired
    private HttpSession httpSession;
    @Override
    public Set<Property> loadResources() {
        HashSet<Property> resources = new HashSet<>();
        Collections.addAll(resources, new Resource("R1001","查询"), new Resource("R1002","编辑"), new Resource("R1003","删除"));
        return resources;
    }
    @Override
    public Set<Property> loadCharacters() {
        HashSet<Property> characters = new HashSet<>();
        Collections.addAll(characters,new Character("C1001","管理员"), new Character("C1002","业务员"), new Character("C1003","财务"));
        return characters;
    }

    @Override
    public Property loadUser() {
        Property currentUser=(Property)httpSession.getAttribute("currentUser");
        return currentUser;
    }

    @Override
    public Map<String, Object> loadExtendData() {
        HashMap<String, Object> map = new HashMap<>(2);
        map.put("特权类型",2);
        map.put("audit",true);
        map.put("status",new int[]{1000,1001,1002,1003,1004,1005});
        map.put("selectColumn",new String[]{"amount","ordernum"});
        return map;
    }
}
