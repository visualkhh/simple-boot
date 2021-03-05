package test.proxy;

import javassist.util.proxy.MethodFilter;
import javassist.util.proxy.MethodHandler;
import javassist.util.proxy.ProxyFactory;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class ProxyFactoryTest {

    public static abstract class Dog {
        public void bark() {
            System.out.println("Woof!");
        }
        public abstract void fetch();
    }

    public static class Pig {
        public void bark() {
            System.out.println("Woof!");
        }
        public void fetch() {
            System.out.println("pig fetch");
        };
    }
    public static class Horse {

        private final String name;

        public Horse(String name) {
            this.name = name;
        }

        public void bark() {
            System.out.println("Horse! "+name);
        }
        public void fetch() {
            System.out.println("Horse fetch "+name);
        };
    }

    @Test
    public void test() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        ProxyFactory factory = new ProxyFactory();
        factory.setSuperclass(Dog.class);
        factory.setFilter(
                new MethodFilter() {
                    @Override
                    public boolean isHandled(Method method) {
                        return Modifier.isAbstract(method.getModifiers());
                    }
                }
        );

        MethodHandler handler = new MethodHandler() {
            @Override
            public Object invoke(Object self, Method thisMethod, Method proceed, Object[] args) throws Throwable {
                System.out.println("Handling " + thisMethod + " via the method handler");
                return null;
            }
        };

        Dog dog = (Dog) factory.create(new Class<?>[0], new Object[0], handler);
        dog.bark();
        dog.fetch();
    }


    @Test
    public void pig() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        ProxyFactory factory = new ProxyFactory();
        factory.setSuperclass(Pig.class);
//        factory.setFilter(
//                new MethodFilter() {
//                    @Override
//                    public boolean isHandled(Method method) {
//                        return Modifier.isAbstract(method.getModifiers());
//                    }
//                }
//        );

        MethodHandler handler = new MethodHandler() {
            @Override
            public Object invoke(Object self, Method thisMethod, Method proceed, Object[] args) throws Throwable {
                System.out.println("Handling " + thisMethod + " via the method handler");
                proceed.invoke(self, args);
                return null;
            }
        };

        Pig dog = (Pig) factory.create(new Class<?>[0], new Object[0], handler);
        dog.bark();
        dog.fetch();
    }

    @Test
    public void horse() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        ProxyFactory factory = new ProxyFactory();
        factory.setSuperclass(Horse.class);
//        factory.setFilter(
//                new MethodFilter() {
//                    @Override
//                    public boolean isHandled(Method method) {
//                        return Modifier.isAbstract(method.getModifiers());
//                    }
//                }
//        );

        MethodHandler handler = new MethodHandler() {
            @Override
            public Object invoke(Object self, Method thisMethod, Method proceed, Object[] args) throws Throwable {
                System.out.println("Handling " + thisMethod + " via the method handler");
                proceed.invoke(self, args);
                return null;
            }
        };

//        Horse horse = (Horse) factory.create(new Class<?>[0], new Object[0], handler);
        Horse horse = (Horse) factory.create(new Class<?>[]{String.class}, new Object[]{"horse"}, handler);
        horse.bark();
        horse.fetch();
    }
}
