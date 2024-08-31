package com.dynamics.portfolio_service.dto.response;

import com.dynamics.portfolio_service.dto.AssetDto;
import com.dynamics.portfolio_service.dto.PortfolioDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SinglePortfolioResponseDto extends ResponseDto{

    private PortfolioDto portfolio;

    public SinglePortfolioResponseDto(String statusCode, String statusMessage, PortfolioDto portfolio) {
        super(statusCode, statusMessage);
        this.portfolio = portfolio;
    }
}
