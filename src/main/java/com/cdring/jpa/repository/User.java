package com.cdring.jpa.repository;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name="user")
public class User {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false,length = 32)
    private String username;

    @Column(nullable = false,length = 32)
    private String password;

    @Column(length = 32)
    private String email;

    private Date createTime;

    private Date lastLoginTime;

}