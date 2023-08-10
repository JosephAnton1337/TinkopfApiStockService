package com.rudnev.tinkopfapistockservice.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;


    @Data
    @ConfigurationProperties(prefix = "api")
    public class ApiConfig {
        private Boolean isSandBoxMode;
    }

