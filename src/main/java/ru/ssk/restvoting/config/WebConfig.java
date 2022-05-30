package ru.ssk.restvoting.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import ru.ssk.restvoting.web.json.JacksonObjectMapper;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Configuration
@EnableSwagger2
@EnableWebMvc
@EnableJpaRepositories
@ComponentScan("ru.ssk.restvoting.web")
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Import({SwaggerConfig.class})
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/swagger-ui/**").addResourceLocations("/resources/swagger-ui/");
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        StringHttpMessageConverter messageConverter = new StringHttpMessageConverter(StandardCharsets.UTF_8);
        messageConverter.setDefaultCharset(StandardCharsets.UTF_8);
        messageConverter.setSupportedMediaTypes(Arrays.asList(
                new MediaType("text", "plain", StandardCharsets.UTF_8),
                new MediaType("text", "html", StandardCharsets.UTF_8),
                MediaType.APPLICATION_JSON));
        converters.add(messageConverter);

        MappingJackson2HttpMessageConverter jsonConverter = new MappingJackson2HttpMessageConverter();
        List<MediaType> supportedMediaTypes = new ArrayList<>(jsonConverter.getSupportedMediaTypes());
        supportedMediaTypes.add(MediaType.valueOf(MediaType.APPLICATION_JSON_VALUE));
        jsonConverter.setSupportedMediaTypes(supportedMediaTypes);
        jsonConverter.setObjectMapper(JacksonObjectMapper.getMapper());
        converters.add(jsonConverter);
    }

//    https://stackoverflow.com/questions/38273640/spring-hibernate-jackson-hibernate5module
//    @Bean
//    public Hibernate5Module hibernate5Module() {
//        return new Hibernate5Module();
//    }
}