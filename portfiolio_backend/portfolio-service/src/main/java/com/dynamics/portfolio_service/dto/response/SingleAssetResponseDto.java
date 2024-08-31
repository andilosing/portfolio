package com.dynamics.portfolio_service.dto.response;

import com.dynamics.portfolio_service.dto.AssetDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SingleAssetResponseDto extends ResponseDto{

    private AssetDto assetDto;

    public SingleAssetResponseDto(String statusCode, String statusMessage, AssetDto assetDto) {
        super(statusCode, statusMessage);
        this.assetDto = assetDto;
    }
}
