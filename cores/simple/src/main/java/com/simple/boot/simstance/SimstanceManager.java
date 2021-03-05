package com.simple.boot.simstance;


import com.simple.boot.anno.*;
import com.simple.boot.proxy.SimProxyMethodHandler;
import com.simple.boot.util.ProxyUtils;
import com.simple.boot.util.ReflectionUtils;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.reflections.Reflections;
import org.reflections.scanners.TypeAnnotationsScanner;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
@Setter
@Slf4j
public class SimstanceManager {

    private final String SIMPLE_BASE_PACKAGE = "com.simple.boot";
    public static SimstanceManager instance;
    private final Class startClass;
    public LinkedHashMap<Class, Object> sims;

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

        // scan
        for (String pit : Arrays.asList(SIMPLE_BASE_PACKAGE, startClass.getPackage().getName())) {
            Reflections reflections = new Reflections(pit, new TypeAnnotationsScanner());
            reflections.getTypesAnnotatedWith(Sim.class, true).stream().forEach(it -> sims.put(it, null));
            reflections.getTypesAnnotatedWith(Service.class, true).stream().forEach(it -> sims.put(it, null));
            reflections.getTypesAnnotatedWith(Controller.class, true).stream().forEach(it -> sims.put(it, null));

            List<Class<?>> configs = reflections.getTypesAnnotatedWith(Config.class, true).stream().sorted(Comparator.comparingInt(s -> s.getAnnotation(Config.class).order())).collect(Collectors.toList());
            configs.stream().forEach(it -> sims.put(it, null));

        }

//        List<Class> collect = sims.keySet().stream().filter(it -> it.isAnnotationPresent(Config.class)).sorted((it, sit) -> {
//            return Integer.compare(((Config) it.getAnnotation(Config.class)).order(), ((Config) sit.getAnnotation(Config.class)).order());
//        }).collect(Collectors.toList());
//        List<Class> collect = sims.keySet().stream().filter(it -> it.isAnnotationPresent(Config.class)).sorted(Comparator.comparingInt(it -> ((Config) it.getAnnotation(Config.class)).order())).collect(Collectors.toList());
//        for (Class aClass : collect) {
//            create(aClass);
//        }

        for (Class klass : sims.keySet()) {
            Object obj = create(klass);
        }





        //@PostConstruct 처리.
        Set<Map.Entry<Method, Object>> posts = getMethodAnnotation(PostConstruct.class, Comparator.comparingInt(PostConstruct::order)).entrySet();
        for (Map.Entry<Method, Object> post : posts) {
            Method method = post.getKey();
            Object obj = post.getValue();
            Injection annotation = method.getAnnotation(Injection.class);
            Object[] params = new Object[0];
            if (null != annotation) {
                params = getOrCreateSims(method.getParameterTypes());
            }
            ReflectionUtils.invoke(method, obj, params);
        }

    }

    //    public <T extends Annotation> Map<Class, Object> addScanSim(Class<T> annotation) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
