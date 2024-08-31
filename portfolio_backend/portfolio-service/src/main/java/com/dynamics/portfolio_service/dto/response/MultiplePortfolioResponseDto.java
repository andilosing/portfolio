package com.dynamics.portfolio_service.dto.response;

import com.dynamics.portfolio_service.dto.AssetDto;
import com.dynamics.portfolio_service.dto.PortfolioDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MultiplePortfolioResponseDto extends ResponseDto{

    private List<PortfolioDto> portfolios;

    public MultiplePortfolioResponseDto(String statusCode, String statusMessage, List<PortfolioDto> portfolios) {
        super(statusCode, statusMessage);
        this.portfolios = portfolios;
        }
}
