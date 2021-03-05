package com.simple.boot.hibernate.manager;

import com.simple.boot.anno.Config;
import com.simple.boot.transaction.TransactionManager;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

@Slf4j
@Config
public class HibernateTransactionManager implements TransactionManager {

    private SessionFactory sessionFactory;

    public HibernateTransactionManager() {
      log.info("HibernateTransactionManager constructor");
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void beginTransaction() {
        try {
            this.sessionFactory.getCurrentSession().beginTransaction();
        } catch (Exception e) {
            log.error("beginTransaction error", e);
        }
    }

    public Session currentSession() {
        return this.sessionFactory.getCurrentSession();
    }

    public void flush() {
        try {
            this.sessionFactory.getCurrentSession().flush();
        } catch (Exception e) {
            log.error("flush error", e);
        }
    }
    public void clear() {
        try {
            this.sessionFactory.getCurrentSession().clear();
        } catch (Exception e) {
            log.error("clear error", e);
        }
    }
    @Override
    public void commit() {
        try {
            this.sessionFactory.getCurrentSession().getTransaction().commit();
        } catch (Exception e) {
            log.error("commit error", e);
        }
    }

    @Override
    public void rollback() {
        try {
            this.sessionFactory.getCurrentSession().getTransaction().rollback();
        } catch (Exception e) {
            log.error("rollback error", e);
        }
    }


//
}
