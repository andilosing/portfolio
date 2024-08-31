package com.dynamics.portfolio_service.repository;

import com.dynamics.portfolio_service.entity.Asset;
import com.dynamics.portfolio_service.entity.Portfolio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface PortfolioRepository extends JpaRepository<Portfolio, Long> {

    Optional<Portfolio> findByPortfolioId(Long portfolioId);

    @Query("SELECT p.userId FROM Portfolio p WHERE p.portfolioId = :portfolioId")
    Optional<UUID> findUserIdByPortfolioId(@Param("portfolioId") Long portfolioId);

    boolean existsByUserId(UUID userId);

    Optional<Portfolio> findByUserId(UUID userId);
}
