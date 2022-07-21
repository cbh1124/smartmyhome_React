package com.example.smartmyhome.controller;


import com.example.smartmyhome.dto.ResponseDTO;
import com.example.smartmyhome.dto.RoomDTO;
import com.example.smartmyhome.dto.UserDTO;
import com.example.smartmyhome.model.room.RoomEntity;
import com.example.smartmyhome.model.user.UserEntity;
import com.example.smartmyhome.service.UserSensorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/user")
public class UserSensorController {

    @Autowired
    UserSensorService userSensorService;

    @GetMapping("/sensorlist")
    public ResponseEntity<?> getsensorlist(){

        List<RoomEntity> entities = userSensorService.getlist();

        List<RoomDTO> dtos = entities.stream().map(RoomDTO::new).collect(Collectors.toList());

        ResponseDTO<RoomDTO> response = ResponseDTO.<RoomDTO>builder().data(dtos).build();

        return ResponseEntity.ok().body(response);
    }

//    @PostMapping("/edit")
//    public ResponseEntity<?> editsensor(@AuthenticationPrincipal String id, UserDTO userDTO){
//
////        List<RoomEntity> entities = userSensorService.
//    }

    @PostMapping("/regist")
    public ResponseEntity<?> getregist(@AuthenticationPrincipal String userId, @RequestBody RoomDTO roomDTO ){
        // dto를 entity로 변환
        RoomEntity entity = RoomDTO.toEntity(roomDTO);

        // 서비스를 이용해서 entity 등록
        Optional<RoomEntity> entities = userSensorService.getregist(entity);

        // 자바 스트림을 이용해 리턴된 엔티티 리스트를 dto로 변경
        List<RoomDTO> dtos = entities.stream().map(RoomDTO::new).collect(Collectors.toList());
        ResponseDTO<RoomDTO> response = ResponseDTO.<RoomDTO>builder().data(dtos).build();
        return ResponseEntity.ok().body(response);
    }

    @PutMapping("/edit")
    public ResponseEntity<?> getedit(@AuthenticationPrincipal String userId, @RequestBody RoomDTO roomDTO){
        // dto를 entity로 변환
        RoomEntity entity = RoomDTO.toEntity(roomDTO);

        // 서비스를 이용하여 entity 업데이트
        Optional<RoomEntity> entities = userSensorService.getedit(entity);

        // 자바 스트림을 이용해 앤티티리스트를 dto로 변경
        List<RoomDTO> dtos = entities.stream().map(RoomDTO::new).collect(Collectors.toList());
        ResponseDTO<RoomDTO> response = ResponseDTO.<RoomDTO>builder().data(dtos).build();

        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> delete(@AuthenticationPrincipal String userid, @RequestBody RoomDTO roomDTO){
        try {
            // dto를 entity로 변환
            RoomEntity entity = RoomDTO.toEntity(roomDTO);

            Optional<RoomEntity> entities = userSensorService.delete(entity);

            List<RoomDTO> dtos = entities.stream().map(RoomDTO::new).collect(Collectors.toList());

            ResponseDTO<RoomDTO> response = ResponseDTO.<RoomDTO>builder().data(dtos).build();

            return ResponseEntity.ok().body(response);

        }catch (Exception e){
            String error = e.getMessage();
            ResponseDTO<RoomDTO> response = ResponseDTO.<RoomDTO>builder().error(error).build();
            return ResponseEntity.badRequest().body(response);
        }
    }
}
