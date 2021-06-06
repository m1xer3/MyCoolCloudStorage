package ru.danilsibgatullin.services;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import ru.danilsibgatullin.controllers.LoginClientController;
import ru.danilsibgatullin.controllers.MainClientController;
import ru.danilsibgatullin.handlers.AuthoritiClientHadler;
import ru.danilsibgatullin.handlers.DownloadFileHandler;
import ru.danilsibgatullin.handlers.StringInHandlet;
import ru.danilsibgatullin.models.StorageUnit;
import java.io.IOException;

/*
Класс для обработки подключения клиента к серверу
 */

public class ConnectHolder {

    private static final Bootstrap CONNECT_SERVER= new Bootstrap();
    private static final  EventLoopGroup CLIENT =new NioEventLoopGroup();
    public static Channel channel = null;
    public static MainClientController mainControl;

    public ConnectHolder(LoginClientController control, String server, Integer port) throws InterruptedException {
        channel =CONNECT_SERVER.group(CLIENT)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<Channel>() {
                    @Override
                    protected void initChannel(Channel channel){
                        channel.pipeline()
                                .addLast(
//                              увеличиваем максимальный размер пакета для декодера
                                new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4),
                                new LengthFieldPrepender(4),
//                              основные обработчики
                                new AuthoritiClientHadler(control),
                                new ObjectDecoder(ClassResolvers.cacheDisabled(StorageUnit.class.getClassLoader())),
                                new ChannelInboundHandlerAdapter() {
                                    @Override
                                    public void channelRead(ChannelHandlerContext ctx, Object msg) throws IOException {
                                        if(msg instanceof StorageUnit){
                                            StorageUnit unit = (StorageUnit) msg;
                                            if(!control.isHide()){
                                                control.changeSceneToMain();
                                                control.setHide(true);
                                            }else{
                                                mainControl.addItemsToList(unit.getUserCurrentPath(),mainControl.getListView(),unit.getFileList());
                                                mainControl.refreshList(mainControl.getListView());
                                            }
                                        }else {
                                            ctx.fireChannelRead(msg);
                                        }

                                    }

                                    @Override
                                    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
                                        cause.printStackTrace();
                                        ctx.channel().close();
                                    }
                                },
                                new StringInHandlet(),
                                new DownloadFileHandler()
                        );
                    }
                })
                .connect(server, port)
                .sync()
                .channel();
    }


    public static void closeConnect(){
        channel.close();
        CLIENT.shutdownGracefully();
    }

    public static void setMainControl(MainClientController mainControl) {
        ConnectHolder.mainControl = mainControl;
    }



}
