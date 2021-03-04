package com.simple.boot.web.netty.communication;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.simple.boot.throwable.ProcessingException;
import com.simple.boot.web.communication.Request;
import com.simple.boot.web.http.HttpMethod;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.QueryStringDecoder;
import io.netty.util.CharsetUtil;
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


    private final FullHttpRequest nettyRequest;

    public NettyRequest(FullHttpRequest request) {
        this.nettyRequest = request;
    }

    @Override
    public String param(String key) {
        return "";
//        return request.param(key);
    }

    @Override
    public HttpMethod method() {
        io.netty.handler.codec.http.HttpMethod method = nettyRequest.method();
        return HttpMethod.valueOf(method.name().toUpperCase());
    }

    @Override
    public Map<String, String> params() {
        return null;
//        return request.params();
    }

    @Override
    public String path() {
        try {
            URI uri = new URI(nettyRequest.uri());
            return uri.getPath();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return "";
//        return request.path();
    }

    @Override
    public String uri() {
        return nettyRequest.uri();
    }

    @Override
    public <T> T body(Class<T> klass) throws ProcessingException {
        ObjectMapper mapper = new ObjectMapper();
//        String json = "[{\"name\":\"mkyong\", \"age\":37}, {\"name\":\"fong\", \"age\":38}]";
        // 1. convert JSON array to Array objects
        try {
            if (byte[].class.isAssignableFrom(klass)) {
                return (T) nettyRequest.content().array();
            }
            String body = nettyRequest.content().toString(CharsetUtil.UTF_8);
            if (String.class.isAssignableFrom(klass)) {
                return (T) body;
            }
            return mapper.readValue(body, klass);
        } catch (Exception e) {
            throw new ProcessingException(e);
        }
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
