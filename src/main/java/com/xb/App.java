package com.xb;

import com.zxb.liqi.annotation.EnableDynamicDB;
import com.zxb.liqi.annotation.LiQiScanner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author Mr.M
 * @date 2023/6/8
 * @Description
 */
@LiQiScanner
//@EnableDynamicDB
@SpringBootApplication
public class App {
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}
