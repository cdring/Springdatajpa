package com.cdring.jpa.repository;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name="reader")
public class Reader {

    @Id
    @GeneratedValue
    private Long id;

    @Column(length = 512)
    private String name;

    private int age;
    private String addr;

}
