package com.carson.vboot;

import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;

// 启动admin
@EnableAdminServer
@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class VbootAdminApplication {

    public static void main(String[] args) {

        SpringApplication.run(VbootAdminApplication.class, args);
    }

}
