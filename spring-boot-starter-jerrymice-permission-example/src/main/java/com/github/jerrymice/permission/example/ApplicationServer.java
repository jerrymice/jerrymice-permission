package com.github.jerrymice.permission.example;

import com.github.jerrymice.permission.starter.EnabledJerryMicePermission;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author tumingjian
 * @date 2018/9/14
 * 说明:
 */
@EnabledJerryMicePermission
@SpringBootApplication
public class ApplicationServer {
    public static void main(String[] args) {
        SpringApplication.run(ApplicationServer.class);
    }
}
