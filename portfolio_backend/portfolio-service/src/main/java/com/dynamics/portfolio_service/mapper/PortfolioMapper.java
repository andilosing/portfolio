package com.dynamics.portfolio_service.mapper;

import com.dynamics.portfolio_service.dto.PortfolioAssetDto;
import com.dynamics.portfolio_service.dto.PortfolioDetailsDto;
import com.dynamics.portfolio_service.dto.PortfolioDto;
import com.dynamics.portfolio_service.entity.Portfolio;
import com.dynamics.portfolio_service.entity.PortfolioAsset;

import java.util.List;
import java.util.stream.Collectors;

public class PortfolioMapper {

    public static PortfolioDto mapToPortfolioDto(Portfolio portfolio, PortfolioDto portfolioDto){
        portfolioDto.setPortfolioId(portfolio.getPortfolioId());
        portfolioDto.setName(portfolio.getName());

        return portfolioDto;
    }

    public static Portfolio mapToPortfolio(PortfolioDto portfolioDto, Portfolio portfolio) {

        portfolio.setName(portfolioDto.getName());

        return portfolio;
    }


    public static PortfolioDetailsDto mapToPortfolioDetailsDto(Portfolio portfolio, PortfolioDetailsDto portfolioDetailsDto){
        portfolioDetailsDto.setPortfolioId(portfolio.getPortfolioId());
        portfolioDetailsDto.setName(portfolio.getName());
        portfolioDetailsDto.setUserId(portfolio.getUserId());

        return portfolioDetailsDto;
    }
}
