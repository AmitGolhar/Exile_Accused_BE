package com.exileaccused.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.exileaccused.entity.ExileRecord;

@Repository
public interface ExileRecordRepository extends JpaRepository<ExileRecord, Long> {
    List<ExileRecord> findByExileDataId(Long exileDataId);
}
