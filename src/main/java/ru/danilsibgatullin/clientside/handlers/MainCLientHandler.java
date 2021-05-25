package ru.danilsibgatullin.clientside.handlers;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import javafx.application.Platform;
import javafx.beans.property.StringProperty;

public class MainCLientHandler extends SimpleChannelInboundHandler<TextWebSocketFrame>  {
    private final StringProperty sendCommand;

    public MainCLientHandler(StringProperty receivingMessageModel) {
        this.sendCommand = receivingMessageModel;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext arg0, TextWebSocketFrame in) throws Exception {
        final String cm = in.text();
        System.out.println("Send coomnd "+sendCommand);
        Platform.runLater( () -> sendCommand.set(cm) );
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
