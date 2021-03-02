package com.simple.boot.web.netty.communication;

import com.simple.boot.web.communication.Response;
import io.netty.handler.codec.http.FullHttpResponse;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class NettyResponse implements Response<byte[]> {


    public Map<String, String> header = new HashMap<>();
    private int status;
    byte[] body;

    public NettyResponse() {
//        this.response = response;
    }

    @Override
    public void header(String header, String value) {
        this.header.put(header, value);
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
}
