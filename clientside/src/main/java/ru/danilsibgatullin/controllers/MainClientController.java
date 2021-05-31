package ru.danilsibgatullin.controllers;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.nio.charset.StandardCharsets;

//Класс контроллер основного окна приложения
public class MainClientController {

    private Channel channel;
    private EventLoopGroup client =new NioEventLoopGroup();
    private Bootstrap connectServer = new Bootstrap();

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    @FXML
    Button sendButton;

    @FXML
    Button connectButton;

    @FXML
    TextField commandLine;

    //Отправка информации на сервер
    @FXML
    public void send(){
        String toSend = commandLine.getText();
        ByteBuf buf =Unpooled.wrappedBuffer(toSend.getBytes(StandardCharsets.UTF_8));
        channel.writeAndFlush(buf);
    }

    //TODO корректное закрытие
    @FXML
    public void disconnect() {

    }
}




