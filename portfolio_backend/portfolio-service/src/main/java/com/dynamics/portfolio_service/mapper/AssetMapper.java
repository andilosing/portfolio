package com.dynamics.portfolio_service.mapper;

import com.dynamics.portfolio_service.dto.AssetDto;
import com.dynamics.portfolio_service.entity.Asset;

public class AssetMapper {

    public static AssetDto mapToAssetDto(Asset asset, AssetDto assetDto){
        assetDto.setAssetId(asset.getAssetId());
        assetDto.setName(asset.getName());
        assetDto.setType(asset.getType());
        assetDto.setLastValue(asset.getLastValue());
        assetDto.setValueDate(asset.getValueDate());
        return assetDto;
    }

    public static Asset mapToAsset(AssetDto assetDto, Asset asset){
        asset.setAssetId(assetDto.getAssetId());
        asset.setName(assetDto.getName());
        asset.setType(assetDto.getType());
        asset.setLastValue(assetDto.getLastValue());
        asset.setValueDate(assetDto.getValueDate());
        return asset;
    }
}
