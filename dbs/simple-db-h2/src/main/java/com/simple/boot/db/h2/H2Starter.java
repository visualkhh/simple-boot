package com.simple.boot.db.h2;

import com.simple.boot.anno.Config;
import com.simple.boot.anno.Injection;
import com.simple.boot.db.h2.config.H2Config;
import com.simple.boot.starter.Starter;
import lombok.extern.slf4j.Slf4j;
import org.h2.tools.Server;

import java.sql.SQLException;

@Slf4j
@Config(order = -1_020_000)
public class H2Starter extends Starter {

    private final H2Config config;

    @Injection
    public H2Starter(H2Config config) throws SQLException {
        this.config = config;
        init();
    }

    private void init() throws SQLException {
//        Server.createTcpServer("-tcpPort", "9092", "-tcpAllowOthers").start();
//        Server server =  Server.createTcpServer("-tcpAllowOthers").start();
        Server server = Server.createTcpServer("-web","-tcpPort" , String.valueOf(config.getPort()), "-tcpAllowOthers").start();
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
