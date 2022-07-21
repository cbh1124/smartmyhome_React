package com.example.smartmyhome.dto;


import com.example.smartmyhome.model.room.RoomEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class RoomDTO {
    private Integer rnum;
    private String rname;
    private String rsaddress;
    private String rsname;

    public RoomDTO(final RoomEntity entity){
        this.rnum = entity.getRnum();
        this.rname = entity.getRname();
        this.rsaddress = entity.getRsaddress();
        this.rsname = entity.getRsname();
    }

    public static RoomEntity toEntity(final RoomDTO dto){
        return RoomEntity.builder()
                .rname(dto.getRname())
                .rnum(dto.getRnum())
                .rsaddress(dto.getRsaddress())
                .rsname(dto.getRsname())
                .build();
    }
}
