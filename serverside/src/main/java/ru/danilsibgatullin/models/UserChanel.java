package ru.danilsibgatullin.models;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.serialization.ObjectEncoder;
import ru.danilsibgatullin.handlers.AuthorityHandler;
import ru.danilsibgatullin.handlers.ByteHandlerIn1;
import ru.danilsibgatullin.handlers.FileUploadHandler;
import ru.danilsibgatullin.handlers.StringCommandHandler;
import ru.danilsibgatullin.services.FileService;

import java.io.IOException;

/*
Класс содержит основную информацию по подключившемуся пользователю
 */

public class UserChanel extends ChannelInitializer {

    private String currentPath;
    private String userSystemPath;
    private FileService fs;


    public void setUserName(String userName) throws IOException {
        this.userSystemPath="serverfolder/"+userName;
        this.fs=new FileService(userName);
        this.currentPath=userSystemPath;
    }


    @Override
    protected void initChannel(Channel ch) throws Exception {
        ch.pipeline()
                .addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4))
                .addLast(new LengthFieldPrepender(4))
                .addLast(
                new ByteHandlerIn1(),
                new ObjectEncoder(),
                new AuthorityHandler(this),
                new StringCommandHandler(this),
                new FileUploadHandler(this)
        );
    }

    public FileService getFs() {
        return fs;
    }

    public String getUserSystemPath() {
        return userSystemPath;
    }

    public String getCurrentPath() {
        return currentPath;
    }

    public void setCurrentPath(String currentPath) {
        this.currentPath = currentPath;
    }


}