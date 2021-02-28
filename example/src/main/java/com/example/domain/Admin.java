package com.example.domain;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Admin {
    @Id
    Integer seq;
    String name;
}
