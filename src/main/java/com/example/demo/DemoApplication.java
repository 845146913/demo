package com.example.demo;

import com.example.demo.core.GenericRepositoryFactoryBean;
import com.example.demo.entity.Demo;
import feign.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.bind.annotation.*;

@SpringBootApplication
//@EnableParamEnumConfig
@RestController
@EnableJpaRepositories(repositoryFactoryBeanClass = GenericRepositoryFactoryBean.class)
@RefreshScope
@EnableFeignClients
public class DemoApplication {

    @Bean
    Logger.Level level(){
        return Logger.Level.FULL;
    }

    @Value("${test.scope:0}")
    private int scope;
    @PostMapping("/test")
    public String test(@RequestBody Demo param) {
        return "ok" + param.toString() + " scope:"+scope;
    }
    @GetMapping("/pt/{gender}")
    public String tset(@PathVariable Demo.Gender gender){
        return gender.toString();
    }

    public static void main(String[] args) {

        ConfigurableApplicationContext run = SpringApplication.run(DemoApplication.class, args);
        System.out.println(run.getEnvironment().getProperty("test.scope"));
    }

}
