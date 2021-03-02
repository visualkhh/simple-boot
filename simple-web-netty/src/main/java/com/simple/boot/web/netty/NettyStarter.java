package com.simple.boot.web.netty;

import com.simple.boot.anno.Config;
import com.simple.boot.anno.Injection;
import com.simple.boot.config.ConfigLoader;
import com.simple.boot.simstance.SimstanceManager;
import com.simple.boot.starter.Starter;
import com.simple.boot.web.config.WebConfig;
import com.simple.boot.web.netty.server.HttpHelloWorldServerInitializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.SelfSignedCertificate;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Config(order = -1_010_000)
public class NettyStarter extends Starter {
    private SimstanceManager simstanceManager;
    private ConfigLoader configLoader;

    @Injection
    public NettyStarter(SimstanceManager simstanceManager, ConfigLoader configLoader) throws Exception {
        this.simstanceManager = simstanceManager;
        this.configLoader = configLoader;
        init();
    }

    private void init() throws Exception {

        WebConfig webConfig = this.configLoader.get(WebConfig.prefix, WebConfig.class);
        Boolean SSL = webConfig.getSsl();
        Integer PORT = webConfig.getPort();

        // Configure SSL.
        final SslContext sslCtx;
        if (SSL) {
            SelfSignedCertificate ssc = new SelfSignedCertificate();
            sslCtx = SslContextBuilder.forServer(ssc.certificate(), ssc.privateKey()).build();
        } else {
            sslCtx = null;
        }

        // Configure the server.
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.option(ChannelOption.SO_BACKLOG, 1024);
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new HttpHelloWorldServerInitializer(sslCtx, simstanceManager));

            Channel ch = b.bind(PORT).sync().channel();

            log.info("Open your web browser and navigate to " +
                    (SSL? "https" : "http") + "://127.0.0.1:" + PORT + '/');

            ch.closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }


    }
}
