package com.example.jpab01.config;
/* Spring Security의 전체 보안 설정을 담당 */
// "우리 홈페이지에 누가 들어올 수 있고, 로그인은 어떻게 하고, 비밀번호는 어떻게 저장하고, CSS/JS같은 파일은 보안 검사를 할지 말지"
import com.example.jpab01.security.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.security.autoconfigure.web.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.sql.DataSource;

@Log4j2
@Configuration
@RequiredArgsConstructor
@EnableMethodSecurity // 메서드 단위로 권한 검사할 수 있게 해주는 설정 ex) @PreAuthorize("isAuthenticated()")
public class CustomSecurityConfig {

    private final DataSource dataSource; // 연결된 database 주입받기
    private final CustomUserDetailsService userDetailsService; // 사용자 정보를 찾아서 UserDetails로 만들어 주는 것


    /* PasswordEncoder : 비밀번호 암호화 설정 */
    @Bean
    public PasswordEncoder passwordEncoder() {

        return new BCryptPasswordEncoder();
        // 사용자가 1234 입력 -> BCrypt가 $2a$10$...로 DB에 저장
        // 로그인 할 때 1234 다시 입력하면 BCrypt가 저장된 값과 비교해서 확인하는 방식
    }


    /* filterChain : 커스텀 로그인(로그인/인증/인가와 관련된 보안 필터 설정) */
    // 브라우저가 HTTP요청 -> Spring Security Filter들이 인증/권한 검사 -> Controller
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        log.info("------------configure-------------------");

        http.formLogin(form -> {
        // 아무것도 안 적으면 기본 설정 사용 : "Spring Security의 Form Login 방식으로 로그인 기능을 사용하겠다."
        // -> login으로 접근했을 때 Spring Security의 기본 로그인 화면이 나옴
            form.loginPage("/member/login");
            // "적어주면 로그인할 때 기본 로그인 페이지 말고 이 url을 사용해"
            // 자동으로 기본 Logout 처리도 함께 제공
        });

        http.csrf(csrf -> csrf.disable());
        // CSRF 토큰 비활성화 : "Spring Security의 CSRF 보호 기능을 끄겠다."

        /* Spring Security의 Remember-Me 자동 로그인 기능을 설정하는 부분 */
        http.rememberMe(remember ->
                remember
                        .key("12345678")
                        .tokenRepository(persistentTokenRepository()) // 밑에서 만든 저장소를 연결
                        .userDetailsService(userDetailsService) // 사용자 정보 필요하니까, UserDetails로 연결
                        .tokenValiditySeconds(60 * 60 * 24 * 30) // 토큰 유효기간
        ); // 로그인 기억 쿠키 발행 설정

        return http.build(); // 지금까지 설정한 Security 설정을 완성
    }

    /* 지속되는 토큰 저장소 : 자동 로그인 정보를 DB에 저장하는 핵심 */
    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl repo = new JdbcTokenRepositoryImpl(); // JDBC : 자바에서 DB에 접근하는 기술
        repo.setDataSource(dataSource); // "위에서 선언한, 연결된 DB를 사용해."
        return repo;
    }

    /* webSecurityCustomizer : CSS, JS등 정적 파일은 로그인 검사할 필요X -> Security 필터에서 제외 */
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {

        log.info("------------web configure-------------------");

        return (web) ->
                web.ignoring().requestMatchers(
                        PathRequest.toStaticResources().atCommonLocations());
    }
}