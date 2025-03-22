package com.practice.entity;

import jakarta.persistence.*;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "roleName")
    private String roleName; // 예: "ROLE_ADMIN", "ROLE_USER"

    //User 테이블과 join
    @ManyToMany(mappedBy = "roles")
    private Set<User> users = new HashSet<>();


    // RoleSeeder 클래스를 통해 Role 미리 넣어두기
    // 역할(Role)을 만들 때 roleName 을 바로 지정할 수 있도록 하는 생성자
    public Role(String roleName) {
        this.roleName = roleName;
    }
}
