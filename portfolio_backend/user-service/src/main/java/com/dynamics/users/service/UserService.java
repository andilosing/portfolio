package com.dynamics.users.service;

import com.dynamics.users.dto.UserDto;
import com.dynamics.users.dto.UserRegistrationDto;
import com.dynamics.users.entity.User;
import com.dynamics.users.mapper.UserMapper;
import com.dynamics.users.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class UserService {

    private UserRepository userRepository;

    public List<UserDto> findAll(){
        List<User> users = userRepository.findAll();

        return users.stream().map(u -> UserMapper.mapToUserDto(u, new UserDto())).toList();
    }

    public UserDto findById(UUID userId){
        User user = userRepository.findByUserId(userId).orElseThrow(() -> new RuntimeException("no user found with given id: " + userId));

        return UserMapper.mapToUserDto(user, new UserDto());
    }



}
