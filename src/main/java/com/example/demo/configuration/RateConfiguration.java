package com.example.demo.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

@Configuration
@ConfigurationProperties(prefix = "rate")
@Setter
@Getter
public class RateConfiguration {
    private String filePath;
    private String sourceUrl;
    @Autowired
    private ResourceLoader resourceLoader;

    public Resource getResource() {
        return resourceLoader.getResource(filePath);
    }
}
