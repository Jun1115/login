package com.practice.jwt;

import com.practice.dto.CustomUserDetailsDTO;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;


public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JWTUtil jwtUtil;

    public LoginFilter(AuthenticationManager authenticationManager, JWTUtil jwtUtil){
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException{
        String username = obtainUsername(request);
        String password = obtainPassword(request);

        System.out.println(username + "로그인 시도");

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password, null);

        return authenticationManager.authenticate(authToken);
    }


    // 로그인 성공 시
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication){
        //UserDetailsS
        CustomUserDetailsDTO customUserDetailsDTO = (CustomUserDetailsDTO) authentication.getPrincipal();

        String username = customUserDetailsDTO.getUsername();


        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        // 권한을 담을 리스트
        List<String> role = new ArrayList<>();

        // 권한을 모두 추가
        for (GrantedAuthority auth : authorities) {
            role.add(auth.getAuthority());  // 각 권한을 roles 리스트에 추가
        }

        // 토큰 생성
        // 권한은 , 기준으로 확인 사용자마다 가지고 있는 권한이 여러개라서
        String token = jwtUtil.createJwt(username, String.join(",", role), 60*60*10L);

        // http 인증 방식은 RFC 7235 정의에 따라 인증 헤더 형태를 가져야 함
        response.addHeader("Authorization", "Bearer " + token);
    }


    // 로그인 실패 시
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) {
        response.setStatus(401);
    }
}
