package com.simple.boot.db.h2;

import com.simple.boot.SimpleApplication;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;

class H2ServerTest {
    @Test
    public void test() throws SQLException, InterruptedException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        new SimpleApplication().run(H2ServerTest.class);
//        Server server = Server.createTcpServer("-web","-tcpPort" ,"9092", "-tcpAllowOthers", "-baseDir", "~/temp").start();
        Thread.sleep(999999999l);
    }

}
