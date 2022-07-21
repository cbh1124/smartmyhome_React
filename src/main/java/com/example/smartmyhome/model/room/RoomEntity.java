package com.example.smartmyhome.model.room;


import com.example.smartmyhome.model.BaseTimeEntity;
import com.example.smartmyhome.model.sensor.SensorEntity;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name="Room")@ToString(exclude={"sensorEntities"})
public class RoomEntity extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer rnum;
    private String rname;
    private String rsname;
    private String rsaddress;
    @JsonManagedReference
    @OneToMany(mappedBy = "roomEntity", cascade = CascadeType.ALL)
    private List<SensorEntity> sensorEntities = new ArrayList<>();

}
