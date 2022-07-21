package com.example.smartmyhome.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ChartDTO {

    private String sdate;
    private String stemp;
    private String shumi;
}
