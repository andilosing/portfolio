package com.dynamics.portfolio_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class PortfolioDetailsDto {

    @NotBlank(message = "portfolio id ist mandatory")
    private Long portfolioId;

    @NotBlank(message = "name is mandatory")
    private String name;

    @NotNull(message = "user id is mandatory")
    private UUID userId;

    private List<PortfolioAssetDto> portfolioAssets;
}
