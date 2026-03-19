package com.hana8.hanaro;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class HanaroApplication {

    public static void main(String[] args) {
        SpringApplication.run(HanaroApplication.class, args);
    }

}
