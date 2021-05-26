package ru.danilsibgatullin.serverside.handlers;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Path;

//Класс обработки загрузки файлов.

public class FileHandler extends SimpleChannelInboundHandler<ByteBuf> {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf) throws Exception {

        StringBuilder sb =new StringBuilder();
        char c=' ';
        //получаем путь где должен создаться файл и его имя. Путь и байты самого файла разделены символом |
        while(byteBuf.isReadable()&&'|'!=c){
            c=(char)byteBuf.readByte();
            sb.append(c);
        }
        String path = sb.toString();
        createFile(path); // создаем файл
        RandomAccessFile file =new RandomAccessFile(path,"rw"); //открываем файл на запись
        while (byteBuf.isReadable()){
            file.write(byteBuf.readByte());
        }

    }

    private void createFile(String path) throws IOException {
        Path newPath = Path.of(path);
        if (!Files.exists(newPath)) {
            Files.createFile(newPath);
        }
    }

}
