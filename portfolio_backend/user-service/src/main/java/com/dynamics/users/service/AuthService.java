package com.dynamics.users.service;

import com.dynamics.users.dto.LoginDto;
import com.dynamics.users.dto.UserRegistrationDto;
import com.dynamics.users.entity.User;
import com.dynamics.users.repository.UserRepository;
import com.dynamics.users.security.JwtTokenProvider;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AuthService {

    //tbd läuft das hier alles richtig ab etc. muss man header checken ob der richtig ist etc

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    public void register(UserRegistrationDto userRegistrationDto){
        User user = new User();
        user.setUsername(userRegistrationDto.getUsername());
        user.setEmail(userRegistrationDto.getEmail());
        user.setPassword(userRegistrationDto.getPassword());
        user.setFirstName(userRegistrationDto.getFirstName());
        user.setLastName(userRegistrationDto.getLastName());
        user.setEnabled(true);

        userRepository.save(user);
    }

    public Map<String, Object> authenticate(LoginDto loginDto){
        Authentication authentication =  authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginDto.getUsername(),
                            loginDto.getPassword()
                    )
            );

        User user = userRepository.findByUsername(loginDto.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));


        String accessToken = jwtTokenProvider.generateAccessToken(user);
        Date accessTokenExpiryDate = jwtTokenProvider.getAccessTokenExpiryDate();
        String refreshToken = jwtTokenProvider.generateRefreshToken(user.getUsername(), user.getUserId());
        Date refreshTokenExpiryDate = jwtTokenProvider.getRefreshTokenExpiryDate();

        Map<String, Object> tokens = new HashMap<>();
        tokens.put("accessToken", accessToken);
        tokens.put("accessTokenExpiryDate", accessTokenExpiryDate);
        tokens.put("refreshToken", refreshToken);
        tokens.put("refreshTokenExpiryDate", refreshTokenExpiryDate);

        return tokens;
    }

    public Map<String, Object> refreshAccessToken(String refreshToken) {

        if (!jwtTokenProvider.validateToken(refreshToken)) {
            throw new RuntimeException("Invalid Refresh Token");
        }

        UUID userId = jwtTokenProvider.getUserIdFromJWT(refreshToken);


        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                user.getUsername(),
                null);

        String newAccessToken = jwtTokenProvider.generateAccessToken(user);
        Date newAccessTokenExpiryDate = jwtTokenProvider.getAccessTokenExpiryDate();


        Map<String, Object> tokens = new HashMap<>();
        tokens.put("accessToken", newAccessToken);
        tokens.put("accessTokenExpiryDate", newAccessTokenExpiryDate);

        return tokens;
    }
}
