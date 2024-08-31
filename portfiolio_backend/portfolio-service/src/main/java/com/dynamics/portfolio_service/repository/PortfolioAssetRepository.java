package com.dynamics.portfolio_service.repository;

import com.dynamics.portfolio_service.entity.Asset;
import com.dynamics.portfolio_service.entity.PortfolioAsset;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PortfolioAssetRepository extends JpaRepository<PortfolioAsset, Long> {

    Optional<PortfolioAsset> findByPortfolioAssetId(Long portfolioAssetId);

    Optional<List<PortfolioAsset>> findByPortfolio_PortfolioId(Long portfolioId);

    boolean existsByPortfolio_PortfolioIdAndAsset_AssetId(Long portfolioId, Long assetId);
}
