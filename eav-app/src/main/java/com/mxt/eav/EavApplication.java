package com.mxt.eav;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

/**
 * MXT EAV 应用主类
 * 简洁高效的EAV（Entity-Attribute-Value）数据模型实现
 */
@SpringBootApplication
@EnableCaching
public class EavApplication {

    /**
     * 应用入口方法
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        SpringApplication.run(EavApplication.class, args);
    }
}
