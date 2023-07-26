package com.ll.jigumiyak.ksyTest;

import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

public class ConfigK {
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

}