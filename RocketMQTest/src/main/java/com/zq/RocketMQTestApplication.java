package com.zq;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author ZQ
 * @Date 2020/4/1
 */
@SpringBootApplication
@EnableScheduling
public class RocketMQTestApplication {
    public static void main(String[] args) {
        SpringApplication.run(RocketMQTestApplication.class);
    }
}
