package com.github.jerrymice.permission.store;

import com.github.jerrymice.permission.config.PermissionService;
import com.github.jerrymice.permission.resource.Property;
import lombok.Data;

import java.util.Map;
import java.util.Set;

/**
 * @author tumingjian
 * 说明: 一个PermissonService适配类.用于httpsession中的数据到PermissionService的适配
 */
@Data
public class PermissionServiceAdapter implements PermissionService {
    private Set<Property> characters;
    private Set<Property> resources;
    private Property user;
    private Map<String, Object> extendData;

    public PermissionServiceAdapter(PermissionService permissionLoader) {
        this.characters = permissionLoader.loadCharacters();
        this.resources = permissionLoader.loadResources();
        this.user = permissionLoader.loadUser();
        this.extendData = permissionLoader.loadExtendData();
    }

    public PermissionServiceAdapter(Property user, Set<Property> characters, Set<Property> resources, Map<String, Object> extendData) {
        this.characters = characters;
        this.resources = resources;
        this.user = user;
        this.extendData = extendData;
    }

    @Override
    public Set<Property> loadResources() {
        return this.resources;
    }

    @Override
    public Set<Property> loadCharacters() {
        return this.characters;
    }

    @Override
    public Property loadUser() {
        return this.user;
    }

    @Override
    public Map<String, Object> loadExtendData() {
        return this.extendData;
    }
}
