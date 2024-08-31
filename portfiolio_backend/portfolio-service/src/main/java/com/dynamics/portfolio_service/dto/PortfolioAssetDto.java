package com.dynamics.portfolio_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PortfolioAssetDto {

    @NotNull(message = "portfolio asset id is mandatory")
    private Long portfolioAssetId;

    @NotNull(message = "asset is mandatory")
    private AssetDto asset;

    @NotNull(message = "quantity is mandatory")
    private Double quantity;
}
