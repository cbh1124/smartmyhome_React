package com.example.smartmyhome.service;


import com.example.smartmyhome.model.room.RoomEntity;
import com.example.smartmyhome.model.sensor.SensorEntity;
import com.example.smartmyhome.persistence.RoomRepository;
import com.example.smartmyhome.persistence.SensorRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class VideoService {

    @Autowired
    RoomRepository roomRepository;


    // CCTV rsname을 통해 서칭하기
    public Optional<RoomEntity> findsensorname(String rsname){

        return roomRepository.findByrsname(rsname);
    }


    // CCTV 갯수
    public Integer findcount(String rsname){
        return roomRepository.findBycountrsname(rsname);
    }

}
