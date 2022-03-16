package com.wangx.oj;

import org.apache.catalina.filters.CorsFilter;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@SpringBootApplication
//使用MapperScan批量扫描所有的Mapper接口；
@MapperScan("com.wangx.oj.mapper")
@EnableScheduling // 开启定时任务功能
public class OjApplication {

	public static void main(String[] args) {
		SpringApplication.run(OjApplication.class, args);
	}

}
