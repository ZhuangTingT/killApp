package com.zhuang.kill;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class KillApplication {

    public static void main(String[] args) {

        SpringApplication.run(KillApplication.class, args);
    }
}
