package com.dynamics.portfolio_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class AssetDto {

    private Long assetId;

    @NotBlank(message = "name is mandatory")
    private String name;

    @NotBlank(message = "type is mandatory")
    private String type;

    private BigDecimal lastValue;

    private LocalDateTime valueDate;
}
