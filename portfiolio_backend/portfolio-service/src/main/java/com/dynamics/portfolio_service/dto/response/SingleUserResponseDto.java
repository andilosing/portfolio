package com.dynamics.portfolio_service.dto.response;

import com.dynamics.portfolio_service.dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SingleUserResponseDto extends ResponseDto {

    private UserDto user;

    public SingleUserResponseDto(String statusCode, String statusMessage, UserDto user) {
        super(statusCode, statusMessage);
        this.user = user;
    }

}
