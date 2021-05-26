package ru.danilsibgatullin.serverside.handlers;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import ru.danilsibgatullin.serverside.services.FileService;

/*
 Класс обработчик потока данных с типом String ,
 после зарезервированных под системные комады 4 байт
 */
public class StringCommandHandler extends SimpleChannelInboundHandler<String> {

    private FileService fileService;

    public StringCommandHandler(FileService fs){
        fileService=fs; // в конструктуре принимаем объект обработчика операций с файлами
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String str) throws Exception {
        str = str.replace("\r\n","");  //для telnet подключения
        if (str.startsWith("mkdr")){
            System.out.println(str);
            fileService.mkDir(str.substring(5));
        } else if(str.startsWith("--rm")) {
            fileService.delDir(str.substring(5));
        }

    }

}
