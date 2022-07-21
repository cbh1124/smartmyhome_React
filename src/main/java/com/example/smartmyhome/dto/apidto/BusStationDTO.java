package com.example.smartmyhome.dto.apidto;


import lombok.*;
import org.json.simple.JSONObject;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
public class BusStationDTO {
    private String citycode;
    private String gpslati;
    private String gpslong;
    private String nodeid;
    private String nodenm;
    private String nodeno;
}
