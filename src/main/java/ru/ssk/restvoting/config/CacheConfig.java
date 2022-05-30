package ru.ssk.restvoting.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.EnableCaching;
//import org.springframework.cache.jcache.JCacheCacheManager;
//import org.springframework.cache.jcache.JCacheManagerFactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.net.URI;

@Configuration
@EnableCaching
public class CacheConfig {
    @Autowired
    private ApplicationContext context;

    private final String ehcacheXmlPath = "classpath:cache/ehcache.xml";

//    @Bean
//    @Autowired
//    public JCacheCacheManager ehCacheCacheManager(JCacheManagerFactoryBean cacheManagerFactoryBean) throws IOException {
//        JCacheCacheManager jCacheCacheManager = new JCacheCacheManager();
//        jCacheCacheManager.setCacheManager(cacheManagerFactoryBean.getObject());
//        return jCacheCacheManager;
//    }

//    @Bean
//    JCacheManagerFactoryBean cacheManagerFactoryBean() {
//        JCacheManagerFactoryBean cmfb = new JCacheManagerFactoryBean();
//        Resource resource = context.getResource(ehcacheXmlPath);
//        URI resourceUri;
//        try {
//            resourceUri = resource.getURI();
//        } catch (IOException e) {
//            throw new IllegalStateException(String.format("Error creating cacheManager URI from resource '%s'", ehcacheXmlPath), e);
//        }
//        cmfb.setCacheManagerUri(resourceUri);
//        return cmfb;
//    }
}
