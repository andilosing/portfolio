package com.dynamics.users;

import com.dynamics.users.entity.User;

public class PasswordGenerator {
    public static void main(String[] args) {
        User user = new User();
        String rawPassword = "xxx";
        user.setPassword(rawPassword);
        System.out.println("Generated BCrypt Hash: " + user.getPassword());
    }
}
