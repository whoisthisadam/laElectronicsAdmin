package com.kasperovich.laelectronics.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableCaching
@SpringBootApplication
@ComponentScan({"com.kasperovich"})
@EnableJpaRepositories("com.kasperovich.laelectronics.repository")
@EnableAspectJAutoProxy
@EntityScan(basePackages = {"com.kasperovich.laelectronics.models"})
public class LaElectronicsApiApp {
    public static void main(String[] args) {
        SpringApplication.run(LaElectronicsApiApp.class, args);
    }
}
