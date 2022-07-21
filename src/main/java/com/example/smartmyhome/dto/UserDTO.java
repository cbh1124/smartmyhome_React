package com.example.smartmyhome.dto;

import com.example.smartmyhome.constant.Role;
import com.example.smartmyhome.model.todo.TodoEntity;
import com.example.smartmyhome.model.user.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Optional;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private String token;
    private String email;
    private String username;
    private String password;
    private String id;
    private String temporary;
    private Role role;
    private boolean check;

//    public UserDTO(final Optional<UserEntity> entity){
//        this.id = String.valueOf(entity);
//    }

    public UserDTO(final UserEntity userEntity) {
        this.id = userEntity.getId();
        this.email = userEntity.getEmail();
        this.username = userEntity.getUsername();
        this.role = userEntity.getRole();
    }

    public static UserEntity toEntity(final UserDTO dto){
        return UserEntity.builder()
                .id(dto.getId())
                .email(dto.getEmail())
                .password(dto.getPassword())
                .username(dto.getUsername())
                .temporary(dto.getTemporary())
                .role(dto.getRole())
                .build();
    }
}
