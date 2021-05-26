package ru.danilsibgatullin.clientside.controllers;

import java.nio.charset.StandardCharsets;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import ru.danilsibgatullin.clientside.handlers.MainCLientHandler;

//Класс контроллер основного окна приложения
public class MainClientController {

    private Channel channel;
    private EventLoopGroup client =new NioEventLoopGroup();
    private Bootstrap connectServer = new Bootstrap();

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

    //Подключение к серверу
    @FXML
    public void connect() throws InterruptedException {
        channel=connectServer.group(client)
                .channel(NioSocketChannel.class)
                .handler(new MainCLientHandler())
                .connect("localhost",7777)
                .sync()
                .channel();
    }

    //TODO корректное закрытие
    @FXML
    public void disconnect() {

    }
}




