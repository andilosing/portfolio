package com.dynamics.users.controller;

import com.dynamics.users.dto.response.MultipleUserResponseDto;
import com.dynamics.users.dto.response.ResponseDto;
import com.dynamics.users.dto.UserDto;
import com.dynamics.users.dto.response.SingleUserResponseDto;
import com.dynamics.users.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Validated
@RestController
@RequestMapping(path = "/users", produces = {MediaType.APPLICATION_JSON_VALUE})
public class UserController {


    private final UserService userService;

    @Autowired
    public UserController(UserService userService){
        this.userService = userService;
    }

    @GetMapping("/")
    public ResponseEntity<ResponseDto> getAllUsers(){
        List<UserDto> users = userService.findAll();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new MultipleUserResponseDto("200", "success", users));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<ResponseDto> getUserById(@PathVariable UUID userId){
        UserDto user = userService.findById(userId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new SingleUserResponseDto("200", "succes", user));
    }






}
