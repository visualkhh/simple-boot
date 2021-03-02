package com.simple.boot.web.netty.communication;

import com.simple.boot.web.communication.Request;
import com.simple.boot.web.http.HttpMethod;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.QueryStringDecoder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Getter
@Setter
public class NettyRequest implements Request {


    private final FullHttpRequest request;
    String body;

    public NettyRequest(FullHttpRequest request) {
        this.request = request;
    }

    @Override
    public String param(String key) {
        return "";
//        return request.param(key);
    }

    @Override
    public HttpMethod method() {
        return null;
    }

    @Override
    public Map<String, String> params() {
        return null;
//        return request.params();
    }

    @Override
    public String path() {
        try {
            URI uri = new URI(request.uri());
            return uri.getPath();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return "";
//        return request.path();
    }

    @Override
    public String uri() {
        return request.uri();
    }

    @Override
    public <T> T body(Class<T> klass) {
        return (T) body;
    }
//    @Override
//    public <T> T body(Class<T> klass) {
//        ByteBufFlux retain = request.receive().retain();
//        retain.asByteBuffer().map(it -> {
//           it
//        });
//        Flux<String> logsss = retain.asString().map(s -> s + "--fff").log("http-server");
////        new Thread(() -> {
////            String s = logsss.blockFirst();
////            log.info("-->{}",s);
//////            String block = retain.aggregate().asString().block();
//////            NettyRequest.log.info("rrr->{}",block);
////        }).start();
//
////        retain.aggregate().subscribe(it -> {
////            log.info("---> {}",it);
////        });
////        ByteBuf byteBuf = request.receive().retain().blockLast();
////        log.info("---> {}",byteBuf);
////        log.info("---> {}",block);
//        request.receive().retain().subscribe(it -> {
//            NettyRequest.log.info("-->{}",it);
//        });
////        request.receiveContent().blockFirst()
//        return null;
//    }
//
//    @Override
//    public <T> Flux<T> bodyFlux(Class<T> klass) {
//        return request.receive().asString(StandardCharsets.UTF_8).map(it -> {
////            ModelMapper modelMapper = new ModelMapper();
////            modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
//            return (T) it;
//        });
//    }


    @Override
    public Map<String, List<String>> queryParameters() {
        QueryStringDecoder query = new QueryStringDecoder(uri());
        return query.parameters();
    }

    @Override
    public Map<String, String> queryFirstParameters() {
        QueryStringDecoder query = new QueryStringDecoder(uri());
        // https://stackoverflow.com/questions/25903137/java8-hashmapx-y-to-hashmapx-z-using-stream-map-reduce-collector/25903190
        return query.parameters().entrySet().stream().collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue().get(0)));
    }
}
