package com.example.cdzocr;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
public class CdzOcrApplication {

    public static void main(String[] args) {
        SpringApplication.run(CdzOcrApplication.class, args);
    }

}
