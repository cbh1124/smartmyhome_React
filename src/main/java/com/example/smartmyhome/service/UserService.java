package com.example.smartmyhome.service;

import com.example.smartmyhome.constant.Role;
import com.example.smartmyhome.dto.UserDTO;
import com.example.smartmyhome.model.todo.TodoEntity;
import com.example.smartmyhome.model.user.UserEntity;
import com.example.smartmyhome.persistence.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // Bean으로 작성해도됨
    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    private void validate(final UserEntity entity) {
        if(entity == null) {
            log.warn("Entity cannot be null.");
            throw new RuntimeException("Entity cannot be null.");
        }

        if(entity.getId() == null) {
            log.warn("Unknown user.");
            throw new RuntimeException("Unknown user.");
        }
    }

    public Optional<UserEntity> retrieve(final String userId){
        return userRepository.findById(userId);
    }

    @Transactional
    public UserEntity create(final UserEntity userEntity){
        if(userEntity == null || userEntity.getEmail() == null){
            throw new RuntimeException("Invalid arguments");
        }
        final String email = userEntity.getEmail();

        if(userRepository.existsByEmail(email)){
            log.warn("Email already exists {}", email);
            throw new RuntimeException("Email already exists");
        }
        return userRepository.save(userEntity);
    }

    /*순서  이메일과 비밀번호 전송 -> 전달받은 이메일을 통해서 해당이메일의 정보가 존재 하는지 안하는지 확인 ->
     *      이메일을 통해 서칭한 정보가 null값이 아니라면 이메일은 존재하는거임 ->
     *      따라서 담긴 originalUser에는 비밀번호도 담겨있으므로 해당 비밀번호와 전달받은 비밀번호를 비교후 일치여부 확인
     *      일치하면 일치한 값을 return
     * */
    public UserEntity getByCredentials(final String email, final String password, final PasswordEncoder encoder){
        // UserRepository에 있는 이메일 값을 서칭 후 같은게 있다면 originalUser로 담는다.

        final UserEntity originalUser = userRepository.findByEmail(email);

        // matches 메서드를 이용해 패스워드가 같은지 확인
        if(originalUser != null && encoder.matches(password, originalUser.getPassword())){
            return originalUser;
        }

        return null;
    }


    public Boolean idsearch(final String userId){

        if(userRepository.findById(userId).isPresent()){
            System.out.println(userRepository.findById(userId));
            return true;
        }else{
            return false;
        }
    }

    public Boolean adminsearch(final String userId){
       final Optional<UserEntity> entity = userRepository.findById(userId);

//       entity.ifPresent(user ->{
//
//       });
        if(!entity.isPresent()){ // 만약에 entity가 존재하지 않는다면
            return false;
        }else{ // 만약에 entity가 존재한다면
           if(entity.get().getRole() == Role.ADMIN){ // 만약에 admin이라면 true
               return true;
           }else{                                   // 그게 아니라면 false
               return false;
           }
        }
    }

    public Boolean useridcheck(final String username){
        return userRepository.existsByUsername(username);
    }

    public Boolean emailcheck(final String email){
        return userRepository.existsByEmail(email);
    }

    public List<UserEntity> usermanage(){
        List<UserEntity> entities = userRepository.findAll();
        System.out.println(entities.get(0).getEmail());
        return entities;
    }
    @Transactional
    public Optional<UserEntity> useredit(final UserEntity entity){

        validate(entity);

        final Optional<UserEntity> original = userRepository.findById(entity.getId());

        original.ifPresent(user ->{
            user.setId(entity.getId());
            user.setRole(entity.getRole());

            userRepository.save(user);
        });

        return retrieve(entity.getId());
    }

    public Optional<UserEntity> userdelete(final UserEntity userEntity){

        validate(userEntity);

        try {
            userRepository.delete(userEntity);
        }catch (Exception e){
            log.error("error deleting entity ", userEntity.getId(), e);

            throw new RuntimeException("error deleting entity" + userEntity.getId());
        }

        return retrieve(userEntity.getId());
    }

    // 임시 비밀번호 서칭하기
    @Transactional
    public Boolean emailvalidate(UserEntity userEntity, String temporary){

        // 제공받은 entity를 통해 찾고자하는 entity를 찾은 후 ->
        // 해당 entity에 값을 넣어서 업데이트 한 후 save
        final UserEntity entity = userRepository.findByEmail(userEntity.getEmail());

        if(entity != null){
            entity.setTemporary(temporary);
            userRepository.save(entity);
            return true;
        }else{
            return false;
        }
    }

    public Boolean temporary(UserEntity userEntity){

        final Optional<UserEntity> entity  = userRepository.findByTemporary(userEntity.getTemporary());

        if(userRepository.findByTemporary(userEntity.getTemporary()).isPresent()){

            entity.ifPresent(user->{
                user.setPassword(passwordEncoder.encode(userEntity.getPassword()));
                userRepository.save(user);
            });

            return true;
        }else{
            return false;
        }
    }
}