package ru.ssk.restvoting.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Response;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.Arrays;
import java.util.List;

@Configuration
public class SwaggerConfig {
    @Bean
    public Docket openApi() {
        List<Response> responseMessages = Arrays.asList(
                new ResponseBuilder().code("401").description("You are not authorized for this action").build(),
                new ResponseBuilder().code("403").description("Accessing the resource you were trying to reach is forbidden").build());

        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.withClassAnnotation(RestController.class))
                .paths(PathSelectors.any())
                .build()
                .useDefaultResponseMessages(false)
                .globalResponses(HttpMethod.POST, responseMessages)
                .globalResponses(HttpMethod.PUT, responseMessages)
                .globalResponses(HttpMethod.GET, responseMessages)
                .globalResponses(HttpMethod.DELETE, responseMessages);
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Restaurant voting system API specification")
                .description("Restaurant voting system REST API")
                .license("Apache 2.0")
                .licenseUrl("http://www.apache.org/licenses/LICENSE-2.0.html")
                .version("1.0.0")
                .build();
    }
}
