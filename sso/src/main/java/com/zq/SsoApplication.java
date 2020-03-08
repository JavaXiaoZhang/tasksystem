package com.zq;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


/**
 * @author ZQ
 */
@SpringBootApplication
@MapperScan("com.zq.mapper")
public class SsoApplication {
    public static void main(String[] args) {
        SpringApplication.run(SsoApplication.class);
    }
}
