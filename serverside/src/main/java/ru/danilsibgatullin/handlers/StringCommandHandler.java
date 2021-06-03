package ru.danilsibgatullin.handlers;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import ru.danilsibgatullin.models.StorageUnit;
import ru.danilsibgatullin.models.UserChanel;
import ru.danilsibgatullin.services.FileService;

import java.io.File;
import java.nio.file.Path;
import java.util.List;


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
            List<File> list= fs.getDirectoryContent(Path.of(chanel.getCurrentPath()));
//          временно
            for (File file : list) {
                System.out.println(file.getName());
            }
//            --
            ctx.writeAndFlush(new StorageUnit(list));
        }else if(str.startsWith("--rm")) {
            fs.delDir(chanel.getCurrentPath(),str.substring(5));
        } else if(str.startsWith("--cd")){
            changeDir(str.substring(5));
        }

    }

//    Изменение текущео пути
    private void changeDir(String newDir){
        String currentPath = chanel.getUserSystemPath();
        if ("~".equals(newDir)){
            chanel.setCurrentPath(currentPath);
        }else if ("..".equals(newDir)){
            String[] strArr = currentPath.split("/");
            if (strArr.length>2){
                int lastDirIndex = currentPath.lastIndexOf("/");
                chanel.setCurrentPath(currentPath.substring(0,lastDirIndex));
            }
        } else{
            chanel.setCurrentPath(currentPath+ "/" + newDir);
        }
    }
}
