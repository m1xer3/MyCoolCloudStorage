package ru.danilsibgatullin.serverside.handlers;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class StringCommandHandler extends SimpleChannelInboundHandler<String> {

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, String str) throws Exception {
        if (str.startsWith("mkdr")){
            System.out.println(str);
            mkDir(str.substring(0,4));
        }
    }

    private void mkDir(String newDir) throws IOException {
        Files.createDirectory(Path.of(newDir));
        System.out.println("dir create");
    }
}
