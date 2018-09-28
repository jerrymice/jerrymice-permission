package com.github.jerrymice.permission.example;

import com.github.jerrymice.permission.resource.Property;
import lombok.Data;

/**
 * @author tumingjian
 * 说明:
 */
@Data
public class User implements Property{
    private String id;
    private String username;
    private String password;

    @Override
    public String getCode() {
        return username;
    }

    @Override
    public String getName() {
        return username;
    }
}
