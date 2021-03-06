package com.simple.boot.db.hibernate;

import com.simple.boot.anno.Config;
import com.simple.boot.anno.Injection;
import com.simple.boot.anno.PostConstruct;
import com.simple.boot.db.hibernate.config.HibernaterConfig;
import com.simple.boot.db.hibernate.manager.HibernateTransactionManager;
import com.simple.boot.simstance.SimstanceManager;
import com.simple.boot.starter.Starter;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.reflections.Reflections;
import org.reflections.scanners.TypeAnnotationsScanner;

import javax.persistence.Entity;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.io.Serializable;
import java.util.List;
import java.util.function.Consumer;

@Slf4j
@Config(order = -1_010_000)
public class HibernateStarter extends Starter {
    private final Class startClass;
    private final HibernaterConfig config;
    private final HibernateTransactionManager hibernateTransactionManager;
    private SessionFactory sessionFactory;

    @Injection
    public HibernateStarter(SimstanceManager simstanceManager, HibernaterConfig config, HibernateTransactionManager hibernateTransactionManager) {
        this.startClass = simstanceManager.getStartClass();
        this.config = config;
        this.hibernateTransactionManager = hibernateTransactionManager;
    }

    @PostConstruct
    public void init() {

//        Configuration configuration_pp = getHibernateConfigByCode();
//        Configuration configuration = new Configuration();
//        configuration.configure("hibernate.cfg.xml");
//        configuration.addPackage(startClass.getPackage().getName());

        Configuration configuration = new Configuration();
        config.property.entrySet().stream().forEach(it -> {
            configuration.setProperty(it.getKey(), null == it.getValue() ? "" : String.valueOf(it.getValue()));
        });


        Reflections reflections = new Reflections(startClass.getPackage().getName(), new TypeAnnotationsScanner());
        reflections.getTypesAnnotatedWith(Entity.class, true).stream().forEach(it -> {
            configuration.addAnnotatedClass(it);
        });

        StandardServiceRegistryBuilder serviceRegistryBuilder = new StandardServiceRegistryBuilder()
                .applySettings(configuration.getProperties());
        sessionFactory = configuration.buildSessionFactory(serviceRegistryBuilder.build());
        hibernateTransactionManager.setSessionFactory(sessionFactory);
    }
//    public Configuration getHibernateConfigByCode() {
//        Configuration configuration = new Configuration();
//        configuration.setProperty(Environment.DIALECT, 							"org.hibernate.dialect.H2Dialect");
//        configuration.setProperty(Environment.DRIVER, 							"org.h2.Driver");
////        configuration.setProperty(Environment.URL, 								"jdbc:h2:mem:test");
//        configuration.setProperty(Environment.URL, 								"jdbc:h2:tcp://localhost:9092/mem:asd");
//        configuration.setProperty(Environment.USER, 							"sa");
//        configuration.setProperty(Environment.PASS, 							"");
//        configuration.setProperty(Environment.POOL_SIZE, 						"55");
//        configuration.setProperty(Environment.STATEMENT_BATCH_SIZE, 			"30");
//        configuration.setProperty(Environment.AUTOCOMMIT, 						"true");
//        configuration.setProperty(Environment.SHOW_SQL, 						"true");
//        configuration.setProperty(Environment.FORMAT_SQL, 						"true");
//        configuration.setProperty(Environment.CURRENT_SESSION_CONTEXT_CLASS, 	"thread");
//        configuration.setProperty(Environment.HBM2DDL_AUTO, 					"create-drop");
//        return configuration;
//    }

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }
//
    public Serializable save(Object data) {
        Session session = getSessionFactory().getCurrentSession();
        session.beginTransaction();
        Serializable save = session.save(data);
        session.flush();
        session.clear();
        session.getTransaction().commit();
        session.close();
        return save;
    }

    public <T> T find(Class<T> data, Serializable key) {
        Session session = getSessionFactory().getCurrentSession();
        session.beginTransaction();
        T t = session.find(data, key);
        session.getTransaction().commit();
        session.close();
        return t;
    }
    public <T> org.hibernate.query.Query<T> query(Session session, Class<T> data, Consumer<CriteriaQuery<T>> consumer) {
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
        Session session = getSessionFactory().getCurrentSession();
        session.beginTransaction();
        org.hibernate.query.Query<T> query = query(session, data, consumer);
        List<T> resultList = query.getResultList();
        session.getTransaction().commit();
        session.close();
        return resultList;
    }

}
