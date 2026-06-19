package com.xhs;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.xhs.mapper")
public class XhsApplication {
    public static void main(String[] args) {
        SpringApplication.run(XhsApplication.class, args);
    }
}
