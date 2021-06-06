package ru.danilsibgatullin.handlers;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.io.File;
import java.io.FileOutputStream;

/*
Класс для загрузки файлов в папку download на стороне клиента
 */
public class DownloadFileHandler extends SimpleChannelInboundHandler<byte[]> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, byte[] msg) throws Exception {
//      Определяем имя файла
        char c=' ';
        int index=0;
        StringBuilder sb = new StringBuilder();
        while('#'!= c){
            c=(char)msg[index];
            if('#'!= c){
                sb.append(c);
            }
            index++;
        }
//      Создаем файл
        File downloadFile  = new File("clientside/download/"+sb);
        FileOutputStream downloadOut = new FileOutputStream(downloadFile.getPath());
        for(int i=index;i<= msg.length-1;i++){
            downloadOut.write(msg[i]);
        }
    }
}
