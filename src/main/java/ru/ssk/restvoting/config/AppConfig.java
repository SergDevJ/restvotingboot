package ru.ssk.restvoting.config;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCaching
// TODO: cache only most requested data!
public class AppConfig {
    //    https://stackoverflow.com/a/46947975/548473
    //    https://stackoverflow.com/questions/38273640/spring-hibernate-jackson-hibernate5module
    @Bean
    Module module() {
        return new Hibernate5Module();
    }
}
