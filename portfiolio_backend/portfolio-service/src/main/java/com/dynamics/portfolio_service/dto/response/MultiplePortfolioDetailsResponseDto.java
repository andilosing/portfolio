package com.dynamics.portfolio_service.dto.response;

import com.dynamics.portfolio_service.dto.PortfolioDetailsDto;
import com.dynamics.portfolio_service.dto.PortfolioDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MultiplePortfolioDetailsResponseDto extends ResponseDto{

    private List<PortfolioDetailsDto> portfoliosDetails;

    public MultiplePortfolioDetailsResponseDto(String statusCode, String statusMessage, List<PortfolioDetailsDto> portfoliosDetails) {
        super(statusCode, statusMessage);
        this.portfoliosDetails = portfoliosDetails;
        }
}
