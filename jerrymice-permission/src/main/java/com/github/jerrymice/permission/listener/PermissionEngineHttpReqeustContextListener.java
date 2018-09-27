package com.github.jerrymice.permission.listener;

import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.http.HttpServletRequest;

/**
 * @author tumingjian
 * 说明:  将当前request信息放入 PermissionEngineRequestContextHolder 的threadlocal中
 */
public class PermissionEngineHttpReqeustContextListener implements ServletRequestListener {
    @Override
    public void requestDestroyed(ServletRequestEvent requestEvent) {
        PermissionEngineRequestContextHolder.setRequest(null);
    }

    @Override
    public void requestInitialized(ServletRequestEvent requestEvent) {
        if (!(requestEvent.getServletRequest() instanceof HttpServletRequest)) {
            throw new IllegalArgumentException(
                    "Request is not an HttpServletRequest: " + requestEvent.getServletRequest());
        }
        PermissionEngineRequestContextHolder.setRequest((HttpServletRequest) requestEvent);
    }
}
