package com.dynamics.portfolio_service.controller;

import com.dynamics.portfolio_service.dto.AssetDto;
import com.dynamics.portfolio_service.dto.response.MultipleAssetResponseDto;
import com.dynamics.portfolio_service.dto.response.ResponseDto;
import com.dynamics.portfolio_service.dto.response.SingleAssetResponseDto;
import com.dynamics.portfolio_service.service.AssetService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping(path = "/asset", produces = {MediaType.APPLICATION_JSON_VALUE})
public class AssetController {

    @Autowired
    private AssetService assetService;

    @GetMapping("/")
    public ResponseEntity<MultipleAssetResponseDto> getAllAssets(){
        List<AssetDto> assets = assetService.getAllAssets();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body( new MultipleAssetResponseDto(
                        "200",
                        "success",
                        assets
                ));
    }

    @GetMapping("/{assetId}")
    public ResponseEntity<SingleAssetResponseDto> getAssetById(@PathVariable Long assetId){
        AssetDto assetDto = assetService.getByAssetId(assetId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new SingleAssetResponseDto(
                        "200",
                        "successfull",
                        assetDto
                ));
    }

    @PostMapping("/create")
    public ResponseEntity<ResponseDto> createAsset(@Valid @RequestBody AssetDto assetDto){
        assetService.createAsset(assetDto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ResponseDto(
                        "201",
                        "successfully created"
                ));
    }
    /*
    PUT /assets/{id} - Aktualisieren eines bestehenden Assets.
    DELETE /assets/{id} - Löschen eines Assets.
     */
}


