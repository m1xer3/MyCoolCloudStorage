package ru.danilsibgatullin.handlers;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;

import java.nio.charset.StandardCharsets;

public class AuthorityHandlerOut extends ChannelOutboundHandlerAdapter {
    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        if (msg instanceof Boolean){
            ByteBuf buf = ctx.alloc().directBuffer();
            Boolean isAythority = (Boolean)msg;
            if (isAythority){
                buf.writeBytes("true".getBytes(StandardCharsets.UTF_8));
            }else{
                buf.writeBytes("false".getBytes(StandardCharsets.UTF_8));
            }
            ctx.writeAndFlush(buf);
        }
    }
}
