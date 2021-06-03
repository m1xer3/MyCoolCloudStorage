package ru.danilsibgatullin.models;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.handler.codec.serialization.ObjectEncoder;
import ru.danilsibgatullin.handlers.AuthorityHandler;
import ru.danilsibgatullin.handlers.ByteHandlerIn1;
import ru.danilsibgatullin.handlers.FileUploadHandler;
import ru.danilsibgatullin.handlers.StringCommandHandler;
import ru.danilsibgatullin.services.FileService;

import java.io.IOException;

public class UserChanel extends ChannelInitializer {

    private String currentPath; //TODO Определить откуда получать текущую папку клиента
    private String userName; //TODO Определить как получить имя клиента при подлкючении
    private String userSystemPath;
    private FileService fs;


    public void setUserName(String userName) throws IOException {
        this.userName = userName;
        this.userSystemPath="serverfolder/"+userName;
        this.fs=new FileService(userName);
        this.currentPath=userSystemPath;
    }


    @Override
    protected void initChannel(Channel ch) throws Exception {
        ch.pipeline().addLast(

                new ByteHandlerIn1(),
                new ObjectEncoder(),
                new AuthorityHandler(this),
                new StringCommandHandler(this),
                new FileUploadHandler(this)





//				new OutputHandler(), //TODO Реализовать OUT
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

    public String getUserName() {
        return userName;
    }

}