package com.exileaccused.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.exileaccused.entity.BlacklistedToken;

public interface BlacklistedTokenRepository extends JpaRepository<BlacklistedToken, Long> {
    Optional<BlacklistedToken> findByToken(String token);
}