package com.example.smartmyhome.dto;

import com.example.smartmyhome.model.sensor.SensorEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class TemphumDTO {
    private int snum;
    private String stemp;
    private String shum;
    private int rnum;

    public TemphumDTO(final SensorEntity entity){
        this.snum = entity.getSnum();
        this.stemp = entity.getStemp();
        this.shum = entity.getShum();
        this.rnum = entity.getRoomEntity().getRnum();
    }

    public static SensorEntity toEntity(final TemphumDTO dto){

        return SensorEntity.builder().
//                snum(dto.getSnum()).
                stemp(dto.getStemp()).
                shum(dto.getShum()).
                build();
    }


}
