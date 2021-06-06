package ru.danilsibgatullin.handlers;


import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import ru.danilsibgatullin.models.UserChanel;
import ru.danilsibgatullin.services.FileService;
import java.nio.file.Path;



/*
 Класс обработчик потока данных с типом String ,
 после зарезервированных под системные комады 4 байт
 */
public class StringCommandHandler extends SimpleChannelInboundHandler<String> {

    private UserChanel chanel;

    public StringCommandHandler(UserChanel userChanel){
        chanel=userChanel; // в конструктуре принимаем объект обработчика операций с файлами
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String str) throws Exception {
        FileService fs =chanel.getFs();
        str = str.replace("\r\n","");  //для telnet подключения
        if (str.startsWith("mkdr")){
            System.out.println(str);
            fs.mkDir(chanel.getCurrentPath(),str.substring(5));
        }else if(str.startsWith("getd")){
            fs.getDirectoryContent(chanel,ctx,Path.of(chanel.getCurrentPath()));
        }else if(str.startsWith("--rm")) {
            fs.delDir(chanel.getCurrentPath(),str.substring(5));
        }else if(str.startsWith("-cat")) {
            fs.viewFile(ctx,chanel.getCurrentPath(),str.substring(5));
        } else if(str.startsWith("load")) {
            fs.sendFile(ctx,chanel.getCurrentPath(),str.substring(5));
        } else if(str.startsWith("info")) {
            fs.infoStorageUnit(ctx,chanel.getCurrentPath(),str.substring(5));
        } else if(str.startsWith("srch")) {
            fs.searchFile(ctx,chanel,chanel.getUserSystemPath(),str.substring(5));
        } else if(str.startsWith("--cd")){
            fs.changeDir(chanel,str.substring(5));
            fs.getDirectoryContent(chanel,ctx,Path.of(chanel.getCurrentPath()));
        }

    }


}
