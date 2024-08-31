package com.dynamics.portfolio_service.service;

import com.dynamics.portfolio_service.dto.AssetDto;
import com.dynamics.portfolio_service.dto.PortfolioAssetCreateDto;
import com.dynamics.portfolio_service.dto.PortfolioAssetDto;
import com.dynamics.portfolio_service.dto.PortfolioDto;
import com.dynamics.portfolio_service.entity.Asset;
import com.dynamics.portfolio_service.entity.Portfolio;
import com.dynamics.portfolio_service.entity.PortfolioAsset;
import com.dynamics.portfolio_service.mapper.AssetMapper;
import com.dynamics.portfolio_service.mapper.PortfolioAssetMapper;
import com.dynamics.portfolio_service.mapper.PortfolioMapper;
import com.dynamics.portfolio_service.repository.PortfolioAssetRepository;
import com.dynamics.portfolio_service.repository.PortfolioRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class PortfolioAssetService {

   private final PortfolioAssetRepository portfolioAssetRepository;
   private final PortfolioService portfolioService;
   private final AssetService assetService;

    public void createPortfolioAsset(PortfolioAssetCreateDto portfolioAssetCreateDto, UUID userId) {

        if (!portfolioService.isUserPortfolioOwner(portfolioAssetCreateDto.getPortfolioId(), userId)){
            throw new RuntimeException("User is not the owner of the Portfolio and has no rights to create portfolio asset");
        }

        if (portfolioAssetRepository.existsByPortfolio_PortfolioIdAndAsset_AssetId(portfolioAssetCreateDto.getPortfolioId(), portfolioAssetCreateDto.getAssetId())){
            throw new RuntimeException("Asset alredy exists in this portfolio");
        }

        PortfolioDto portfolioDto = portfolioService.getByPortfolioId(portfolioAssetCreateDto.getPortfolioId());
        AssetDto assetDto = assetService.getByAssetId(portfolioAssetCreateDto.getAssetId());

        Portfolio portfolio  = PortfolioMapper.mapToPortfolio(portfolioDto, new Portfolio());
        Asset asset  = AssetMapper.mapToAsset(assetDto, new Asset());

        portfolio.setPortfolioId(portfolioAssetCreateDto.getPortfolioId());
        asset.setAssetId(portfolioAssetCreateDto.getAssetId());

        PortfolioAsset portfolioAsset = PortfolioAssetMapper.mapToPortfolioAsset(portfolioAssetCreateDto, new PortfolioAsset());

        portfolioAsset.setPortfolio(portfolio);
        portfolioAsset.setAsset(asset);

        portfolioAssetRepository.save(portfolioAsset);
    }

    public void updateQuantity(Long portfolioAssetId, double quantityChange, UUID userId){

        PortfolioAsset portfolioAsset = portfolioAssetRepository.findById(portfolioAssetId)
                .orElseThrow( () ->
                        new RuntimeException("No portfolio asset found with give id: " +  portfolioAssetId)
                );

        if (!portfolioService.isUserPortfolioOwner(portfolioAsset.getPortfolio().getPortfolioId(), userId)){
            throw new RuntimeException("User is not the owner of the Portfolio and has no rights to create portfolio asset");
        }

        double newQuantiy = Math.max(0, portfolioAsset.getQuantity() + quantityChange);

        portfolioAsset.setQuantity(newQuantiy);

        portfolioAssetRepository.save(portfolioAsset);

    }


}
