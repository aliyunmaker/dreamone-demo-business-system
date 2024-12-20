package org.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author 恬裕
 * @date 2024/1/2
 */
@SpringBootApplication
@ServletComponentScan("org.example")
@EnableScheduling
public class DreamOneBusinessSystemApplication {
    public static void main(String[] args) {
        SpringApplication.run(DreamOneBusinessSystemApplication.class);
    }
}