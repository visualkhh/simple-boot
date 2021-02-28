package com.simple.boot.simstance;


import com.simple.boot.anno.Controller;
import com.simple.boot.anno.Service;
import com.simple.boot.anno.Sim;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.reflections.Reflections;
import org.reflections.scanners.Scanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;

import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Getter
@Setter
@Slf4j
public class SimstanceManager {

    public static SimstanceManager instance;
    private final Class startClass;
    public Map<Class, Object> sims;
    // TODO: singletoen
    private SimstanceManager(Class startClass) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        this.startClass = startClass;
        init(this.startClass);
    }

    public static SimstanceManager getInstance() throws NullPointerException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        if(null != SimstanceManager.instance){
            return SimstanceManager.instance;
        } else {
            throw new NullPointerException();
        }
    }
    public static SimstanceManager getInstance(Class startClass) throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        if(null == SimstanceManager.instance){
            SimstanceManager.instance = new SimstanceManager(startClass);
        }
        return SimstanceManager.instance;
    }

    private void init(Class startClass) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {

        Reflections reflections = new Reflections(startClass.getPackage().getName(),
                new TypeAnnotationsScanner());

        Map<Class, Object> sims = new HashMap<>();
        reflections.getTypesAnnotatedWith(Controller.class, true).stream().forEach(it -> {
            sims.put(it, null);
        });
        reflections.getTypesAnnotatedWith(Service.class, true).stream().forEach(it -> {
            sims.put(it, null);
        });
        reflections.getTypesAnnotatedWith(Sim.class, true).stream().forEach(it -> {
            sims.put(it, null);
        });

        // TODO: DI
        for (Map.Entry<Class, Object> e: sims.entrySet()) {
            sims.put(e.getKey(), e.getKey().getDeclaredConstructor().newInstance());
        }
        this.sims = Collections.unmodifiableMap(sims);
    }
}
