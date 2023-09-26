package com.bookend.login.domain.entity;

import com.bookend.login.domain.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;


/**
 * 소셜 로그인 시 반환된 사용자 정보를 저장할 Entity
 */
@Getter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    @Column
    private String picture;
    private String ip;
    private Date lastConDt;

    @Enumerated(EnumType.STRING) // Enum type의 상수를 데이터베이스에 문자열로 저장하도록 지정
    @Column(nullable = false)
    private Role role;

    // Todo ip, regDt
    @PrePersist
    protected void setLastConDt() {
        this.lastConDt = new Date();
    }

    public User update(String name, String picture) {
        this.name = name;
        this.picture = picture;
        return this;
    }

    public String getRoleKey() {
        return this.role.getKey();
    }
}


