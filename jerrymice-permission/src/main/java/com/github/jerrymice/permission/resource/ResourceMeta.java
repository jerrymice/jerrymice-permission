package com.github.jerrymice.permission.resource;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author tumingjian
 * 说明:用户资源描述实体
 */
public interface ResourceMeta extends Property {
    Property getParentResource();
    Set<Property> getCharters();
    Object getValue();
}
