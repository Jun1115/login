package com.practice.service;

import com.practice.dto.JoinDTO;
import com.practice.entity.User;
import com.practice.entity.Role;
import com.practice.repository.RoleRepository;
import com.practice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor // private final이 붙은 필드를 자동으로 생성자에 추가해 줌

public class JoinService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public void joinProcess(JoinDTO joinDTO){

        String username = joinDTO.getUsername();
        String password = joinDTO.getPassword();

        boolean isExist = userRepository.existsByUsername(username); // DB 확인 후 중복된 이름 있는지 확인하여 불린 값으로 리턴
        if (isExist) {
            System.out.println("이미 생성된 아이디가 존재합니다.");
            return ;
        }

        Role userRole = roleRepository.findByRoleName("ROLE_USER")// 기본적으로 ROLE_USER 역할 부여
                .orElseThrow(() -> new RuntimeException("ROLE_USER 가 존재하지 않습니다.")); // 근데 RoleSeeder 클래스를 통해 역할 미리 넣어놨음


        //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
        Role adminRole = roleRepository.findByRoleName("ROLE_ADMIN")
                .orElseThrow(() -> new RuntimeException("ROLE_USER 가 존재하지 않습니다.")); // 근데 RoleSeeder 클래스를 통해 역할 미리 넣어놨음
        // ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ

        User data = new User();

        data.setUsername(username);
        data.setPassword(passwordEncoder.encode(password));
        data.addRole(userRole);

        //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
        data.addRole(adminRole);
        //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ

        userRepository.save(data);
    }
}
