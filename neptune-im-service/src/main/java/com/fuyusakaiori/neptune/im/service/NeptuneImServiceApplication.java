package com.fuyusakaiori.neptune.im.service;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@MapperScan(value = "com.fuyusakaiori.neptune.im.service.core.*.mapper")
@SpringBootApplication
public class NeptuneImServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(NeptuneImServiceApplication.class, args);

	}

}
