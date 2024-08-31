package com.dynamics.portfolio_service.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PortfolioAssetCreateDto {

    @NotNull(message = "portfolio id is mandatory")
    private Long portfolioId;

    @NotNull(message = "asset id is mandatory")
    private Long assetId;

    @NotNull(message = "quantity is mandatory")
    private Double quantity;
}
