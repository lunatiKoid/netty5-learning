package com.flyliang.netty.learning.ch2.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * Created by liang.yaol on 3/26/16.
 */
public class NettyTimerClient {

    public void connect(int port, String host) throws Exception {
        EventLoopGroup group = new NioEventLoopGroup();

        try {
            Bootstrap bs = new Bootstrap();
            bs.group(group).channel(NioSocketChannel.class).option(ChannelOption.TCP_NODELAY,
                                                                   true).handler(new ChannelInitializer<SocketChannel>() {

                                                                       protected void initChannel(SocketChannel socketChannel) throws Exception {
                                                                           socketChannel.pipeline().addLast(new TimerClientHandler());
                                                                       }
                                                                   });
            ChannelFuture cf = bs.connect(host, port).sync();
            System.out.println("client is coming");
            cf.channel().closeFuture().sync();
            System.out.println("client is closed");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            group.shutdownGracefully();
        }
    }

    public static void main(String[] args) {
        int port = 8080;
        if (args != null && args.length > 0) {
            try {
                port = Integer.valueOf(args[0]);
            } catch (Exception e) {
            }
        }
        try {
            new NettyTimerClient().connect(port, "127.0.0.1");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
