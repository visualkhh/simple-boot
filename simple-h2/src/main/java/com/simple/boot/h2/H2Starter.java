package com.simple.boot.h2;

import com.simple.boot.anno.Config;
import com.simple.boot.anno.Injection;
import com.simple.boot.anno.Sim;
import com.simple.boot.config.ConfigLoader;
import com.simple.boot.h2.config.H2Config;
import lombok.extern.slf4j.Slf4j;
import org.h2.tools.Server;

import java.sql.SQLException;
import java.util.Map;

@Slf4j
@Config
public class H2Starter {

    private final ConfigLoader configLoader;

    @Injection
    public H2Starter(ConfigLoader configLoader) throws SQLException {
        this.configLoader = configLoader;
        init();
    }

    private void init() throws SQLException {
//        Server.createTcpServer("-tcpPort", "9092", "-tcpAllowOthers").start();
//        Server server =  Server.createTcpServer("-tcpAllowOthers").start();
        H2Config h2 = configLoader.get("h2", H2Config.class);
        Server server = Server.createTcpServer("-web","-tcpPort" ,String.valueOf(h2.getPort()), "-tcpAllowOthers").start();
//        int port = 5555;
//        Server.createTcpServer(
//                "-tcp",
//                "-tcpAllowOthers",
//                "-ifNotExists",
//                "-tcpPort", port+"").start();
//
//        return Server.createTcpServer(
//                "-tcp",
//                "-tcpAllowOthers",
//                "-ifNotExists",
//                "-tcpPort", port+"", "-key", externalDbName, db_store.value2(dbname)).start();
    }
}
