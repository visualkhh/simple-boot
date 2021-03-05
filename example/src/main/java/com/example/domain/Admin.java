package com.example.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;

@Entity
@Getter
@Setter
public class Admin implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    Integer seq;
    String name;
}
