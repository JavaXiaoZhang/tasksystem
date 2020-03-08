package com.zq;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author ZQ
 * @Date 2020/2/29
 */
@SpringBootApplication
@MapperScan("com.zq.mapper")
public class TaskApplication {
    public static void main(String[] args) {
        SpringApplication.run(TaskApplication.class);
    }
}
