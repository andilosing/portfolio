package com.dynamics.portfolio_service.controller;

import com.dynamics.portfolio_service.dto.AssetDto;
import com.dynamics.portfolio_service.dto.PortfolioAssetCreateDto;
import com.dynamics.portfolio_service.dto.PortfolioAssetDto;
import com.dynamics.portfolio_service.dto.response.ResponseDto;
import com.dynamics.portfolio_service.service.PortfolioAssetService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Controller
@RequestMapping(path = "/portfolio-asset", produces = {MediaType.APPLICATION_JSON_VALUE})
public class PortfolioAssetController {

    @Autowired
    private PortfolioAssetService portfolioAssetService;

    @PostMapping("/create")
    public ResponseEntity<ResponseDto> createPortfolioAsset(@Valid @RequestBody PortfolioAssetCreateDto portfolioAssetCreateDto,
                                                            @RequestHeader("X-User-ID") UUID userId){

        portfolioAssetService.createPortfolioAsset(portfolioAssetCreateDto, userId);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ResponseDto(
                        "201",
                        "successfully created"
                ));
    }

    @PutMapping("/{portfolioAssetId}")
    public ResponseEntity<ResponseDto> updateQuantity(@PathVariable Long portfolioAssetId, @RequestParam Double quantityChange,
                                                      @RequestHeader("X-User-ID") UUID userId){

        portfolioAssetService.updateQuantity(portfolioAssetId, quantityChange, userId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseDto(
                        "200",
                        "successfully updated"
                ));
    }

}
