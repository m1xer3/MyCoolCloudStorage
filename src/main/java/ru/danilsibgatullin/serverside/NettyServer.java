package ru.danilsibgatullin.serverside;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import ru.danilsibgatullin.serverside.handlers.ByteHandlerIn1;
import ru.danilsibgatullin.serverside.handlers.FileHandler;
import ru.danilsibgatullin.serverside.handlers.StringCommandHandler;


public class NettyServer {
    public NettyServer(Integer port) {
        EventLoopGroup auth = new NioEventLoopGroup(1);
        EventLoopGroup worker = new NioEventLoopGroup();

        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(auth, worker)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<>() {
                        @Override
                        protected void initChannel(Channel ch) throws Exception {
                            ch.pipeline().addLast(
									new ByteHandlerIn1(), // in-1
                                    new FileHandler(), // in-2 file upload
                                    new StringCommandHandler()
//									new OutputHandler(), // out-2
                                    //new ChatMessageHandler()
                            );
                        }
                    });
            ChannelFuture future = bootstrap.bind(port).sync();
            System.out.println("Server started");
            future.channel().closeFuture().sync();
            System.out.println("Server closed");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            auth.shutdownGracefully();
            worker.shutdownGracefully();
        }
    }
}
