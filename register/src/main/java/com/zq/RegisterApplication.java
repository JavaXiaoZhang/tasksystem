package com.zq;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * @author ZQ
 * @Date 2020/2/29
 */
@SpringBootApplication
@MapperScan("com.zq.mapper")
@EnableEurekaClient
public class RegisterApplication {
    public static void main(String[] args) {
        SpringApplication.run(RegisterApplication.class);
    }
}
