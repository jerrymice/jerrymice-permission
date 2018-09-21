package com.github.jerrymice.permission.store;

import com.github.jerrymice.permission.config.PermissionLoader;
import com.github.jerrymice.permission.resource.Property;
import lombok.Data;

import java.util.Map;
import java.util.Set;

/**
 * @author tumingjian
 * @date 2018/9/14
 * 说明:
 */
@Data
public class PermissionStoreData implements PermissionLoader {
    private Set<Property> characters;
    private Set<Property> resources;
    private Property user;
    private Map<String, Object> extendData;

    public PermissionStoreData(PermissionLoader permissionLoader) {
        this.characters = permissionLoader.loadCharacters();
        this.resources = permissionLoader.loadResources();
        this.user = permissionLoader.loadUser();
        this.extendData = permissionLoader.loadExtendData();
    }

    public PermissionStoreData(Property user,Set<Property> characters, Set<Property> resources, Map<String, Object> extendData) {
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
