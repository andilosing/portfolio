package com.dynamics.portfolio_service.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserDto {

    @NotBlank(message = "username is mandatory")
    @Size(min = 4, max = 20, message = "the length of the username should be between 4 and 20")
    private String username;

    @NotBlank(message = "email is mandatory")
    @Email(message = "email should be a valid value")
    private String email;

    @Size(min = 4, max = 20, message = "the length of the first name should be between 4 and 20")
    private String firstName;

    @Size(min = 4, max = 20, message = "the length of the last name should be between 4 and 20")
    private String lastName;

}
