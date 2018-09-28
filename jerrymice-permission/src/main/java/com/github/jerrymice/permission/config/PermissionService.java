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
     * @return 当前用户资源列表
     */
    Set<Property> loadResources();

    /**
     * 载入或初始化用户所拥有的角色信息
     * @return 当前用户角色列表
     */
    Set<Property> loadCharacters();

    /**
     * 载入当前用户的User信息
     * @return 当前用户实体
     */
    Property loadUser();

    /**
     * 载入或初始化当前用户所拥有的资源的描述信息
     * @return 当前用户所属角色的资源描述信息
     */
    default Set<Property> loadMetadata(){
        return null;
    }

    /**
     * 载入或初始化当前用户的其他扩展信息
     * @return 当前用户扩展JS数据
     */
    default Map<String, Object> loadExtendData() {
        return null;
    }

}
