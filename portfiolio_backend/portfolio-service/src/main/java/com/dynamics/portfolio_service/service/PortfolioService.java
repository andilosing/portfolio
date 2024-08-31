package com.dynamics.portfolio_service.service;

import com.dynamics.portfolio_service.dto.PortfolioAssetDto;
import com.dynamics.portfolio_service.dto.PortfolioDetailsDto;
import com.dynamics.portfolio_service.dto.PortfolioDto;
import com.dynamics.portfolio_service.dto.UserDto;
import com.dynamics.portfolio_service.dto.response.SingleUserResponseDto;
import com.dynamics.portfolio_service.entity.Portfolio;
import com.dynamics.portfolio_service.entity.PortfolioAsset;
import com.dynamics.portfolio_service.mapper.PortfolioAssetMapper;
import com.dynamics.portfolio_service.mapper.PortfolioMapper;
import com.dynamics.portfolio_service.repository.PortfolioAssetRepository;
import com.dynamics.portfolio_service.repository.PortfolioRepository;
import com.dynamics.portfolio_service.client.UserServiceFeignClient;
import feign.FeignException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PortfolioService {

    private final PortfolioRepository portfolioRepository;
    private final UserServiceFeignClient userServiceFeignClient;
    private final PortfolioAssetRepository portfolioAssetRepository;


    public void createPortfolio(PortfolioDto portfolioDto, UUID userId) {

        boolean isPortfolioAlreadyCreated = portfolioRepository.existsByUserId(userId);

        if (isPortfolioAlreadyCreated){
            throw new RuntimeException("User already has a existing Portfolio");
        }

        Portfolio portfolio = PortfolioMapper.mapToPortfolio(portfolioDto, new Portfolio());
        portfolio.setUserId(userId);

        portfolioRepository.save(portfolio);
    }


    public PortfolioDto getByPortfolioId(Long portfolioId) {
        Portfolio portfolio = portfolioRepository.findByPortfolioId(portfolioId)
                .orElseThrow(() -> new RuntimeException("Portfolio not found with given id: " + portfolioId));

        return PortfolioMapper.mapToPortfolioDto(portfolio, new PortfolioDto());
    }


    public List<PortfolioDto> getAllPortfolios() {
        List<Portfolio> portfolios = portfolioRepository.findAll();
       // portfolios.forEach(portfolio -> System.out.println(portfolio.getPortfolioId() + "" + portfolio.getPortfolioAssets().isEmpty()));

        return portfolios.stream().map(portfolio -> PortfolioMapper.mapToPortfolioDto(portfolio, new PortfolioDto())).toList();
    }

    public PortfolioDetailsDto getPortfolioByUserId(UUID userId) {
        Portfolio portfolio = portfolioRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("No portfolio found for user with given id: " + userId));

        List<PortfolioAsset> portfolioAssets = portfolioAssetRepository.findByPortfolio_PortfolioId(portfolio.getPortfolioId())
                .orElseThrow(() -> new RuntimeException("Portfolio assets not found with given portfolio id: " + portfolio.getPortfolioId()));

        PortfolioDetailsDto portfolioDetailsDto = PortfolioMapper.mapToPortfolioDetailsDto(portfolio,new PortfolioDetailsDto());

        List<PortfolioAssetDto> portfolioAssetDtos = portfolioAssets.stream()
                .map(portfolioAsset -> PortfolioAssetMapper.mapToPortfolioAssetDto(portfolioAsset, new PortfolioAssetDto()))
                .toList();

        portfolioDetailsDto.setPortfolioAssets(portfolioAssetDtos);

        return portfolioDetailsDto;
    }

    /*
    public List<PortfolioDetailsDto> getAllPortfoliosDetails() {
        List<Portfolio> portfolios = portfolioRepository.findAll();

        List<PortfolioDetailsDto> portfolioDetailsDtos = new ArrayList<>();

        for (Portfolio portfolio : portfolios) {
            List<PortfolioAsset> portfolioAssets = portfolioAssetRepository.findByPortfolio_PortfolioId(portfolio.getPortfolioId())
                    .orElseThrow(() -> new RuntimeException("Portfolio assets not found for portfolio id: " + portfolio.getPortfolioId()));

            PortfolioDetailsDto portfolioDetailsDto = PortfolioMapper.mapToPortfolioDetailsDto(portfolio, new PortfolioDetailsDto());

            List<PortfolioAssetDto> portfolioAssetDtos = portfolioAssets.stream()
                    .map(portfolioAsset -> PortfolioAssetMapper.mapToPortfolioAssetDto(portfolioAsset, new PortfolioAssetDto()))
                    .collect(Collectors.toList());

            portfolioDetailsDto.setPortfolioAssets(portfolioAssetDtos);

            portfolioDetailsDtos.add(portfolioDetailsDto);
        }

        return portfolioDetailsDtos;
    }

     */



    public PortfolioDetailsDto getPortfolioDetailsByPortfolioId(Long portfolioId) {
        Portfolio portfolio = portfolioRepository.findByPortfolioId(portfolioId)
                .orElseThrow(() -> new RuntimeException("Portfolio not found with given id: " + portfolioId));

        List<PortfolioAsset> portfolioAssets = portfolioAssetRepository.findByPortfolio_PortfolioId(portfolioId)
                .orElseThrow(() -> new RuntimeException("Portfolio assets not found with given portfolio id: " + portfolioId));

        PortfolioDetailsDto portfolioDetailsDto = PortfolioMapper.mapToPortfolioDetailsDto(portfolio,new PortfolioDetailsDto());

        List<PortfolioAssetDto> portfolioAssetDtos = portfolioAssets.stream()
                .map(portfolioAsset -> PortfolioAssetMapper.mapToPortfolioAssetDto(portfolioAsset, new PortfolioAssetDto()))
                .toList();

        portfolioDetailsDto.setPortfolioAssets(portfolioAssetDtos);

        return portfolioDetailsDto;
    }



    public boolean isUserPortfolioOwner(Long portfolioId, UUID userID){
        UUID portfolioOwnerID = portfolioRepository.findUserIdByPortfolioId(portfolioId).orElseThrow(() ->
            new RuntimeException("No portfolio exists with given portfolio id: "+  portfolioId)
        );

        return portfolioOwnerID.equals(userID);
    }


    public void updateAccessCode(Long portfolioId, UUID userId, String accessCode) {
        Portfolio portfolio = portfolioRepository.findById(portfolioId).orElseThrow(() ->
                new RuntimeException("No portfolio exists with the give portfolio id: " + portfolioId));

        if (!portfolio.getUserId().equals(userId)) {
            throw new RuntimeException("User with given id: " + userId + " is not the owner of the portfolio with the given portfolio id: " + portfolioId);
        }

        portfolio.setAccessCode(accessCode);
        portfolioRepository.save(portfolio);

    }



    public boolean verifyAccessCode(Long portfolioId, String accessCode) {
        Portfolio portfolio = portfolioRepository.findById(portfolioId).orElseThrow();
        return portfolio.getAccessCode().equals(accessCode);
    }

    public PortfolioDetailsDto verifyAccessCodeAndReturnPortfolioIfAccessIsGranted(Long portfolioId, String accessCode) {

        if(!verifyAccessCode(portfolioId, accessCode)){
            throw new RuntimeException("invalid access code");
        }

        return getPortfolioDetailsByPortfolioId(portfolioId);
    }

    public String getAccessCodeByUserId(UUID userId) {
        Portfolio portfolio = portfolioRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("No portfolio found for user with given id: " + userId));
        return portfolio.getAccessCode();
    }






