package com.dynamics.portfolio_service.mapper;

import com.dynamics.portfolio_service.dto.AssetDto;
import com.dynamics.portfolio_service.dto.PortfolioAssetCreateDto;
import com.dynamics.portfolio_service.dto.PortfolioAssetDto;
import com.dynamics.portfolio_service.entity.PortfolioAsset;

public class PortfolioAssetMapper {

    public static PortfolioAssetDto mapToPortfolioAssetDto(PortfolioAsset portfolioAsset, PortfolioAssetDto portfolioAssetDto){
        portfolioAssetDto.setAsset(AssetMapper.mapToAssetDto(portfolioAsset.getAsset(), new AssetDto()));
        portfolioAssetDto.setQuantity(portfolioAsset.getQuantity());
        portfolioAssetDto.setPortfolioAssetId(portfolioAsset.getPortfolioAssetId());

        return portfolioAssetDto;
    }


    public static PortfolioAsset mapToPortfolioAsset(PortfolioAssetDto portfolioAssetDto, PortfolioAsset portfolioAsset){
        portfolioAsset.setPortfolioAssetId(portfolioAssetDto.getPortfolioAssetId());
        portfolioAsset.setQuantity(portfolioAssetDto.getQuantity());

        return portfolioAsset;
    }

    public static PortfolioAsset mapToPortfolioAsset(PortfolioAssetCreateDto portfolioAssetCreateDto, PortfolioAsset portfolioAsset){
        portfolioAsset.setQuantity(portfolioAssetCreateDto.getQuantity());

        return portfolioAsset;
    }

}
