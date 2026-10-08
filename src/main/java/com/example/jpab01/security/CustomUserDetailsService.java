package com.example.jpab01.security;
/* 로그인한 사용자의 정보를 가져오는 담당자 */
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/* UserDetailService : Spring Security가 제공하는 인터페이스 */
@Log4j2
@Service
//@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private PasswordEncoder passwordEncoder; // CustomSecurityConfig의 메서드 : 비밀번호 암호화

    public CustomUserDetailsService() {
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    /* 사용자가 로그인할 때 Spring Security가 호출하는 메서드 */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        log.info("loadUserByUsername: " + username);

        // UserDetails : 사용자 정보 규격 -> 이러한 정보를 수집
        // .builder() : User 객체를 만듦. 데이터베이스에서 가져오는 게x (테스트용)
        UserDetails userDetails = User.builder()
                .username("user2")
                .password(passwordEncoder.encode("1111"))
                .authorities("ROLE_USER") // -> BoardController에서 @PreAuthorize("hasRole('USER')") 사용 가능
                .build();

        return userDetails;
    }

}