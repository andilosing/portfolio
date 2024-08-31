package com.dynamics.users.controller;

import com.dynamics.users.dto.LoginDto;
import com.dynamics.users.dto.UserRegistrationDto;
import com.dynamics.users.dto.response.AuthResponseDto;
import com.dynamics.users.dto.response.ResponseDto;
import com.dynamics.users.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.Map;

@RestController
@RequestMapping(path = "/auth", produces = {MediaType.APPLICATION_JSON_VALUE})
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<ResponseDto> registerUser(@Valid @RequestBody UserRegistrationDto userRegistrationDto){
        authService.register(userRegistrationDto);
        return  ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ResponseDto("204", "successfully registered"));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@Valid @RequestBody LoginDto loginDto){
        Map<String, Object> tokens = authService.authenticate(loginDto);
        String accessToken = (String) tokens.get("accessToken");
        Date accessTokenExpiryDate = (Date) tokens.get("accessTokenExpiryDate");
        String refreshToken = (String) tokens.get("refreshToken");
        Date refreshTokenExpiryDate = (Date) tokens.get("refreshTokenExpiryDate");

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new AuthResponseDto(
                        "200",
                        "Login successful",
                        accessToken,
                        accessTokenExpiryDate,
                        refreshToken,
                        refreshTokenExpiryDate
                ));
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponseDto> refreshAccessToken(@RequestBody Map<String, String> tokenMap){
        String refreshToken = tokenMap.get("refreshToken");
        Map<String, Object> tokens = authService.refreshAccessToken(refreshToken);
        String accessToken = (String) tokens.get("accessToken");
        Date accessTokenExpiryDate = (Date) tokens.get("accessTokenExpiryDate");

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new AuthResponseDto(
                        "200",
                        "access token successfully refreshed",
                        accessToken,
                        accessTokenExpiryDate
                        ));
    }
}
