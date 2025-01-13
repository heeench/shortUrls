package com.example.urlShorter.urls;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "url")
public class UrlConfig {
    private Long maxLifetime;
    private Integer defaultMaxClicks;

    public Integer getDefaultMaxClicks() {
        return defaultMaxClicks;
    }

    public void setDefaultMaxClicks(Integer defaultMaxClicks) {
        this.defaultMaxClicks = defaultMaxClicks;
    }

    public long getMaxLifetime() {
        return maxLifetime;
    }

    public void setMaxLifetime(long maxLifetime) {
        this.maxLifetime = maxLifetime;
    }
}