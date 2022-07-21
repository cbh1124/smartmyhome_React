package com.example.smartmyhome.service;


import com.example.smartmyhome.model.event.NoticeEntity;
import com.example.smartmyhome.persistence.NoticeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class NoticeService {

    @Autowired
    NoticeRepository repository;

    // 전체 리스트
    public List<NoticeEntity> getlist(){return repository.findAll();}

    private void validate(final NoticeEntity noticeEntity) {
        if(noticeEntity == null) {
            log.warn("Entity cannot be null.");
            throw new RuntimeException("Entity cannot be null.");
        }

        if(noticeEntity.getNtitle() == null) {
            log.warn("Unknown notice.");
            throw new RuntimeException("Unknown notice.");
        }
    }

    public Optional<NoticeEntity> retrieve(final Integer nnum){return repository.findBynnum(nnum);}

    // 공지사항 등록하기 dto-> entity 변환 후 등록
    public Optional<NoticeEntity> getregist(final NoticeEntity noticeEntity){

        validate(noticeEntity);

        repository.save(noticeEntity);
        log.info("Entity notice : {} is saved", noticeEntity.getNnum());
        return repository.findBynnum(noticeEntity.getNnum());
    }

    // 공지사항 수정하기
    public Optional<NoticeEntity> getedit(final NoticeEntity noticeEntity){
        validate(noticeEntity);

        final Optional<NoticeEntity> original = repository.findBynnum(noticeEntity.getNnum());

        original.ifPresent(notice ->{
            notice.setNtitle(noticeEntity.getNtitle());
            notice.setNcontent(noticeEntity.getNcontent());

            repository.save(notice);
        });

        return retrieve(noticeEntity.getNnum());
    }

    // 공지사항 삭제하기
    public Optional<NoticeEntity> delete(final NoticeEntity noticeEntity){
        validate(noticeEntity);

        try{
            repository.delete(noticeEntity);
        }catch (Exception e){
            log.error("error deleting entity ", noticeEntity.getNnum(), e);

            throw new RuntimeException("error deleting entity" + noticeEntity.getNnum());
        }

        return retrieve(noticeEntity.getNnum());
    }
}
