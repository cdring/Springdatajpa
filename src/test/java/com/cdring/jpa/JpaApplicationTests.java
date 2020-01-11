package com.cdring.jpa;
import java.util.Date;

import com.cdring.jpa.repository.ArticleRepository;
import com.cdring.jpa.repository.User;
import com.cdring.jpa.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class JpaApplicationTests {

    @Autowired
    ArticleRepository articleRepository;

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
       // articleRepository.save(user);

    }

}
