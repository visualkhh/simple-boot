package com.simple.boot.hibernate;

import com.simple.boot.anno.*;
import com.simple.boot.config.ConfigLoader;
import com.simple.boot.hibernate.config.HibernaterConfig;
import com.simple.boot.simstance.SimstanceManager;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.reflections.Reflections;
import org.reflections.scanners.TypeAnnotationsScanner;

import javax.persistence.Entity;

@Slf4j
@Config(order = -999)
public class HibernateStarter {
    private final SimstanceManager simstanceManager;
    private final ConfigLoader configLoader;
    private SessionFactory sessionFactory;

    @Injection
    public HibernateStarter(SimstanceManager simstanceManager, ConfigLoader configLoader){
        this.simstanceManager = simstanceManager;
        this.configLoader = configLoader;
        init();
    }

    private void init() {
        HibernaterConfig config = this.configLoader.get("hibernate", HibernaterConfig.class);
        Class startClass = simstanceManager.getStartClass();

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

}
