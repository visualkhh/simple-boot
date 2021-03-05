package test.proxy;

import javassist.util.proxy.MethodHandler;

import java.lang.reflect.Method;

public class TestProxyMethodHandler implements MethodHandler {

    public TestProxyMethodHandler() {
    }

    @Override
    public Object invoke(Object self, Method thisMethod, Method proceed, Object[] args) throws Throwable {


        System.out.println("Test Handling " + thisMethod + " via the method handler");
        return proceed.invoke(self, args);
    }
}
