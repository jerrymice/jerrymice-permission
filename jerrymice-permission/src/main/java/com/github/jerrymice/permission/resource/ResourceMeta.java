package com.github.jerrymice.permission.resource;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author tumingjian
 * @date 2018/9/17
 * 说明:
 */
public interface ResourceMeta extends Property {
    Property getParentResource();
    Set<Property> getCharters();
    Object getValue();
}
