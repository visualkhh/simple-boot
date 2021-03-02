/*
 * Copyright 2013 The Netty Project
 *
 * The Netty Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package com.simple.boot.web.netty.server;

import com.simple.boot.simstance.SimstanceManager;
import com.simple.boot.web.communication.Request;
import com.simple.boot.web.communication.Response;
import com.simple.boot.web.netty.communication.NettyRequest;
import com.simple.boot.web.netty.communication.NettyResponse;
import com.simple.boot.web.netty.dispatch.NettyDispatcher;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import lombok.extern.slf4j.Slf4j;

import java.net.URI;
import java.nio.charset.StandardCharsets;

import static io.netty.handler.codec.http.HttpHeaderNames.*;
import static io.netty.handler.codec.http.HttpHeaderValues.CLOSE;
import static io.netty.handler.codec.http.HttpHeaderValues.KEEP_ALIVE;
import static io.netty.handler.codec.http.HttpHeaderValues.TEXT_PLAIN;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;

@Slf4j
public class HttpHelloWorldServerHandler extends SimpleChannelInboundHandler<FullHttpRequest> {
    private static final byte[] CONTENT = {'H', 'e', 'l', 'l', 'o', ' ', 'W', 'o', 'r', 'l', 'd'};
    private final SimstanceManager simstanceManager;
    private final NettyDispatcher nettyDispatcher;

    public HttpHelloWorldServerHandler(SimstanceManager simstanceManager) {
        this.simstanceManager = simstanceManager;
        nettyDispatcher = new NettyDispatcher(this.simstanceManager);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, FullHttpRequest req) {
//        ByteBuf data = msg.content();
//        String s = data.toString(StandardCharsets.UTF_8);
//        log.info("POST/PUT : " + s);
//          https://stackoverflow.com/questions/36474734/java-netty-get-post-request-content

        Request request = new NettyRequest(req);
        NettyResponse response = new NettyResponse();

        nettyDispatcher.executeMapping(request, response);

        boolean keepAlive = HttpUtil.isKeepAlive(req);
        FullHttpResponse resp = new DefaultFullHttpResponse(req.protocolVersion(), OK,
                Unpooled.wrappedBuffer(response.getBody()));
        resp.headers()
                .set(CONTENT_TYPE, TEXT_PLAIN)
                .setInt(CONTENT_LENGTH, resp.content().readableBytes());

        if (keepAlive) {
            if (!req.protocolVersion().isKeepAliveDefault()) {
                resp.headers().set(CONNECTION, KEEP_ALIVE);
            }
        } else {
            // Tell the client we're going to close the connection.
            resp.headers().set(CONNECTION, CLOSE);
        }

        ChannelFuture f = ctx.write(resp);

        if (!keepAlive) {
            f.addListener(ChannelFutureListener.CLOSE);
        }

//        Request request = new NettyRequest(req);
//        NettyResponse response = new NettyResponse();
//
//        nettyDispatcher.executeMapping(request, response);
//
//
//        FullHttpResponse resp = new DefaultFullHttpResponse(req.protocolVersion(), HttpResponseStatus.valueOf(response.getStatus()),
//                Unpooled.wrappedBuffer(response.getBody()));
//
//        resp.headers()
//                .set(CONTENT_TYPE, TEXT_PLAIN)
//                .setInt(CONTENT_LENGTH, resp.content().readableBytes());
//
//        boolean keepAlive = HttpUtil.isKeepAlive(req);
//        if (keepAlive) {
//            if (!req.protocolVersion().isKeepAliveDefault()) {
//                resp.headers().set(CONNECTION, HttpHeaderValues.KEEP_ALIVE);
//            }
//        } else {
//            // Tell the client we're going to close the connection.
//            resp.headers().set(CONNECTION, CLOSE);
//        }
//
//        ChannelFuture f = ctx.write(response);
//
//        if (!keepAlive) {
//            f.addListener(ChannelFutureListener.CLOSE);
//        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
