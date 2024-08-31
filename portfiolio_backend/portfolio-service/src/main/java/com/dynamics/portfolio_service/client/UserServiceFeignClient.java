package com.dynamics.portfolio_service.client;

import com.dynamics.portfolio_service.dto.UserDto;
import com.dynamics.portfolio_service.dto.response.SingleUserResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "user-service",  path = "/users")
public interface UserServiceFeignClient {

    @GetMapping("/{userId}")
    SingleUserResponseDto getUserById(@PathVariable UUID userId);
}
