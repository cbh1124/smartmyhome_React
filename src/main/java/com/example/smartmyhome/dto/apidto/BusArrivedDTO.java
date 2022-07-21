package com.example.smartmyhome.dto.apidto;

import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
public class BusArrivedDTO {
    private String arrprevstationcnt;
    private String arrtime;
    private String nodeid;
    private String nodenm;
    private String routeid;
    private String routeno;
    private String routetp;
    private String vehicletp;
}
