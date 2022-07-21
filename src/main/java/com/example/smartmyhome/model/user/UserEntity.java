package com.example.smartmyhome.model.user;

import com.example.smartmyhome.constant.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user",uniqueConstraints = {@UniqueConstraint(columnNames = "email")})
public class UserEntity {
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name="system-uuid", strategy = "uuid")
    private String id; // 사용자에게 고유하게 부여되는 id

    @Column(nullable = false)
    private String username; // 사용자의 이름

    // 값을 false로 설정해 주면, 해당 필드는 DDL 생성 시 not null이라는 조건이 붙은 채로 생성된다.
    // https://kafcamus.tistory.com/15 참고
    @Column(nullable = false)
    private String email; // 사용자의 이메일, 아이디와 같은 역할을 한다.

    @Column(nullable = false)
    private String password; // 패스워드

    @Column(nullable = false) // 임시번호
    private String temporary;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;
}