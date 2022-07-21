package com.example.smartmyhome.model.sensor;


import com.example.smartmyhome.dto.TemphumDTO;
import com.example.smartmyhome.model.BaseTimeEntity;
import com.example.smartmyhome.model.room.RoomEntity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;
import org.jetbrains.annotations.NotNull;

import javax.persistence.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name="Sensor")
public class SensorEntity extends BaseTimeEntity {
    @Id// 기본키
    @GeneratedValue(strategy = GenerationType.IDENTITY)// auto key
    private Integer snum;
    private String stemp;
    private String shum;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name="rnum")
    private RoomEntity roomEntity;

    
}
