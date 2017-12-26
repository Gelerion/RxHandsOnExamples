package com.gelerion.learning.rx.v5.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * Created by denis.shuvalov on 19/12/2017.
 *
 * This is the most basic HTTP server in Netty. The crucial part is bossGroup pool responsible
 * for accepting incoming connections and workerGroup that processes events.
 */
public class HttpTcpNettyServer {

    public static void main(String[] args) throws InterruptedException {
        NioEventLoopGroup bossGroup = new NioEventLoopGroup(1);
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        //We did not specify yet what the server should do, apart from listening on port 8080.
        //This is configurable via ChannelInitializer (HttpInitializer)
        try {
            new ServerBootstrap()
                    .option(ChannelOption.SO_BACKLOG, 50_000) //maximum queue length for incoming connection indication
                    .group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new HttpInitializer())
                    .bind(8080)
                    .sync()
                    .channel()
                    .closeFuture()
                    .sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

}
