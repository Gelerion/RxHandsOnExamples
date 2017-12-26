package com.gelerion.learning.rx.v5.netty;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;

import static io.netty.handler.codec.http.HttpHeaderNames.CONTENT_LENGTH;
import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * Created by denis.shuvalov on 19/12/2017.
 *
 * the business logic component that actually handles the request rather than just intercepting or enriching it.
 * Although HttpServerCodec is inherently stateful (it translates incoming packets to high-level HttpRequest instances),
 * our custom HttpHandler can be a stateless singleton
 */
@Sharable
public class HttpHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof HttpRequest) {
            sendResponse(ctx);
        }
    }

    private void sendResponse(ChannelHandlerContext ctx) {
        DefaultFullHttpResponse response = new DefaultFullHttpResponse(
                HttpVersion.HTTP_1_1,
                HttpResponseStatus.OK,
                Unpooled.wrappedBuffer("OK".getBytes(UTF_8)));
        response.headers().add(CONTENT_LENGTH, 2);

        //Channel is an abstraction over a communication link—for example, an HTTP connection
        ctx.writeAndFlush(response);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
