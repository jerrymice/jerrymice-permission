package com.github.jerrymice.permission.config;

import com.github.jerrymice.permission.resource.Property;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;

/**
 * @author tumingjian
 * 说明:初始化当前用户的RBAC相关信息
 *
 */
public interface PermissionService extends Serializable {
    /**
     * 载入或初始化用户所拥有的资源信息
     * @return Set<Property>
     */
    Set<Property> loadResources();

    /**
     * 载入或初始化用户所拥有的角色信息
     * @return Set<Property>
     */
    Set<Property> loadCharacters();

    /**
     * 载入当前用户的User信息
     * @return Property
     */
    Property loadUser();

    /**
     * 载入或初始化当前用户所拥有的资源的描述信息
     * @return Set<Property>
     */
    default Set<Property> loadMetadata(){
        return null;
    }

    /**
     * 载入或初始化当前用户的其他扩展信息
     * @return Map<String, Object>
     */
    default Map<String, Object> loadExtendData() {
        return null;
    }

}
