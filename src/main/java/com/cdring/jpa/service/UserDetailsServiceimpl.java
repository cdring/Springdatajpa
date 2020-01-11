package com.cdring.jpa.service;

import com.cdring.jpa.jwt.JwtUserDetails;
import com.cdring.jpa.repository.User;
import com.cdring.jpa.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserDetailsServiceimpl implements UserDetailsService {


    @Resource
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        List<User> list = userRepository.findByUsername(s);
        User user = list.get(0);
        if (user == null) {
            throw new UsernameNotFoundException(String.format("No user found with username '%s'.", s));
        }
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        JwtUserDetails jwtUserDetails = new JwtUserDetails(user.getId(),
                user.getUsername(),
                user.getPassword(),
                user.getEmail(),
                authorities);
        return jwtUserDetails;
    }
}
