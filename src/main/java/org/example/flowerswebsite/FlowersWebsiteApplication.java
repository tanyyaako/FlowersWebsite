package org.example.flowerswebsite;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class FlowersWebsiteApplication {

    public static void main(String[] args) {
        SpringApplication.run(FlowersWebsiteApplication.class, args);
    }

}
