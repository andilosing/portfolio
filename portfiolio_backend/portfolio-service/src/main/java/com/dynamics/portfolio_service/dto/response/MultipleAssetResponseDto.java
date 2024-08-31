package com.dynamics.portfolio_service.dto.response;

import com.dynamics.portfolio_service.dto.AssetDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MultipleAssetResponseDto extends ResponseDto{

    private List<AssetDto> assets;

    public MultipleAssetResponseDto(String statusCode, String statusMessage, List<AssetDto> assets) {
        super(statusCode, statusMessage);
        this.assets = assets;
        }
}
