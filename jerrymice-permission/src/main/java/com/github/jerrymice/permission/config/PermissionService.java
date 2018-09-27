package com.github.jerrymice.permission.config;

import com.github.jerrymice.permission.resource.Property;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;

/**
 * @author tumingjian
 * @date 2018/9/13
 * 说明:
 */
public interface PermissionService extends Serializable {
    Set<Property> loadResources();

    Set<Property> loadCharacters();

    Property loadUser();
    default Set<Property> loadMetadata(){
        return null;
    }
    default Map<String, Object> loadExtendData() {
        return null;
    }

}
