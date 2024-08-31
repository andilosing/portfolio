package com.dynamics.users.mapper;

import com.dynamics.users.dto.UserDto;
import com.dynamics.users.dto.UserRegistrationDto;
import com.dynamics.users.entity.User;

public class UserMapper {

    public static UserDto mapToUserDto(User user, UserDto userDto){
       userDto.setEmail(user.getEmail());
       userDto.setUsername(user.getUsername());
       userDto.setFirstName(user.getFirstName());
       userDto.setLastName(user.getLastName());
       return userDto;
    }

    public static User mapToUser(UserDto userDto, User user){
        user.setEmail(userDto.getEmail());
        user.setUsername(userDto.getUsername());
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        return user;
    }

    public static User mapToUser(UserRegistrationDto userRegistrationDto, User user){
        user.setEmail(userRegistrationDto.getEmail());
        user.setUsername(userRegistrationDto.getUsername());
        user.setFirstName(userRegistrationDto.getFirstName());
        user.setLastName(userRegistrationDto.getLastName());
        return user;
    }

}
