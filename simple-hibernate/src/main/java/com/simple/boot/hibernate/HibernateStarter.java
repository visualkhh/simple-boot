package com.simple.boot.hibernate;

import com.simple.boot.anno.*;
import com.simple.boot.config.ConfigLoader;
import com.simple.boot.hibernate.config.HibernaterConfig;
import com.simple.boot.simstance.SimstanceManager;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.reflections.Reflections;
import org.reflections.scanners.TypeAnnotationsScanner;

import javax.persistence.Entity;

@Slf4j
@Config
public class HibernateStarter {
    private final SimstanceManager simstanceManager;
    private final ConfigLoader configLoader;
    private Configuration configuration;

    @Injection
    public HibernateStarter(SimstanceManager simstanceManager, ConfigLoader configLoader){
        this.simstanceManager = simstanceManager;
        this.configLoader = configLoader;
        init();
    }

    private void init() {
        HibernaterConfig config = this.configLoader.get("hibernate", HibernaterConfig.class);
        Class startClass = simstanceManager.getStartClass();
        Configuration configuration = new Configuration();
//        configuration.configure("hibernate.cfg.xml");
        configuration.addPackage(startClass.getPackage().getName());

        config.property.entrySet().forEach(it -> {
            configuration.setProperty(it.getKey(),String.valueOf(it.getValue()));
        });


        Reflections reflections = new Reflections(startClass.getPackage().getName(), new TypeAnnotationsScanner());
        reflections.getTypesAnnotatedWith(Entity.class, true).stream().forEach(it -> {
            configuration.addAnnotatedClass(it);
        });

        this.configuration = configuration;

    }

    public SessionFactory buildSessionFactory() {
        StandardServiceRegistryBuilder serviceRegistryBuilder = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties());
        return configuration.buildSessionFactory(serviceRegistryBuilder.build());
    }

}
