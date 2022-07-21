package com.example.smartmyhome.service;


import com.example.smartmyhome.dto.UserDTO;
import com.example.smartmyhome.model.room.RoomEntity;
import com.example.smartmyhome.model.todo.TodoEntity;
import com.example.smartmyhome.persistence.RoomRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class UserSensorService {
    @Autowired
    private RoomRepository roomRepository;

    public List<RoomEntity> getlist(){
        return roomRepository.findAll();
    }

    public Optional<RoomEntity> retrieve(final Integer rnum){
        return roomRepository.findByrnum(rnum);
    }

    private void validate(final RoomEntity roomEntity) {
        if(roomEntity == null) {
            log.warn("Entity cannot be null.");
            throw new RuntimeException("Entity cannot be null.");
        }

        if(roomEntity.getRname() == null) {
            log.warn("Unknown user.");
            throw new RuntimeException("Unknown user.");
        }
    }
    @Transactional
    public Optional<RoomEntity> getregist(final RoomEntity roomEntity){

        validate(roomEntity);

        roomRepository.save(roomEntity);
        log.info("Entity roomnum : {} is saved", roomEntity.getRnum());
        return roomRepository.findByrnum(roomEntity.getRnum());
    }
    @Transactional
    public Optional<RoomEntity> getedit(final RoomEntity roomEntity){
        validate(roomEntity);

        final Optional<RoomEntity> original = roomRepository.findByrnum(roomEntity.getRnum());

        original.ifPresent(room ->{
            room.setRsaddress(roomEntity.getRsaddress());
            room.setRname(roomEntity.getRname());
            room.setRsname(roomEntity.getRsname());

            roomRepository.save(room);
        });

        return retrieve(roomEntity.getRnum());
    }

    public Optional<RoomEntity> delete(final RoomEntity roomEntity){
        validate(roomEntity);

        try{
            roomRepository.delete(roomEntity);
        }catch (Exception e){
            log.error("error deleting entity ", roomEntity.getRnum(), e);

            throw new RuntimeException("error deleting entity" + roomEntity.getRnum());
        }

        return retrieve(roomEntity.getRnum());
    }
}
