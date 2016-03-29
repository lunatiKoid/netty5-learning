package com.flyliang.netty.learning.ch2.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * Created by liang.yaol on 3/26/16.
 */
public class NettyTimerServer {

    public void bind(int port) throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap sbs = new ServerBootstrap();
            sbs.group(bossGroup,
                      workerGroup).channel(NioServerSocketChannel.class).option(ChannelOption.SO_BACKLOG,
                                                                                1024).childHandler(new ChildChannelHandler());

            ChannelFuture cf = sbs.bind(port).sync();
            System.out.println("server is coming");
            cf.channel().closeFuture().sync();
            System.out.println("server is closed");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    private class ChildChannelHandler extends ChannelInitializer<SocketChannel> {

        protected void initChannel(SocketChannel socketChannel) throws Exception {
            socketChannel.pipeline().addLast(new TimerServerHandler());
        }
    }

    public static void main(String[] args) {
        int port = 8080;
        if (args != null && args.length > 0) {
            try {
                port = Integer.valueOf(args[0]);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        try {
            new NettyTimerServer().bind(port);
            Thread.sleep(1000000);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
