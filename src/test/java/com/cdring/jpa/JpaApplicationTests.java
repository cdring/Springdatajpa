package com.cdring.jpa;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.cdring.jpa.jwt.JwtUserDetails;
import com.cdring.jpa.repository.ArticleRepository;
import com.cdring.jpa.repository.User;
import com.cdring.jpa.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.TestExecutionListeners;

@SpringBootTest
class JpaApplicationTests {



    @Autowired
    private UserRepository userRepository;
    @Test
    void contextLoads() {
    }
    @Test
    void jpaTest() {
        User user = new User();
        user.setUsername("1");
        user.setPassword("1");
        user.setEmail("cdring@mail");
        user.setCreateTime(new Date());
        user.setLastLoginTime(new Date());
        userRepository.save(user);

    }
    @Test
    public void testBCrypt() {
        //加密
        //String hashpw = BCrypt.hashpw("123", BCrypt.gensalt());
        //System.out.println(hashpw);
        //String password = new BCryptPasswordEncoder().encode("123");
        //System.out.println(password);

        //new BCryptPasswordEncoder().matches("123","$2a$10$NMuQE4pPKsJ5oY4AzrPPqeX4qu4HIVeeQsJoMoMviHeMPM0dKqjzm");
        //解密
        boolean checkpw = BCrypt.checkpw("123456","$2a$10$CG3HPiMaSff7KYMiHd2n7OwZfYcCwJsUSSJ2EQgRwUq8aAweSNeue");
        System.out.println(checkpw);
    }

    @Test
    void testUser() {
        User user = new User();
        user.setUsername("cdring");
        user.setPassword("cda");
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        JwtUserDetails jwtUserDetails = new JwtUserDetails(user.getId(),
                user.getUsername(),
                user.getPassword(),
                authorities);
        //return jwtUserDetails;
    }


}
