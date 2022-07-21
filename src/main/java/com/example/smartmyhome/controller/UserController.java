package com.example.smartmyhome.controller;

import com.example.smartmyhome.constant.Role;
import com.example.smartmyhome.dto.MailDto;
import com.example.smartmyhome.dto.ResponseDTO;
import com.example.smartmyhome.dto.TodoDTO;
import com.example.smartmyhome.dto.UserDTO;
import com.example.smartmyhome.model.user.UserEntity;
import com.example.smartmyhome.security.TokenProvider;
import com.example.smartmyhome.service.SendMailService;
import com.example.smartmyhome.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.User;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.Option;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/auth")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private TokenProvider tokenProvider;

    @Autowired
    private SendMailService sendMailService;

    // Bean으로 작성해도됨
    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody UserDTO userDTO){

        try{
            // 요청을 이용해 저장할 사용자 만들기
            UserEntity user = UserEntity.builder()
                    .email(userDTO.getEmail())
                    .username(userDTO.getUsername())
                    //todo passwordEncoder
                    .password(passwordEncoder.encode(userDTO.getPassword()) )
                    .role(Role.NOTUSER)
                    .temporary("")
                    .build();
            // 서비스를 이용해 리포지터리에 사용자 저장
            UserEntity registeredUser = userService.create(user);

            // 저장된 레포지토리를 통해서 값 검증 후 반환 값 메시지 확인
            UserDTO responseUserDTO = UserDTO.builder()
                    .email(registeredUser.getEmail())
                    .id(registeredUser.getId())
                    .username(registeredUser.getUsername())
                    .role(Role.NOTUSER)
                    .build();
            // 유저 정보는 항상 하나이므로 그냥 리스트로 만들어야하는 ResponseDTO를 사용하지 않고 그냥 UserDTO리턴.
            return ResponseEntity.ok().body(responseUserDTO);
        }catch (Exception e){

            // 예외가 나는 경우 bad 리스폰스 리턴
            ResponseDTO responseDTO = ResponseDTO.builder().error(e.getMessage()).build();

            // 전달된 검증 값을 error 메시지만 담아서 responseDTO로 만들어 오류 값 반환
            return ResponseEntity
                    .badRequest()
                    .body(responseDTO);
        }
    }

    @PostMapping("/signin")
    public ResponseEntity<?> authenticate(@RequestBody UserDTO userDTO){

        try {
            UserEntity user = userService.getByCredentials(
                    userDTO.getEmail(),
                    userDTO.getPassword(),
                    passwordEncoder
            );
            /*만약 유저가 null이 아니라면 토큰 생성 */
            if(user != null && user.getRole() != Role.NOTUSER ) {
                // 토큰 생성
                final String token = tokenProvider.create(user);
                final UserDTO responseUserDTO = UserDTO.builder()
                        .email(user.getEmail())
                        .id(user.getId())
                        .token(token)
                        .role(user.getRole())
                        .build();
                return ResponseEntity.ok().body(responseUserDTO);
            }else{
                ResponseDTO responseDTO = ResponseDTO.builder()
                        .error("NOTUSER").build();
                return ResponseEntity.badRequest().body(responseDTO);
            }

        }catch (Exception e){
            ResponseDTO responseDTO = ResponseDTO.builder()
                    .error("Login failed").build();
            return ResponseEntity.badRequest().body(responseDTO);
        }

    }

    @GetMapping("/usermanage")
    public ResponseEntity<?> usermanage(@AuthenticationPrincipal String userId){

        try {
            List<UserEntity> entities =  userService.usermanage();
            List<UserDTO> dtos = entities.stream().map(UserDTO::new).collect(Collectors.toList());

            ResponseDTO<UserDTO> response = ResponseDTO.<UserDTO>builder().data(dtos).build();

            return ResponseEntity.ok().body(response);
        }catch (Exception e){
            String error = e.getMessage();
            ResponseDTO<UserDTO> response = ResponseDTO.<UserDTO>builder().error(error).build();
            // 반환할때는 ResponseEntity를 활용해서 상태코드,header,data를 나타내기 위해 사용한다.
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PutMapping("/useredit")
    public ResponseEntity<?> useredit(@AuthenticationPrincipal String userId, @RequestBody UserDTO userDTO){

        System.out.println(userDTO);
        UserEntity entity = UserDTO.toEntity(userDTO);
        System.out.println(entity);
        Optional<UserEntity> entities = userService.useredit(entity);

        List<UserDTO> dtos = entities.stream().map(UserDTO::new).collect(Collectors.toList());
        ResponseDTO<UserDTO> response = ResponseDTO.<UserDTO>builder().data(dtos).build();

        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping("/userdelete")
    public ResponseEntity<?> userdelete(@AuthenticationPrincipal String userId, @RequestBody UserDTO userDTO){
        try{
            UserEntity entity = UserDTO.toEntity(userDTO);

            Optional<UserEntity> entities = userService.userdelete(entity);

            List<UserDTO> dtos = entities.stream().map(UserDTO::new).collect(Collectors.toList());

            ResponseDTO<UserDTO> response = ResponseDTO.<UserDTO>builder().data(dtos).build();

            return ResponseEntity.ok().body(response);

        }catch (Exception e){
            String error = e.getMessage();
            ResponseDTO<UserDTO> response = ResponseDTO.<UserDTO>builder().error(error).build();
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/usersearch")
    public ResponseEntity<?> usersearch(@AuthenticationPrincipal String userId){
        try {
            if(userId != null){
                ResponseDTO<UserDTO> response = ResponseDTO.<UserDTO>builder().check(userService.idsearch(userId)).build();
                return ResponseEntity.ok().body(response);
            }else{
                ResponseDTO responseDTO = ResponseDTO.builder()
                        .error("NOT").build();
                return ResponseEntity.badRequest().body(responseDTO);
            }
        }catch (Exception e){
            ResponseDTO responseDTO = ResponseDTO.builder()
                    .error("NOT").build();
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

    @PostMapping("/adminsearch")
    public ResponseEntity<?> adminsearch(@AuthenticationPrincipal String userId){
        try{
            if(userId != null){
                ResponseDTO<UserDTO> response = ResponseDTO.<UserDTO>builder().check(userService.adminsearch(userId)).build();
                System.out.println(response);
                return ResponseEntity.ok().body(response);
            }else{
                ResponseDTO responseDTO = ResponseDTO.builder()
                        .error("NOT").build();
                return ResponseEntity.badRequest().body(responseDTO);
            }
        }catch(Exception e){
            ResponseDTO responseDTO = ResponseDTO.builder()
                    .error("NOT").build();
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

    @PostMapping("/useridcheck")
    public ResponseEntity<?> useridcheck(@RequestBody UserDTO userDTO){
        Boolean usernamecheck = userService.useridcheck(userDTO.getUsername());

        return  ResponseEntity.ok().body(usernamecheck);
    }

    @PostMapping("/emailcheck")
    public ResponseEntity<?> emailcheck(@RequestBody UserDTO userDTO){
        Boolean email = userService.emailcheck(userDTO.getEmail());

        return ResponseEntity.ok().body(email);
    }

    @PostMapping("/passwordfind")
    public ResponseEntity<?> passwordfind(@RequestBody UserDTO userDTO){
        Boolean email = userService.emailcheck(userDTO.getEmail());

        // 인증 코드 만들기
        Random random = new Random();
        StringBuilder authkey = new StringBuilder();
        for(int i = 0; i<12; i++){
            char randomchar = (char)(random.nextInt(26)+97); // 97~122
            authkey.append(randomchar);
        }

        // dto -> entity로 변환
        UserEntity entity = UserDTO.toEntity(userDTO);

        if(userService.emailvalidate(entity, authkey.toString())){
            // 해당 인증키를 entity로 서칭 후 해당 entity 수정하기
            MailDto maildto = MailDto.builder()
                    .address(userDTO.getEmail())
                    .title("임시 인증키 입니다.")
                    .content(authkey.toString())
                    .build();
            sendMailService.sendSimpleMessage(maildto);
            return ResponseEntity.ok().body(email);
        }else{
            return ResponseEntity.badRequest().body(false);
        }
    }

    @PostMapping("/temporary")
    public ResponseEntity<?> temporary(@RequestBody UserDTO userDTO){
        UserEntity entity = UserDTO.toEntity(userDTO);

        //  entity를 넣어서 entity의 값 수정 (인증번호를 통해서 entity 찾은후)
        // 찾은 인증번호를 통해서 해당 비밀번호를 변경한다.

        if(userService.temporary(entity)){
            ResponseDTO<UserDTO> response = ResponseDTO.<UserDTO>builder().check(userService.temporary(entity)).build();
            return ResponseEntity.ok().body(response);
        }else{
            ResponseDTO responseDTO = ResponseDTO.builder()
                    .error("NOT").build();
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

}