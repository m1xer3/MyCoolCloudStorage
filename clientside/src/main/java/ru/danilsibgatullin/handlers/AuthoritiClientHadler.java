package ru.danilsibgatullin.handlers;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import javafx.scene.control.Alert;
import ru.danilsibgatullin.controllers.LoginClientController;

public class AuthoritiClientHadler extends SimpleChannelInboundHandler<ByteBuf> {

    LoginClientController control;

    public AuthoritiClientHadler(LoginClientController controller){
        this.control=controller;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf) throws Exception {
        StringBuilder sb = new StringBuilder();
        while(byteBuf.isReadable()){
            sb.append((char) byteBuf.readByte());
        }
        if ("true".equals(sb.toString())){
            System.out.println("Authority SUCCES");
            control.changeSceneToMain();
        }else {
            System.out.println("Authority FALSE");
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Login or Password is incorrect.");
            alert.show();
        }
    }

}
