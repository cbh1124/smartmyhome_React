package com.example.smartmyhome.dto;

import com.example.smartmyhome.model.event.NoticeEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class NoticeDTO {
    private Integer nnum;
    private String ntitle;
    private String ncontent;
    private LocalDateTime created_date;
    private LocalDateTime modified_date;

    public NoticeDTO(final NoticeEntity entity){
        this.nnum = entity.getNnum();
        this.ntitle = entity.getNtitle();
        this.ncontent = entity.getNcontent();
        this.created_date = entity.getCreatedDate();
        this.modified_date = entity.getModifiedDate();
    }

    public static NoticeEntity toEntity(final NoticeDTO dto){
        return NoticeEntity.builder()
                .nnum(dto.nnum)
                .ntitle(dto.ntitle)
                .ncontent(dto.ncontent)
                .build();
    }

}
