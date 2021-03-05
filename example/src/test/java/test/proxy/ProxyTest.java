package test.proxy;

import com.example.service.AdminService;
import com.simple.boot.proxy.SimProxyMethodHandler;
import com.simple.boot.util.ProxyUtils;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;

public class ProxyTest {
    @Test
    public void test() throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        AdminService adminService = ProxyUtils.newInstanceMethodProxy(AdminService.class, new TestProxyMethodHandler());
        adminService.print();
    }
}
