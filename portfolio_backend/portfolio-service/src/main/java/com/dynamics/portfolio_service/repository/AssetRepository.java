package com.dynamics.portfolio_service.repository;

import com.dynamics.portfolio_service.entity.Asset;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AssetRepository extends JpaRepository<Asset, Long> {

    Optional<Asset> findByAssetId(Long assetId);
}
