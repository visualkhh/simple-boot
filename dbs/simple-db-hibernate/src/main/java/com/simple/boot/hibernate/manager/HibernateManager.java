package com.simple.boot.hibernate.manager;

import com.simple.boot.anno.Config;
import com.simple.boot.anno.Injection;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.io.Serializable;
import java.util.List;
import java.util.function.Consumer;

@Config
@Slf4j
public class HibernateManager {
    private final HibernateTransactionManager tm;
    private SessionFactory sessionFactory;

    @Injection
    public HibernateManager(HibernateTransactionManager hibernateTransactionManager) {
        this.tm = hibernateTransactionManager;
    }


    public Serializable save(Object data) {
        Serializable save = tm.currentSession().save(data);
//        session.flush();session.clear();session.getTransaction().commit();session.close();
        return save;
    }
//
    public <T> T find(Class<T> data, Serializable key) {
        T t = tm.currentSession().find(data, key);
        return t;
    }

    private <T> org.hibernate.query.Query<T> query(Session session, Class<T> data, Consumer<CriteriaQuery<T>> consumer) {
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(data);
        Root<T> root = criteriaQuery.from(data);
        criteriaQuery.select(root);

        if(null != consumer) {
            consumer.accept(criteriaQuery);
        }
        org.hibernate.query.Query<T> query = session.createQuery(criteriaQuery);
        return query;
    }

    public <T> List<T> resultList(Class<T> data) {
        return resultList(data, null);
    }

    public <T> List<T> resultList(Class<T> data, Consumer<CriteriaQuery<T>> consumer) {
        org.hibernate.query.Query<T> query = query(tm.currentSession(), data, consumer);
        List<T> resultList = query.getResultList();
        return resultList;
    }
}
