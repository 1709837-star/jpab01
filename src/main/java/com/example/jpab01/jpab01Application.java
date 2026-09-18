package com.example.jpab01;
/* 자바의 main() 메서드와 같은 역할 */
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
//스프링 부트 사용에 필요한 기본 설정을 해주는 애너테이션
public class jpab01Application {

    public static void main(String[] args) {

        SpringApplication.run(jpab01Application.class, args);
    }

}
