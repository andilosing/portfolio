package com.dynamics.users.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@Data
@AllArgsConstructor
public class AuthResponseDto extends ResponseDto {

    private Map<String, Object> tokens;

    public AuthResponseDto(String statusCode, String statusMessage, String accessToken, Date accessTokenExpiryDate, String refreshToken, Date refreshTokenExpiryDate) {
        super(statusCode, statusMessage);
        this.tokens = new LinkedHashMap<>();
        this.tokens.put("accessToken", accessToken);
        this.tokens.put("accessTokenExpiryDate", accessTokenExpiryDate);
        this.tokens.put("refreshToken", refreshToken);
        this.tokens.put("refreshTokenExpiryDate", refreshTokenExpiryDate);
    }

    public AuthResponseDto(String statusCode, String statusMessage, String accessToken, Date accessTokenExpiryDate) {
        super(statusCode, statusMessage);
        this.tokens = new LinkedHashMap<>();
        this.tokens.put("accessToken", accessToken);
        this.tokens.put("accessTokenExpiryDate", accessTokenExpiryDate);
    }
}
