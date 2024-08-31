package com.dynamics.users.dto.response;

import com.dynamics.users.dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MultipleUserResponseDto extends ResponseDto{

    private List<UserDto> users;

    public MultipleUserResponseDto(String statusCode, String statusMessage, List<UserDto> users) {
        super(statusCode, statusMessage);
        this.users = users;
        }
}
