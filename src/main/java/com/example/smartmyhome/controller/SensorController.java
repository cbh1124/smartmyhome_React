package com.example.smartmyhome.controller;


import com.example.smartmyhome.dto.ChartDTO;
import com.example.smartmyhome.dto.ResponseDTO;
import com.example.smartmyhome.dto.RoomDTO;
import com.example.smartmyhome.dto.TemphumDTO;
import com.example.smartmyhome.model.room.RoomEntity;
import com.example.smartmyhome.model.sensor.SensorEntity;
import com.example.smartmyhome.service.ApiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("api")
public class SensorController {

    @Autowired
    ApiService apiService;

//    @PostMapping("/myroom")
//    public ResponseEntity<?> tempHumi(@RequestBody TemphumDTO dto){
//        try{
//            System.out.println(dto);
//            SensorEntity entity = TemphumDTO.toEntity(dto);
//            System.out.println(dto);
//            List<SensorEntity> entities = new ArrayList<>();
//            entity = apiService.temphum(entity);
//            entities.add(entity);
////            List<SensorEntity> entities = (List<SensorEntity>) apiService.temphum(entity);
//            System.out.println(dto);
//            List<TemphumDTO> dtos = entities.stream().map(TemphumDTO::new).collect(Collectors.toList());
//            System.out.println(dto);
//            ResponseDTO<TemphumDTO> response = ResponseDTO.<TemphumDTO>builder().data(dtos).build();
//            System.out.println(dto);
//            return ResponseEntity.ok().body(response);
//        }catch (Exception e){
//            String error = e.getMessage();
//            ResponseDTO<TemphumDTO> response = ResponseDTO.<TemphumDTO>builder().error(error).build();
//            return ResponseEntity.badRequest().body(response);
//        }
//    }

    @PostMapping("/myroom")
    public ResponseEntity<?> test(@RequestBody TemphumDTO dto){
        try{
            apiService.sensorfilter(dto);
            SensorEntity entity = TemphumDTO.toEntity(dto);
            Optional<RoomEntity> roomEntity = apiService.findtemphum(dto);

            SensorEntity sensorEntity = SensorEntity.builder()
                    .stemp(dto.getStemp())
                    .shum(dto.getShum())
                    .roomEntity(roomEntity.get())
                    .build();

            List<SensorEntity> entities = new ArrayList<>();
            entity = apiService.temphum(sensorEntity);
            entities.add(entity);

            List<TemphumDTO> dtos = entities.stream().map(TemphumDTO::new).collect(Collectors.toList());
            ResponseDTO<TemphumDTO> response = ResponseDTO.<TemphumDTO>builder().data(dtos).build();

            return ResponseEntity.ok().body(response);
        }catch (Exception e){
            String error = e.getMessage();
            ResponseDTO<TemphumDTO> response = ResponseDTO.<TemphumDTO>builder().error(error).build();
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/room")
    public ResponseEntity<?> roomcreate(@RequestBody RoomDTO dto){
        try{
            RoomEntity entity = RoomDTO.toEntity(dto);
            List<RoomEntity> entities = new ArrayList<>();
            entity = apiService.createroom(entity);
            entities.add(entity);
            List<RoomDTO> dtos = entities.stream().map(RoomDTO::new).collect(Collectors.toList());
            ResponseDTO<RoomDTO> response = ResponseDTO.<RoomDTO>builder().data(dtos).build();
            return ResponseEntity.ok().body(response);

        }catch (Exception e){
            String error = e.getMessage();
            ResponseDTO<RoomDTO> response = ResponseDTO.<RoomDTO>builder().error(error).build();
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/temp")
    public String gettempsmoothing( String id){
        return String.format("%.2f", apiService.gettempsmoothing());
    }

    @GetMapping("/humi")
    public String gethumismoothing( String id){
        return String.format("%.2f", apiService.gethumismoothing());
    }

    @GetMapping("/minichart")
    public List<ChartDTO> recentchart( String id){
        // 최근 5개의 값 가져오기
        return apiService.findSensorEntityByNative();
    }

    @GetMapping("/realchart")
    public ChartDTO getrealchart( String id){
        return apiService.lastSensorEntity();
    }

    @GetMapping("/daychart")
    public List<ChartDTO> getdaychart( String id){
        return apiService.finddaychart();
    }

    @GetMapping("/hourchart")
    public List<ChartDTO> gethourchart( String id){
        return apiService.findhourchart();
    }

    @GetMapping("/sensorlist")
    public String getsensorlist( String id){

        return "1";
    }

    @GetMapping("/location")
    public String getLocation(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null) {
            ip = request.getHeader("Proxy-Client-IP");
            log.info(">>>> Proxy-Client-IP : " + ip);
        }
        if (ip == null) {
            ip = request.getHeader("WL-Proxy-Client-IP"); // 웹로직
            log.info(">>>> WL-Proxy-Client-IP : " + ip);
        }
        if (ip == null) {
            ip = request.getHeader("HTTP_CLIENT_IP");
            log.info(">>>> HTTP_CLIENT_IP : " + ip);
        }
        if (ip == null) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
            log.info(">>>> HTTP_X_FORWARDED_FOR : " + ip);
        }
        if (ip == null) {
            ip = request.getRemoteAddr();
        }

        log.info(">>>> Result : IP Address : "+ip);

        return ip;

    }
}
