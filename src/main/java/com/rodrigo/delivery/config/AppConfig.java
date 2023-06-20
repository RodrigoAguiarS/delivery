package com.rodrigo.delivery.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
public class AppConfig {

    @Value("${cep.url}")
    private String cepUrl;
}
