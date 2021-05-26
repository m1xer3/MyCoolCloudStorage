package ru.danilsibgatullin.serverside.models;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import ru.danilsibgatullin.serverside.handlers.ByteHandlerIn1;
import ru.danilsibgatullin.serverside.handlers.FileHandler;
import ru.danilsibgatullin.serverside.handlers.StringCommandHandler;
import ru.danilsibgatullin.serverside.services.FileService;
import java.io.IOException;

public class UserChanel extends ChannelInitializer {

    private String currentPath; //TODO Определить откуда получать текущую папку клиента
    private String userName; //TODO Определить как получить имя клиента при подлкючении
    private FileService fs;


    public UserChanel(String user) throws IOException {
        userName=user;
        fs=new FileService(user);
    }

    @Override
    protected void initChannel(Channel ch) throws Exception {
        ch.pipeline().addLast(
                new ByteHandlerIn1(),
                new FileHandler(),
                new StringCommandHandler(fs)
//				new OutputHandler(), //TODO Реализовать OUT
        );
    }
}