package com.example.smartmyhome;

import com.example.smartmyhome.model.sensor.SensorEntity;
import com.example.smartmyhome.persistence.RoomRepository;
import com.example.smartmyhome.persistence.SensorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.format.DateTimeFormatter;
import java.util.List;

@SpringBootApplication
@RestController
@EnableJpaAuditing
public class SmartmyhomeApplication {

    public static void main(String[] args) {
        SpringApplication.run(SmartmyhomeApplication.class, args);
    }

    @Autowired
    SensorRepository sensorRepository;

    @Autowired
    RoomRepository roomRepository;

    @PersistenceContext
    EntityManager em;

    @GetMapping("/hello")
    public String hello(@RequestParam(value = "name", defaultValue = "World") String name) {


//        List<SensorEntity> entityOptional = sensorRepository.findAll();
//
//        for(int i=0; i<entityOptional.size(); i++){
//            String date = entityOptional.get(i).getCreatedDate().format(DateTimeFormatter.ofPattern("yy-MM-dd"));
//            System.out.println(date);
//        }

        System.out.println(sensorRepository.findSensorEntityByNative().toString());
//        System.out.println(roomRepository.findRoomEntityByNative().toString());
        return String.format("Hello %s!", name);
    }
}
