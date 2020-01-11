package com.cdring.jpa.service;

import com.cdring.jpa.jwt.JwtToken;
import com.cdring.jpa.jwt.JwtUserDetails;
import com.cdring.jpa.repository.User;
import com.cdring.jpa.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;


@Service
public class UserServiceimpl implements UserService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtToken jwtTokenUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public String login(String username, String password) {
        UsernamePasswordAuthenticationToken upToken = new UsernamePasswordAuthenticationToken(username, password);
        Authentication authentication = authenticationManager.authenticate(upToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        JwtUserDetails jwtUserDetails = (JwtUserDetails) authentication.getPrincipal();
        // 用户已经禁用
//        if (JwtUserDetails.getStatus() == BaseStatus.INVALID.getCode()) {
//            throw new GlobalDefaultException(6011);
//        }
        String accessToken = jwtTokenUtil.generateToken(jwtUserDetails);
        //jwtUserDetails.setToken("Bearer " + accessToken);
        return "Bearer " + accessToken;
    }

    @Override
    public void logout(String token) {

    }

    @Override
    public User register(User user) {
        String username = user.getUsername();
        if(!userRepository.findByUsername(username).isEmpty()){
            throw new AuthenticationCredentialsNotFoundException("用户已经存在");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setCreateTime(new Date());
        user.setLastLoginTime(new Date());
        //userToAdd.setRoles(asList("ROLE_USER"));
        return userRepository.save(user);
    }
}