//    }
//    @Deprecated
//    public <T extends Annotation> List<Object> addScanSim(Class<T> annotation, Comparator<T> comparator) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
//        Map<Class, Object> rsims = new LinkedHashMap<>();
//        for (String pit : Arrays.asList(SIMPLE_BASE_PACKAGE, startClass.getPackage().getName())) {
//            Reflections reflections = new Reflections(pit, new TypeAnnotationsScanner());
//            reflections.getTypesAnnotatedWith(annotation, true).stream().forEach(it -> {
////                sims.put(it, null);
//                rsims.put(it, null);
//            });
//        }
//
//        List<Class> collect = rsims.keySet().stream().sorted((s1, s2) -> {
//            return comparator.compare((T) s1.getAnnotation(annotation), (T) s2.getAnnotation(annotation));
//        }).collect(Collectors.toList());
//
//
//        List<Object> rtn = new ArrayList<>();
//        for (Class klass : collect) {
//            rtn.add(create(klass));
//        }
//        return rtn;
//    }

    public Object create(Class klass) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Object obj = sims.get(klass);
        if (null != obj) {
            return obj;
        }
        Object sim = null;
        Optional<Constructor> injectionCunstructors = Stream.of(klass.getConstructors()).filter(it -> it.isAnnotationPresent(Injection.class)).findAny();
        if (injectionCunstructors.isPresent()) {
            Class[] parameterTypes = injectionCunstructors.get().getParameterTypes();
            Object[] parameterValus = getOrCreateSims(parameterTypes);
//            sim = ReflectionUtils.newInstance(injectionCunstructors.get(), parameterValus);
            sim = ProxyUtils.newInstanceMethodProxy(klass, parameterTypes, parameterValus , new SimProxyMethodHandler(this));
        } else {
//            sim = ReflectionUtils.newInstance(klass.getDeclaredConstructor());
            sim = ProxyUtils.newInstanceMethodProxy(klass, new Class[0], new Object[0] , new SimProxyMethodHandler(this));
        }
        sims.put(klass, sim);
        return sim;
    }

    public Object invokeInjectionAnnotaionParameterGetOrCreateSims(Method method, Object obj) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        return ReflectionUtils.invoke(method, obj, injectionAnnotaionParameterGetOrCreateSims(method));
    }
    public Object[] injectionAnnotaionParameterGetOrCreateSims(Method method) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        Object[] params = new Object[0];
        Injection annotation = method.getAnnotation(Injection.class);
        if (null != annotation) {
            params = getOrCreateSims(method.getParameterTypes());
        }
        return params;
    }
    public Object[] getOrCreateSims(Class[] parameterTypes) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        Object[] parameterValus = new Object[parameterTypes.length];
        for (int i = 0; i < parameterTypes.length; i++) {
            //search
            Class parameterType = parameterTypes[i];
//                Optional<Object> first = sims.entrySet().stream().filter(it -> parameterType.isAssignableFrom(it.getKey())).map(it -> it.getValue()).map(Optional::ofNullable).findFirst().flatMap(Function.identity());
            Map.Entry<Class, Object> first = sims.entrySet().stream().filter(it -> parameterType.isAssignableFrom(it.getKey())).findFirst().get();
            if (null == first.getValue()) {
                parameterValus[i] = create(first.getKey());
            } else {
                parameterValus[i] = first.getValue();
            }
        }
        return parameterValus;
    }

    public <T extends Annotation> LinkedHashMap<Method, Object> getMethodAnnotation(Class<T> methodAnnotationClass) {
        return getMethodAnnotation(methodAnnotationClass, null);
    }
    public <T extends Annotation> LinkedHashMap<Method, Object> getMethodAnnotation(Class<T> methodAnnotationClass, Comparator<T> comparator) {
        LinkedHashMap<Method, Object> rtn = new LinkedHashMap<>();

        /*
            for (Map.Entry<Class, Object> sims : getSims().entrySet()) {
            Class key = sims.getKey();
            Object value = sims.getValue();
            if(null == value) {
            value = create(key);
            }
            Object finalValue = value;
            Arrays.stream(key.getDeclaredMethods()).filter(sit -> sit.isAnnotationPresent(methodAnnotationClass)).forEach(sit -> rtn.put(sit, finalValue));
            }
         */
        getSims().entrySet().stream().forEach(it -> {
            Arrays.stream(it.getKey().getDeclaredMethods()).filter(sit -> sit.isAnnotationPresent(methodAnnotationClass)).forEach(sit -> rtn.put(sit, it.getValue()));
        });


        Stream<Map.Entry<Method, Object>> stream = rtn.entrySet().stream();
        if(null != comparator) {
//                .sorted(Map.Entry.comparingByValue((v1,v2)->v2.compareTo(v1)))
            stream = stream.sorted((s1, s2) -> comparator.compare(s1.getKey().getAnnotation(methodAnnotationClass), s2.getKey().getAnnotation(methodAnnotationClass)));
        }
        return stream.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (v1, v2) -> v1, LinkedHashMap::new));
    }


    public <T> LinkedHashMap<Class<T>, T> getSimsNotNullObjAndIsAssignableFrom(Class<T> klass) {
        return getSims((e) -> {
            return null != e.getKey() && null != e.getValue() && klass.isAssignableFrom(e.getKey());
        }).entrySet().stream().collect(Collectors.toMap((it) -> {
            return (Class<T>) it.getKey();
        }, (it) -> (T)it.getValue(), (x, y) -> y, LinkedHashMap::new));
    }
    public LinkedHashMap<Class, Object> getSims(Predicate<Map.Entry<Class, Object>> test) {
        return this.getSims().entrySet().stream()
                .filter(test)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (x, y) -> y, LinkedHashMap::new));
    }
}
