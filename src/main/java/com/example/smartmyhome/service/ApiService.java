package com.example.smartmyhome.service;

import com.example.smartmyhome.dto.ChartDTO;
import com.example.smartmyhome.dto.TemphumDTO;
import com.example.smartmyhome.model.room.RoomEntity;
import com.example.smartmyhome.model.sensor.QSensorEntity;
import com.example.smartmyhome.model.sensor.SensorEntity;
import com.example.smartmyhome.persistence.RoomRepository;
import com.example.smartmyhome.persistence.SensorRepository;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.ConstantImpl;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringTemplate;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ApiService {
    @Autowired
    private SensorRepository sensorRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Transactional
    public SensorEntity temphum(final SensorEntity sensorentity){
        if(sensorentity == null || sensorentity.getStemp() == null || sensorentity.getShum() == null){
            throw new RuntimeException("Invalid arguments");
        }

        if(sensorentity.getStemp() == "nan" || sensorentity.getShum() == "nan" ){
            throw new RuntimeException("Invalid arguments");
        }

        return sensorRepository.save(sensorentity);
    }

    public RoomEntity createroom(final RoomEntity roomEntity){

//        if(roomEntity == null || roomEntity.getRnum() == null || roomEntity.getRname() == null ){
//            System.out.println(roomEntity);
//            throw new RuntimeException("Invalid arguments");
//
//        }

        return roomRepository.save(roomEntity);
    }


    public Optional<RoomEntity> findtemphum(final TemphumDTO temphumDTO){
        // dto의 rnum값  즉,  pk -> fk  참조하려는 fknum값의 엔티티를 가져온다.

        Optional<RoomEntity> roomEntity = roomRepository.findByrnum(temphumDTO.getRnum());

        return roomEntity;
    }

    // 센서로부터 들어오는 오류 값(nan) 잡아내기
    public boolean sensorfilter(final TemphumDTO temphumDTO){
        Double filtertemp = Double.parseDouble(temphumDTO.getStemp());
        Double filterhum =  Double.parseDouble(temphumDTO.getShum());

        if(filtertemp.isNaN() || filterhum.isNaN()){
            System.out.println("nan 값 오류 체크");
            return false;
        }
        return true;
    }

    // 센서값 평균내기
    public Double gettempsmoothing(){

        Optional<RoomEntity> roomEntity = roomRepository.findByrnum(4);

        List<SensorEntity> sensorEntity = roomEntity.get().getSensorEntities();
        Double total = 0.0;
        Double avg = 0.0;

        for(int i = sensorEntity.size()-9; i<sensorEntity.size(); i++){
            total = Double.parseDouble(sensorEntity.get(i).getStemp()) + total;
            avg = total/9.0;
        }

        return avg;
    }

    // 센서값 평균내기
    public Double gethumismoothing(){
        Optional<RoomEntity> roomEntity = roomRepository.findByrnum(4);
        List<SensorEntity> sensorEntity = roomEntity.get().getSensorEntities();
        Double total = 0.0;
        Double avg = 0.0;
        for(int i = sensorEntity.size()-9; i<sensorEntity.size(); i++){
            total = Double.parseDouble(sensorEntity.get(i).getShum()) + total;
            avg = total/9.0;
        }
        return avg;
    }

    @PersistenceContext
    EntityManager em;

    // 레포지토리 정리
    public List<ChartDTO> findSensorEntityByNative(){

        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        QSensorEntity qSensorEntity = QSensorEntity.sensorEntity;

        StringTemplate formattedDate = Expressions.stringTemplate( "DATE_FORMAT( {0}, {1} )",qSensorEntity.createdDate, ConstantImpl.create("%Y%m%d%H") );
        JPAQuery<SensorEntity> query = queryFactory.select(qSensorEntity).from(qSensorEntity)
                .groupBy(formattedDate);

        JPAQuery<Tuple> result = queryFactory.select(formattedDate, qSensorEntity.stemp.castToNum(Double.class).avg() ,qSensorEntity.shum.castToNum(Double.class).avg())
                .from(qSensorEntity)
                .groupBy(formattedDate);

        List<SensorEntity> sensorEntities = query.fetch();
        List<Tuple> sensorEntities2 = result.fetch();
        List<ChartDTO> chartdto = new ArrayList<>();

        for(int i = sensorEntities2.size()-5; i<sensorEntities2.size(); i++){
            String date = sensorEntities2.get(i).toString().split(",")[0].split("\\[")[1];
            String stemp = sensorEntities2.get(i).toString().split(",")[1];
            String shumi = sensorEntities2.get(i).toString().split(",")[2].split("\\]")[0];

            ChartDTO chartDTO = new ChartDTO(date, stemp, shumi);
            chartdto.add(chartDTO);
        }
        // 값을 가져와서 json으로 바꿔서 배포해야됨 createddate는 시간으로 변환 -> 각 시간별 평균 값
        return chartdto;
    }

    public ChartDTO lastSensorEntity(){
        SensorEntity entity =  sensorRepository.findTop1ByOrderBySnumDesc();
        String s = entity.getModifiedDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        ChartDTO chartDTO = new ChartDTO( s , entity.getStemp(), entity.getShum());
        return chartDTO;
    }

    public List<ChartDTO> finddaychart(){
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        QSensorEntity qSensorEntity = QSensorEntity.sensorEntity;
        StringTemplate formattedDate = Expressions.stringTemplate( "DATE_FORMAT( {0}, {1} )",qSensorEntity.createdDate, ConstantImpl.create("%Y-%m-%d") );

        JPAQuery<Tuple> result = queryFactory.select(formattedDate, qSensorEntity.stemp.castToNum(Double.class).avg() ,qSensorEntity.shum.castToNum(Double.class).avg())
                .from(qSensorEntity)
                .groupBy(formattedDate);

        List<Tuple> sensorEntities = result.fetch();
        List<ChartDTO> chartdto = new ArrayList<>();

        for(int i = 0; i<sensorEntities.size(); i++){
            String date = sensorEntities.get(i).toString().split(",")[0].split("\\[")[1];
            String stemp = sensorEntities.get(i).toString().split(",")[1];
            String shumi = sensorEntities.get(i).toString().split(",")[2].split("\\]")[0];

            ChartDTO chartDTO = new ChartDTO(date, stemp, shumi);
            chartdto.add(chartDTO);
        }
        // 값을 가져와서 json으로 바꿔서 배포해야됨 createddate는 시간으로 변환 -> 각 시간별 평균 값
        return chartdto;
    }

    public List<ChartDTO> findhourchart(){
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        QSensorEntity qSensorEntity = QSensorEntity.sensorEntity;
        StringTemplate formattedDate = Expressions.stringTemplate( "DATE_FORMAT( {0}, {1} )",qSensorEntity.createdDate, ConstantImpl.create("%Y-%m-%d %H:%m") );

        JPAQuery<Tuple> result = queryFactory.select(formattedDate, qSensorEntity.stemp.castToNum(Double.class).avg() ,qSensorEntity.shum.castToNum(Double.class).avg())
                .from(qSensorEntity)
                .groupBy(formattedDate);

        List<Tuple> sensorEntities = result.fetch();
        List<ChartDTO> chartdto = new ArrayList<>();

        for(int i = 0; i<sensorEntities.size(); i++){
            String date = sensorEntities.get(i).toString().split(",")[0].split("\\[")[1];
            String stemp = sensorEntities.get(i).toString().split(",")[1];
            String shumi = sensorEntities.get(i).toString().split(",")[2].split("\\]")[0];

            ChartDTO chartDTO = new ChartDTO(date, stemp, shumi);
            chartdto.add(chartDTO);
        }
        // 값을 가져와서 json으로 바꿔서 배포해야됨 createddate는 시간으로 변환 -> 각 시간별 평균 값
        return chartdto;
    }
}
