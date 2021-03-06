package com.example.service;

import com.example.domain.Admin;
import com.simple.boot.anno.Injection;
import com.simple.boot.anno.Service;
import com.simple.boot.transaction.anno.Transactional;
import com.simple.boot.db.DatabaseAccessor;

import java.io.Serializable;
import java.util.List;

@Service
public class AdminService {

    private DatabaseAccessor hibernateManager;

    public AdminService() {
    }

    @Injection
    public AdminService(DatabaseAccessor hibernateManager) {
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
