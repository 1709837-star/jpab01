package com.example.jpab01.config;
/* 서버가 가공하지 않고 그대로 브라우저에 전달해주는 파일 */
/* Spring MVC의 설정을 직접 커스터마이징하는 정적자원 */
/* ex) CSS, JavaScript, 이미지, 폰트 */
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;

import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class CustomServletConfig implements WebMvcConfigurer {

    // "브라우저가 특정 주소로 요청하면 실제 파일은 어디에서 찾아올지 알려줄게."
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        registry.addResourceHandler("/js/**")
                .addResourceLocations("classpath:/static/js/");
        // "URL에서 /js/로 요청이 들어오면 static/js 폴더에서 파일 찾아줘."
        registry.addResourceHandler("/fonts/**")
                .addResourceLocations("classpath:/static/fonts/");
        registry.addResourceHandler("/css/**")
                .addResourceLocations("classpath:/static/css/");
        registry.addResourceHandler("/assets/**").
                addResourceLocations("classpath:/static/assets/");

    }

}
