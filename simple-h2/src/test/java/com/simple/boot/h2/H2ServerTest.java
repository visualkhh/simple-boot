package com.simple.boot.h2;

import com.simple.boot.bootstrap.SimpleApplication;
import org.h2.tools.Server;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class H2ServerTest {
    @Test
    public void test() throws SQLException, InterruptedException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        new SimpleApplication().run(H2ServerTest.class);
//        Server server = Server.createTcpServer("-web","-tcpPort" ,"9092", "-tcpAllowOthers", "-baseDir", "~/temp").start();
        Thread.sleep(999999999l);
    }

}
