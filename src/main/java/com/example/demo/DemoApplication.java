package com.example.demo;

import com.example.demo.core.GenericRepositoryFactoryBean;
import com.example.demo.entity.Demo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.bind.annotation.*;

@SpringBootApplication
//@EnableParamEnumConfig
@RestController
@EnableJpaRepositories(repositoryFactoryBeanClass = GenericRepositoryFactoryBean.class)
public class DemoApplication {

    @PostMapping("/test")
    public String test(@RequestBody Demo param) {
        return "ok" + param.getGender();
    }
    @GetMapping("/pt/{gender}")
    public String tset(@PathVariable Demo.Gender gender){
        return gender.toString();
    }

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

}
