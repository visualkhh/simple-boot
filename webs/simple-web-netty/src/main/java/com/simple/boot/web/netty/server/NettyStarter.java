package com.simple.boot.web.netty.server;

import com.simple.boot.anno.Config;
import com.simple.boot.anno.Injection;
import com.simple.boot.starter.Starter;
import com.simple.boot.web.config.WebConfig;
import com.simple.boot.web.dispatch.Dispatcher;
import com.simple.boot.web.server.WebServer;
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
@Config
public class NettyStarter extends Starter implements WebServer {
    private Dispatcher dispatcher;
    private WebConfig config;

    @Injection
    public NettyStarter(Dispatcher dispatcher, WebConfig config) throws Exception {
        this.dispatcher = dispatcher;
        this.config = config;
        init();
    }

    private void init() throws Exception {
    }

    @Override
    public void run()  {
        try {
            Boolean SSL = config.getSsl();
            Integer PORT = config.getPort();

            // Configure SSL.
            final SslContext sslCtx;
            if (null != SSL && SSL) {
                SelfSignedCertificate ssc = new SelfSignedCertificate();
                sslCtx = SslContextBuilder.forServer(ssc.certificate(), ssc.privateKey()).build();
            } else {
                sslCtx = null;
            }

            // Configure the server.
            EventLoopGroup bossGroup = new NioEventLoopGroup(1);
            EventLoopGroup workerGroup = new NioEventLoopGroup();
            try {
                ServerBootstrap serverBootstrap = new ServerBootstrap();
                serverBootstrap.option(ChannelOption.SO_BACKLOG, 1024);
                serverBootstrap.group(bossGroup, workerGroup)
                        .channel(NioServerSocketChannel.class)
                        .handler(new LoggingHandler(LogLevel.INFO))
                        .childHandler(new HttpHelloWorldServerInitializer(sslCtx, dispatcher));

                Channel ch = serverBootstrap.bind(PORT).sync().channel();
                log.info("Open your web browser and navigate to " +
                        (SSL ? "https" : "http") + "://127.0.0.1:" + PORT + '/');
                ch.closeFuture().sync();
            } finally {
                bossGroup.shutdownGracefully();
                workerGroup.shutdownGracefully();
            }
        }catch (Exception e){
            log.error("server runing error", e);
        }
    }
}
