package com.exileaccused.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.exileaccused.entity.ExileData;

@Repository
public interface ExileDataRepository extends JpaRepository<ExileData, Long> {
}
