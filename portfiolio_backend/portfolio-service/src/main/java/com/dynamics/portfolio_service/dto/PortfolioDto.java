package com.dynamics.portfolio_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class PortfolioDto {

    private Long portfolioId;

    @NotBlank(message = "name is mandatory")
    private String name;

}