/* How to load users of other microservice
    public Map<String, Object> calculatePortfolioValue(Long portfolioId){

        Portfolio portfolio = portfolioRepository.findById(portfolioId)
                .orElseThrow(() -> new RuntimeException("No portfolio found with given id: " + portfolioId));

        SingleUserResponseDto singleUserResponseDto;
        UserDto userDto;
        try {
             singleUserResponseDto = userServiceFeignClient.getUserById(portfolio.getUserId());
             userDto = singleUserResponseDto.getUser();
        } catch (Exception e) {
            throw new RuntimeException("User does not exist with give id: " + portfolio.getUserId());
        }

        BigDecimal totalValue = BigDecimal.ZERO;
        Map<String, Object> portfolioInfoValues = new HashMap<>();

        portfolioInfoValues.put("portfolio", portfolio.getName());
        portfolioInfoValues.put("owner: ", userDto.getFirstName());

        for (PortfolioAsset portfolioAsset : portfolio.getPortfolioAssets()) {
            String assetName = portfolioAsset.getAsset().getName();
            BigDecimal currentPrice = assetPriceService.getCurrentPrice(assetName);
            BigDecimal assetValue = currentPrice.multiply(BigDecimal.valueOf(portfolioAsset.getQuantity()));
            portfolioInfoValues.put(assetName, assetValue);
            totalValue = totalValue.add(assetValue);
        }

        portfolioInfoValues.put("total value", totalValue);

        return portfolioInfoValues;
    }

 */

}
