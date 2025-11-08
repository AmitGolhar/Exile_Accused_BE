package com.exileaccused.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.exileaccused.entity.ExileRecord;

@Repository
public interface ExileRecordRepository extends JpaRepository<ExileRecord, Long> {
    List<ExileRecord> findByExileDataId(Long exileDataId);
    
    @Query(value = "SELECT COALESCE(MAX(CAST(sr_no AS UNSIGNED)), 0) FROM exile_record", nativeQuery = true)
    Long findMaxSrNo();
}
