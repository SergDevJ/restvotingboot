package ru.ssk.restvoting.config;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.support.NoOpCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCaching
public class CacheConfig {

    @Bean
    public NoOpCacheManager noOpCacheManager() {
        return new NoOpCacheManager();
    }

}
