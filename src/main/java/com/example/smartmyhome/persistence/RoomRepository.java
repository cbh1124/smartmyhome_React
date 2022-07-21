package com.example.smartmyhome.persistence;

import com.example.smartmyhome.model.room.RoomEntity;
import com.example.smartmyhome.model.sensor.SensorEntity;
import com.example.smartmyhome.model.todo.TodoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Repository
public interface RoomRepository extends JpaRepository<RoomEntity, Integer> {

    Optional<RoomEntity> findByrnum(Integer rnum);
    Optional<RoomEntity> findByrname(String rname);
    Optional<RoomEntity> findByrsname(String rsname);
//    List<RoomEntity> findByrnum(Integer rnum);
//    @Query(value ="select * from smarthome.room sc group by sc.rnum", nativeQuery = true)
//    List<SensorEntity> findRoomEntityByNative();
//    Optional<RoomEntity> findByrsname(String rname);
//    long countByRsname(String name);

    @Query(value="select count(*) from smarthome.room sc  where rsname like %:rsname%", nativeQuery = true)
    Integer findBycountrsname(String rsname);
}
