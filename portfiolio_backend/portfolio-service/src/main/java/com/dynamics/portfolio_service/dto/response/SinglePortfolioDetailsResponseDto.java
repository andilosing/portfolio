package com.dynamics.portfolio_service.dto.response;

import com.dynamics.portfolio_service.dto.PortfolioDetailsDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SinglePortfolioDetailsResponseDto extends ResponseDto {

    private PortfolioDetailsDto portfolioDetails;

    public SinglePortfolioDetailsResponseDto(String statusCode, String statusMessage, PortfolioDetailsDto portfolioDetails) {
        super(statusCode, statusMessage);
        this.portfolioDetails = portfolioDetails;
    }
}
