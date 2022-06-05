package ru.ssk.restvoting.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import ru.ssk.restvoting.application.Settings;

import java.time.LocalTime;

@Configuration
@PropertySource("classpath:app-settings.properties")
public class SystemConfig {

    @Value("#{T(java.time.LocalTime).parse('${system.voteDeadline:12:00}', T(java.time.format.DateTimeFormatter).ofPattern('HH:mm'))}")
    private LocalTime voteDeadline;

    @Bean("systemSettings")
    Settings getSystemSettings() {
        Settings settings = new Settings();
        settings.setVoteDeadline(voteDeadline);
        return settings;
    }

    //    https://stackoverflow.com/questions/15937592/spring-value-is-not-resolving-to-value-from-property-file
    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }
}
