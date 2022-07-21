package com.example.smartmyhome.controller;


import com.example.smartmyhome.dto.NoticeDTO;
import com.example.smartmyhome.dto.ResponseDTO;
import com.example.smartmyhome.dto.RoomDTO;
import com.example.smartmyhome.model.event.NoticeEntity;
import com.example.smartmyhome.service.NoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/notice")
public class NoticeController {


    @Autowired
    NoticeService service;


    // 공지사항 등록
    @PostMapping
    public ResponseEntity<?> regist(@AuthenticationPrincipal String userId, @RequestBody NoticeDTO noticeDTO){
        /// dto를 entity로 변환

        System.out.println(noticeDTO.getNtitle());
        NoticeEntity entity = NoticeDTO.toEntity(noticeDTO);

        // 서비스를 이용해서 entity 를 등록
        Optional<NoticeEntity> entities = service.getregist(entity);

        // 자바스트림을 이용해 리턴된 entity리스트를 dto로 변경
        List<NoticeDTO> dtos = entities.stream().map(NoticeDTO::new).collect(Collectors.toList());
        ResponseDTO<NoticeDTO> response = ResponseDTO.<NoticeDTO>builder().data(dtos).build();

        return ResponseEntity.ok().body(response);
    }

    // 공지사항 리스트 제공
    @GetMapping
    public ResponseEntity<?> getnotice(){

        List<NoticeEntity> entities = service.getlist();

        List<NoticeDTO> dtos = entities.stream().map(NoticeDTO::new).collect(Collectors.toList());

        ResponseDTO<NoticeDTO> response = ResponseDTO.<NoticeDTO>builder().data(dtos).build();

        return  ResponseEntity.ok().body(response);
    }

    // 공지사항 수정
    @PutMapping
    public ResponseEntity<?> getedit(@AuthenticationPrincipal String userId, @RequestBody NoticeDTO noticeDTO){
        NoticeEntity entity = NoticeDTO.toEntity(noticeDTO);

        // 서비스를 이용하여 entity 업데이트
        Optional<NoticeEntity> entities = service.getedit(entity);

        //자바 스트림을 이용해 엔티티리스트 dto로 변경
        List<NoticeDTO> dtos = entities.stream().map(NoticeDTO::new).collect(Collectors.toList());
        ResponseDTO<NoticeDTO> response = ResponseDTO.<NoticeDTO>builder().data(dtos).build();

        return ResponseEntity.ok().body(response);
    }

    // 공지사항 삭제
    @DeleteMapping
    public ResponseEntity<?> delete(@AuthenticationPrincipal String userId, @RequestBody NoticeDTO noticeDTO){
        try{
            NoticeEntity entity = NoticeDTO.toEntity(noticeDTO);

            Optional<NoticeEntity> entities = service.delete(entity);

            List<NoticeDTO> dtos = entities.stream().map(NoticeDTO::new).collect(Collectors.toList());

            ResponseDTO<NoticeDTO> response = ResponseDTO.<NoticeDTO>builder().data(dtos).build();

            return ResponseEntity.ok().body(response);

        }catch (Exception e){
            String error = e.getMessage();
            ResponseDTO<NoticeDTO> response = ResponseDTO.<NoticeDTO>builder().error(error).build();
            return ResponseEntity.badRequest().body(response);
        }
    }



}
