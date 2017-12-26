package com.gelerion.learning.rx.v5.netty;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;


/**
 * Created by denis.shuvalov on 19/12/2017.
 *
 * Rather than providing a single function that handles the connection, we build a pipeline that processes
 * incoming ByteBuf instances as they arrive. The first step of the pipeline decodes raw incoming bytes into
 * higher-level HTTP request objects. This handler is built-in. It is also used for encoding the HTTP response
 * back to raw bytes. In more robust applications you will often see more handlers focused on smaller functionality;
 * for example, frame decoding, protocol decoding, security, and so on. Every piece of data and notification
 * flows through this pipeline.
 */
public class HttpInitializer extends ChannelInitializer<SocketChannel> {

    private final HttpHandler httpHandler = new HttpHandler();

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ch
                .pipeline()
                .addLast(new HttpServerCodec())
                .addLast(httpHandler);
    }
}
