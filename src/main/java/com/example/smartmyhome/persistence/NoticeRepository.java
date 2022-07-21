package com.example.smartmyhome.persistence;


import com.example.smartmyhome.model.event.NoticeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NoticeRepository extends JpaRepository<NoticeEntity, Integer> {

    Optional<NoticeEntity> findBynnum(Integer nnum);
}
