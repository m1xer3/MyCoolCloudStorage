package ru.danilsibgatullin.services;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import ru.danilsibgatullin.controllers.LoginClientController;
import ru.danilsibgatullin.controllers.MainClientController;
import ru.danilsibgatullin.handlers.AuthoritiClientHadler;
import ru.danilsibgatullin.models.CurrentFolderContent;
import ru.danilsibgatullin.models.StorageUnit;

import java.io.File;

public class ConnectHolder {

    private static final Bootstrap CONNECT_SERVER= new Bootstrap();
    private static final  EventLoopGroup CLIENT =new NioEventLoopGroup();
    public static Channel channel = null;
    public CurrentFolderContent folder =new CurrentFolderContent();


    public ConnectHolder(LoginClientController control, String server, Integer port) throws InterruptedException {
        channel =CONNECT_SERVER.group(CLIENT)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<Channel>() {
                    @Override
                    protected void initChannel(Channel channel){
                        channel.pipeline().addLast(
                                new AuthoritiClientHadler(control),
                                new ObjectDecoder(ClassResolvers.cacheDisabled(StorageUnit.class.getClassLoader())),
                                new ChannelInboundHandlerAdapter() {
                                    @Override
                                    public void channelRead(ChannelHandlerContext ctx, Object msg) {
                                        System.out.println("enter");
                                        StorageUnit unit = (StorageUnit) msg;
                                        //сначала заполняем текущий путь прежде чем инициировать основное окно
                                        folder.curFolder = unit.getFileList();
                                        if(!control.isHide()){
                                            control.changeSceneToMain();
                                            control.setHide(true);
                                        }
                                    }

                                    @Override
                                    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
                                        cause.printStackTrace();
                                        ctx.channel().close();
                                    }
                                }
                        );
                    }
                })
                .connect(server, port)//)
                .sync()
                .channel();
    }

}
