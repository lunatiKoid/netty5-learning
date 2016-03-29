package com.flyliang.netty.learning.ch2.client;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

/**
 * Created by liang.yaol on 3/26/16.
 */
public class TimerClientHandler extends ChannelHandlerAdapter {

    private final ByteBuf firstMsg;

    public TimerClientHandler(){
        // write
        byte[] req = "QUERY TIME ORDER".getBytes();
        firstMsg = Unpooled.buffer(req.length);
        firstMsg.writeBytes(req);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush(firstMsg);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("client channel read ...");
        ByteBuf buf = (ByteBuf) msg;
        byte[] req = new byte[buf.readableBytes()];
        buf.readBytes(req);
        String body = new String(req, "UTF-8");
        System.out.println("client's received body="+body);
    }
}
