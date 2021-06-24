package com.alten.cancun.hotel.modifybook;

import com.alten.cancun.hotel.advice.ExceptionHandlerAdvice;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.web.client.RestTemplate;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@SpringBootApplication
@EnableDiscoveryClient
@OpenAPIDefinition(info =
@Info(title = "Modify Book Service", version = "1.0", description = "Documentation for Modify Book Service v1.0")
)
@Import({ExceptionHandlerAdvice.class})
public class ModifyBookServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ModifyBookServiceApplication.class, args);
    }

    @Bean
    public Docket swaggerPersonApi10() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.alten.cancun.hotel.modifybook.controller"))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(new ApiInfoBuilder().version("1.0").title("Modify Book Service").description("Modify Book Service v1.0").build());
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

}
