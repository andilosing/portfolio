package com.dynamics.users.dto.response;

import com.dynamics.users.dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

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
