package com.simple.boot.simstance;


import com.simple.boot.anno.*;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.reflections.Reflections;
import org.reflections.scanners.*;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
@Setter
@Slf4j
public class SimstanceManager {

    private final String SIMPLE_BASE_PACKAGE = "com.simple.boot";

    public static SimstanceManager instance;
    private final Class startClass;
    public Map<Class, Object> sims;

    // TODO: singletoen
    private SimstanceManager(Map<Class, Object> definedSims, Class startClass) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        this.startClass = startClass;
        sims = new LinkedHashMap<>();
        sims.putAll(definedSims);
        sims.put(this.getClass(), this);
        init(this.startClass);
    }

    public static SimstanceManager getInstance() throws NullPointerException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        if (null != SimstanceManager.instance) {
            return SimstanceManager.instance;
        } else {
            throw new NullPointerException();
        }
    }

    public static SimstanceManager getInstance(Map<Class, Object> definedSims, Class startClass) throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        if (null == SimstanceManager.instance) {
            SimstanceManager.instance = new SimstanceManager(definedSims, startClass);
        }
        return SimstanceManager.instance;
    }

    private void init(Class startClass) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        for (String pit : Arrays.asList(SIMPLE_BASE_PACKAGE, startClass.getPackage().getName())) {
            Reflections reflections = new Reflections(pit, new TypeAnnotationsScanner());
            reflections.getTypesAnnotatedWith(Config.class, true).stream().forEach(it -> sims.put(it, null));
            reflections.getTypesAnnotatedWith(Controller.class, true).stream().forEach(it -> sims.put(it, null));
            reflections.getTypesAnnotatedWith(Service.class, true).stream().forEach(it -> sims.put(it, null));
            reflections.getTypesAnnotatedWith(Sim.class, true).stream().forEach(it -> sims.put(it, null));
        }

//        List<Class> collect = sims.keySet().stream().filter(it -> it.isAnnotationPresent(Config.class)).sorted((it, sit) -> {
//            return Integer.compare(((Config) it.getAnnotation(Config.class)).order(), ((Config) sit.getAnnotation(Config.class)).order());
//        }).collect(Collectors.toList());
        List<Class> collect = sims.keySet().stream().filter(it -> it.isAnnotationPresent(Config.class)).sorted((it, sit) -> {
            return Integer.compare(((Config) it.getAnnotation(Config.class)).order(), ((Config) sit.getAnnotation(Config.class)).order());
        }).collect(Collectors.toList());
        Collections.reverse(collect);
        for (Class aClass : collect) {
            create(aClass);
        }

        for (Class klass : sims.keySet()) {
            create(klass);
        }
    }

    public Object create(Class klass) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Object obj = sims.get(klass);
        if (null != obj) {
            return obj;
        }

        Object value = null;

        Optional<Constructor> injectionCunstructors = Stream.of(klass.getConstructors()).filter(it -> it.isAnnotationPresent(Injection.class)).findAny();

        if (injectionCunstructors.isPresent()) {
            Class[] parameterTypes = injectionCunstructors.get().getParameterTypes();
            Object[] parameterValus = new Object[parameterTypes.length];
            for (int i = 0; i < parameterTypes.length; i++) {
                Object isSim = sims.get(parameterTypes[i]);
                if (null == isSim) {
                    isSim = create(parameterTypes[i]);
                }
                parameterValus[i] = isSim;
            }
            value = injectionCunstructors.get().newInstance(parameterValus);
        } else {
            value = klass.getDeclaredConstructor().newInstance();
        }

        sims.put(klass, value);
        return value;

    }
}
