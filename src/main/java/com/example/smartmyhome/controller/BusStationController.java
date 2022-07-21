package com.example.smartmyhome.controller;

import com.example.smartmyhome.dto.ResponseDTO;
import com.example.smartmyhome.dto.apidto.BusArrivedDTO;
import com.example.smartmyhome.dto.apidto.BusStationDTO;
import com.example.smartmyhome.service.BusStationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class BusStationController {

    @Autowired
    BusStationService busStationService;

    @GetMapping("/busstation")
    public ResponseEntity<?> Busstation(){
        ArrayList<BusStationDTO> busStationDTOS = new ArrayList<>();
        busStationDTOS = busStationService.getgpsstationlist(busStationService.getgpsstation());

        ResponseDTO<BusStationDTO> response = ResponseDTO.<BusStationDTO>builder().data(busStationDTOS).build();

        return ResponseEntity.ok().body(response);
    }
    @GetMapping("/buslocation")
    public ResponseEntity<?> bustlocation(){

        // 위치를 기준으로 정류장을 찾는다,
        ArrayList<BusStationDTO> busStationDTOS = new ArrayList<>();
        ArrayList<BusArrivedDTO> busArrivedDTOS = new ArrayList<>();
        List<List<BusArrivedDTO>> doubleArrayList = new ArrayList<>();
        busStationDTOS = busStationService.getgpsstationlist(busStationService.getgpsstation());

        // 버스 정류장 하나의 id당 버스 오는 정보 받아오기 버스정류장 18개 -> 18*n개
        for (BusStationDTO dto:busStationDTOS) {
            String citycode = dto.getCitycode();
            String nodeid = dto.getNodeid();
            busArrivedDTOS = busStationService
                                .getstationarrivelist(busStationService
                                        .getstationarrive(citycode, nodeid)
                                );
            doubleArrayList.add(busArrivedDTOS);
        }

        ResponseDTO<BusArrivedDTO> response = ResponseDTO.<BusArrivedDTO>builder().datalist(doubleArrayList).build();

        return ResponseEntity.ok().body(response);
    }
}
