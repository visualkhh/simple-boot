package com.simple.boot.transaction;

public interface TransactionManager {
    void beginTransaction();

    void commit();

    void rollback();
}
