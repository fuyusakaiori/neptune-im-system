package com.fuyusakaiori.nep.im.service;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@Slf4j
@MapperScan(value = "com.fuyusakaiori.nep.im.service.core.*.mapper")
@SpringBootApplication
public class NeptuneImServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(NeptuneImServiceApplication.class, args);

	}

}
