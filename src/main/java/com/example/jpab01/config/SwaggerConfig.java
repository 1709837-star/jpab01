package com.example.jpab01.config;
/* API 설명서 만들어주는 설정 */
/* 프로젝트의 REST API를 Swagger 화면에서 문서화하고 테스트할 수 있도록 설정하는 클래스 */
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
//import springfox.documentation.builders.ApiInfoBuilder;
//import springfox.documentation.builders.PathSelectors;
//import springfox.documentation.builders.RequestHandlerSelectors;
//import springfox.documentation.service.ApiInfo;
//import springfox.documentation.spi.DocumentationType;
//import springfox.documentation.spring.web.plugins.Docket;

@Configuration
/* "Spring아, 이 클래스는 설정을 담당하는 클래스야." */

public class SwaggerConfig {

//    @Bean
//    public Docket api() {
//        return new Docket(DocumentationType.OAS_30)
//                .useDefaultResponseMessages(false)
//                .select()
//                .apis(RequestHandlerSelectors.basePackage("com.example.jpab01.controller"))
//                .paths(PathSelectors.any())
//                .build()
//                .apiInfo(apiInfo());
//
//    }
//
//    private ApiInfo apiInfo() {
//        return new ApiInfoBuilder()
//                .title("Boot 01 Project Swagger")
//                .build();
//    }

//    @Bean
//    public GroupedOpenApi restApi() {
//
//        return GroupedOpenApi.builder()
//                .pathsToMatch("/api/**")
//                .group("REST API")
//                .build();
//    }
//
//    @Bean
//    public GroupedOpenApi commonApi() {
//        return GroupedOpenApi.builder()
//                .pathsToMatch("/**")
//                .pathsToExclude("/api/**")
//                .group("COMMON API")
//                .build();
//    }

    /* Swagger에 표시될 API 기본 정보를 설정하는 부분 */
    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI().info(new Info()
                .title("Boot 01 Project API")
                .description("REST 방식 댓글 처리 실습")
                .version("v1"));
    }

    /* Swagger에서 API를 그룹으로 묶는 것 */
    @Bean
    public GroupedOpenApi restApi() {
        return GroupedOpenApi.builder()
                .group("REST API")
                .pathsToMatch("/replies/**")
                .build();
    }

    /* 전체 API 경로 */
    @Bean
    public GroupedOpenApi commonApi() {
        return GroupedOpenApi.builder()
                .group("COMMON API")
                .pathsToMatch("/**")
                .build();
    }
}