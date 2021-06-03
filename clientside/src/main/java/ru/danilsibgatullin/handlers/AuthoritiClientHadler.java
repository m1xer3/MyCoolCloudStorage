package ru.danilsibgatullin.handlers;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import ru.danilsibgatullin.controllers.LoginClientController;
import ru.danilsibgatullin.services.ConnectHolder;

import java.nio.charset.StandardCharsets;

public class AuthoritiClientHadler extends SimpleChannelInboundHandler<ByteBuf> {

    LoginClientController control;

    public AuthoritiClientHadler(LoginClientController controller){
        this.control=controller;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf byteBuf){
        if(!control.isAuthorized()){
            StringBuilder sb = new StringBuilder();
            int byteCount = 1;
            while(byteBuf.isReadable()&&byteCount<5){
                sb.append((char) byteBuf.readByte());
                byteCount++;
            }
            if ("true".equals(sb.toString())){
                System.out.println("Authority SUCCES");
                control.setAuthorized(true);
                String toSend = "getd";
                ByteBuf buf = Unpooled.wrappedBuffer(toSend.getBytes(StandardCharsets.UTF_8));
                ConnectHolder.channel.writeAndFlush(buf);
            }else if("false".equals(sb.toString())) {
                System.out.println("Authority FALSE");
                Platform.runLater(() -> {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setContentText("Login or Password is incorrect.");
                    alert.show();
                });
            }
        }else{
            ctx.fireChannelRead(byteBuf.retain());
        }

    }

}
