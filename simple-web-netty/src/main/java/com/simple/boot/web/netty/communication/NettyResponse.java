package com.simple.boot.web.netty.communication;

import com.simple.boot.web.communication.Response;
import com.simple.boot.web.http.HttpHeaderNames;
import com.simple.boot.web.http.HttpHeaderValues;
import com.simple.boot.web.http.HttpStatus;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static io.netty.handler.codec.http.HttpHeaderNames.CONTENT_LENGTH;
import static io.netty.handler.codec.http.HttpHeaderNames.CONTENT_TYPE;
import static io.netty.handler.codec.http.HttpHeaderValues.TEXT_PLAIN;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;

@Getter
@Setter
public class NettyResponse implements Response<byte[]> {


    private final NettyRequest request;
    public Map<String, String> header = new HashMap<>();
    private int status;
    byte[] body;

    public NettyResponse(NettyRequest request) {
        this.request = request;
    }

    @Override
    public void putHeader(HttpHeaderNames header, HttpHeaderValues value) {
        putHeader(header.value(), value.value());
    }

    @Override
    public void putHeader(String header, String value) {
        this.header.put(header, value);
    }

    @Override
    public void status(HttpStatus httpStatus) {
        this.status(httpStatus.value());
    }

    @Override
    public void status(int status) {
        this.status = status;
//        response.status(value);
    }

    @Override
    public void body(byte[] body) {
        this.body = body;
    }

    public FullHttpResponse toFullHttpResponse() {
        HttpResponseStatus status = Optional.ofNullable(HttpResponseStatus.valueOf(this.status)).orElse(OK);
        DefaultFullHttpResponse resp = new DefaultFullHttpResponse(request.getNettyRequest().protocolVersion(), status, Unpooled.wrappedBuffer(getBody()));
        this.header.forEach((k, v) -> resp.headers().set(k, v));
        resp.headers().setInt(CONTENT_LENGTH, resp.content().readableBytes());
        return resp;

    }
}
