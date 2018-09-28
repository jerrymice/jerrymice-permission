package com.github.jerrymice.permission.example;

import com.github.jerrymice.permission.starter.EnabledJerryMicePermission;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author tumingjian
 * 说明: spring boot 启动类
 */
@EnabledJerryMicePermission
@SpringBootApplication
public class ApplicationServer {
    public static void main(String[] args) {
        SpringApplication.run(ApplicationServer.class);
    }
}
