package com.example.jpab01.controller;
// GET /member/login -> MemberController.loginGET() -> member/login.html */

/* 로그인이 필요한 페이지 접속 시 로그인 페이지로 돌아오는 과정 */
// (1) GET /board/register 요청
// (2) Spring Security "로그인 했나?" -> 아직 안 함
// (3) "그럼 로그인 페이지로 보내야겠다."
// (4) /member/login

/* 로그아웃을 했을 때 돌아오는 로그인 페이지로 돌아오는 과정 */
// (1) GET /logout 요청
// (2) Spring Security Filter Chain -> Logout Filter (formLogin()으로 로그아웃도 기본 처리 기능)
// (3) Logout Filter가 인증 정보 삭제, SecurityContext 정리, 로그아웃 처리
// (4) /member/login?logout (logout: 파라미터)
// (5) MemberController.loginGET() -> login.html

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/member")
@Log4j2
@RequiredArgsConstructor
public class MemberController {

    @GetMapping("/login")
    public void loginGET(String error, String logout) {
        log.info("login get.............");
        log.info("logout: " + logout);

        if(logout != null) {
            log.info("user logout......");
        }
    }
}