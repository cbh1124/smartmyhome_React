package com.example.smartmyhome.persistence;

import com.example.smartmyhome.model.room.RoomEntity;
import com.example.smartmyhome.model.sensor.SensorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SensorRepository extends JpaRepository<SensorEntity, Integer> {

    @Query(value ="select * from smarthome.sensor sc group by date_format(sc.created_date, '%Y-%m-%d-%H')", nativeQuery = true)
    List<SensorEntity> findSensorEntityByNative();

    SensorEntity findTop1ByOrderBySnumDesc();

}
