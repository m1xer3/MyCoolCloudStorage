package ru.danilsibgatullin.handlers;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import ru.danilsibgatullin.models.UserChanel;
import ru.danilsibgatullin.services.FileService;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Path;

//Класс обработки загрузки файлов.

public class FileUploadHandler extends SimpleChannelInboundHandler<ByteBuf> {

    private UserChanel chanel;

    public FileUploadHandler(UserChanel ch){
        this.chanel=ch;
    }


    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf) throws Exception {
        FileService fs =chanel.getFs();
        StringBuilder sb =new StringBuilder();
        char c=' ';
        //получаем путь где должен создаться файл и его имя. Путь и байты самого файла разделены символом |
        while(byteBuf.isReadable()&&'|'!=c){
            c=(char)byteBuf.readByte();
            if('|'!=c){
                sb.append(c);
            }
        }
        String path =chanel.getCurrentPath()+"/"+sb.toString();
        fs.createFile(path); // создаем файл
        RandomAccessFile file =new RandomAccessFile(path,"rw"); //открываем файл на запись
        while (byteBuf.isReadable()){
            file.write(byteBuf.readByte());
        }
    }



}
