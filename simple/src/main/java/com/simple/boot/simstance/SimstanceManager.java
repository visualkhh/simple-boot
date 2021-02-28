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

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

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
//        Reflections reflections = new Reflections(new ConfigurationBuilder().setUrls(ClasspathHelper.forPackage(klass.getPackage().getName())).setScanners(new MethodAnnotationsScanner()));
//        Reflections reflections = new Reflections(new ConfigurationBuilder().setUrls(ClasspathHelper.forClass(klass)), new MethodAnnotationsScanner());
        Reflections reflections = new Reflections(klass.getPackage().getName(), new MethodAnnotationsScanner());
        Optional<Constructor> injectionCunstructors = reflections.getConstructorsAnnotatedWith(Injection.class).stream().filter(it -> klass == it.getDeclaringClass()).findAny();
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
