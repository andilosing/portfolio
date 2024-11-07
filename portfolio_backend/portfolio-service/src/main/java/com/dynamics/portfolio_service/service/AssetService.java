package com.dynamics.portfolio_service.service;

import com.dynamics.portfolio_service.dto.AssetDto;
import com.dynamics.portfolio_service.entity.Asset;
import com.dynamics.portfolio_service.mapper.AssetMapper;
import com.dynamics.portfolio_service.repository.AssetRepository;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class AssetService {

    private final AssetRepository assetRepository;

    private final AssetPriceService assetPriceService;

    public void createAsset(AssetDto assetDto) {
        Asset asset = AssetMapper.mapToAsset(assetDto, new Asset());
        assetRepository.save(asset);
    }

    public AssetDto getByAssetId(Long assetId) {
        Asset asset = assetRepository.findByAssetId(assetId)
                .orElseThrow(() -> new RuntimeException("Asset not found with given id: " + assetId));

        return AssetMapper.mapToAssetDto(asset, new AssetDto());
    }

    public List<AssetDto> getAllAssets() {
        List<Asset> assets = assetRepository.findAll();

        return assets.stream().map(asset -> AssetMapper.mapToAssetDto(asset, new AssetDto())).toList();
    }

    @Scheduled(fixedRate = 1800000) // execution every 1 hour
    public void updateAssetPrices(){
        List<Asset> assets = assetRepository.findAll();

        for (Asset asset : assets){
            try {
                updateAssetPriceIfNeeded(asset);
                Thread.sleep(3 * 60 * 1000);
            } catch (Exception e) {
              //  throw new RuntimeException("Error in fetching asset prices from extern api: " + e.getMessage());
                System.out.println("Error in fetching prices from extern api: " + e.getMessage());
            }
        }
    }

    private void updateAssetPriceIfNeeded(Asset asset) {
        if(needsPriceUpdate(asset.getValueDate())){
            BigDecimal newAssetPrice = assetPriceService.getCurrentPrice(asset.getName());

            asset.setLastValue(newAssetPrice);
            asset.setValueDate(LocalDateTime.now());
            assetRepository.save(asset);
        }
    }

    private boolean needsPriceUpdate(LocalDateTime lastUpdate) {
        return lastUpdate == null || lastUpdate.isBefore(LocalDateTime.now().minusMinutes(45));
    }
}
