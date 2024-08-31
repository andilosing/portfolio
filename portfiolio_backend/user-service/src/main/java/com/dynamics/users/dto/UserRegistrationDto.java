package com.dynamics.users.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class UserRegistrationDto {

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

    @NotBlank(message = "password is mandatory")
    @Pattern(
            regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{6,50}$",
            message = "Password must be between 6 and 50 characters long and contain at least one digit, one lowercase letter, one uppercase letter, and one special character"
    )
    private String password;
    
}
