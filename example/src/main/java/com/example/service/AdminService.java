package com.example.service;

import com.example.domain.Admin;
import com.simple.boot.anno.Injection;
import com.simple.boot.anno.Service;
import com.simple.boot.anno.transaction.Transactional;
import com.simple.boot.hibernate.manager.HibernateManager;

import java.io.Serializable;
import java.util.List;

@Service
public class AdminService {

    private HibernateManager hibernateManager;

    public AdminService() {
    }

    @Injection
    public AdminService(HibernateManager hibernateManager) {
        this.hibernateManager = hibernateManager;
    }

    public void print() {
        System.out.printf("zzzzzzzzzz");
    }

    @Transactional
    public Serializable save(Admin admin) {
        return hibernateManager.save(admin);
    }

    @Transactional
    public List<Admin> admins() {
        return hibernateManager.resultList(Admin.class);
    }


}
