package com.simple.boot.web.communication.netty;

import com.simple.boot.web.communication.Request;
import io.netty.handler.codec.http.QueryStringDecoder;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.netty.ByteBufFlux;
import reactor.netty.http.server.HttpServerRequest;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

@Slf4j
public class NettyRequest implements Request {

    private final HttpServerRequest request;

    public NettyRequest(HttpServerRequest request) {
        this.request = request;
    }

    @Override
    public String param(String key) {
        return request.param(key);
    }

    @Override
    public Map<String, String> params() {
        return request.params();
    }

    @Override
    public String path() {
        return request.path();
    }

    @Override
    public String uri() {
        return request.uri();
    }

    @Override
    public <T> T body(Class<T> klass) {
        ByteBufFlux retain = request.receive().retain();
        Flux<String> logsss = retain.asString().map(s -> s + "--fff").log("http-server");
        new Thread(() -> {
            String s = logsss.blockFirst();
            log.info("-->{}",s);
//            String block = retain.aggregate().asString().block();
//            NettyRequest.log.info("rrr->{}",block);
        }).start();

//        retain.aggregate().subscribe(it -> {
//            log.info("---> {}",it);
//        });
//        ByteBuf byteBuf = request.receive().retain().blockLast();
//        log.info("---> {}",byteBuf);
//        log.info("---> {}",block);
        request.receive().retain().subscribe(it -> {
            NettyRequest.log.info("-->{}",it);
        });
//        request.receiveContent().blockFirst()
        return null;
    }

    @Override
    public <T> Flux<T> bodyFlux(Class<T> klass) {
        return request.receive().asString(StandardCharsets.UTF_8).map(it -> {
//            ModelMapper modelMapper = new ModelMapper();
//            modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
            return (T) it;
        });
    }


    @Override
    public Map<String, List<String>> queryParameters() {
        QueryStringDecoder query = new QueryStringDecoder(uri());
        return query.parameters();
    }
}
