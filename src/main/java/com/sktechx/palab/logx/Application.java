package com.sktechx.palab.logx;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Created by 1002382 on 2016. 7. 5..
 */

@EnableJpaRepositories( basePackages = {"com.sktechx.palab.logx.repository"} )
@EntityScan(basePackages= {"com.sktechx.palab.logx.model"})
@SpringBootApplication
@EnableScheduling
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public CorsHeaderFilter corsHeaderFilter(){
        return new CorsHeaderFilter();
    }
}
