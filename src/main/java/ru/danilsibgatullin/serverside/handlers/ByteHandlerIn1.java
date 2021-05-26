package ru.danilsibgatullin.serverside.handlers;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

//Класс обработчик in-1
public class ByteHandlerIn1 extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf = (ByteBuf) msg;
        System.out.println("buf: " + buf);
        StringBuilder sb = new StringBuilder();
        int byteCount=0; //первые 4 байта используем для команд
        //читаем команду
        while (buf.isReadable() && byteCount <5) {
            sb.append((char) buf.readByte());
            byteCount++;
        }
        String command = sb.toString();
        sb=new StringBuilder();
        //если команда начинается на -nfl , то значит передается файл и будем обрабатывать отдельно
        if(!"-nfl".equals(command)){
            sb.append(command);
            while (buf.isReadable()){
                sb.append((char) buf.readByte());
            }
            ctx.fireChannelRead(sb.toString());
        } else {
            ctx.fireChannelRead(buf);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }
}
