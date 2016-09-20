package com.sktechx.palab.logx;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration;
import org.springframework.context.annotation.Bean;

/**
 * Created by 1002382 on 2016. 7. 5..
 */

@SpringBootApplication(exclude = JpaRepositoriesAutoConfiguration.class)
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public CorsHeaderFilter corsHeaderFilter(){
        return new CorsHeaderFilter();
    }

}
