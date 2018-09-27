package com.github.jerrymice.permission.config;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author tumingjian
 * @date 2018/9/14
 * 说明:
 */
@Data
@Accessors(chain = true)
public class PermissionConfig {
    private boolean mixtureSearch = false;
    private boolean threadLocalCache = true;
}
