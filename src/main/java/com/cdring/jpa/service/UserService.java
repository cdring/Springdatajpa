package com.cdring.jpa.service;

import com.cdring.jpa.jwt.JwtUserDetails;
import com.cdring.jpa.repository.User;

public interface UserService {
    String login(User user);
    void logout(String token);
    User register(User user);
}
