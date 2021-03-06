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

    private DatabaseAccessor databaseAccessor;

    public AdminService() {
    }

    @Injection
    public AdminService(DatabaseAccessor databaseAccessor) {
        this.databaseAccessor = databaseAccessor;
    }

    public void print() {
        System.out.printf("zzzzzzzzzz");
    }

    @Transactional
    public Serializable save(Admin admin) {
        return databaseAccessor.save(admin);
    }

    @Transactional
    public List<Admin> admins() {
        return databaseAccessor.resultList(Admin.class);
    }

}
