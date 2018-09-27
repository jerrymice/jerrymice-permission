package com.github.jerrymice.permission.engine;

/**
 * @author tumingjian
 * 说明:普通异常包装类
 */
public class PermissionException extends RuntimeException {
    public PermissionException(Throwable cause) {
        super(cause);
    }
}
