package ru.danilsibgatullin.handlers;

import com.mysql.cj.util.StringUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import ru.danilsibgatullin.models.UserChanel;
import ru.danilsibgatullin.services.AuthorityService;

import java.nio.charset.StandardCharsets;

public class AuthorityHandler extends SimpleChannelInboundHandler<String> {
    UserChanel user;

    public AuthorityHandler(UserChanel userChanelInitializer) {
        user=userChanelInitializer;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String str) throws Exception {
        if (str.startsWith("auth")){
            String[] strArr = str.split(" ");
            AuthorityService auth = new AuthorityService();
            String username = auth.authorityService(strArr[1],strArr[2]);
            if (!StringUtils.isNullOrEmpty(username)){
                user.setUserName(username);
                System.out.println(true);
                ByteBuf buf = Unpooled.wrappedBuffer("true".getBytes(StandardCharsets.UTF_8));
                ctx.writeAndFlush(buf);
            }else{
                System.out.println(false);
                ByteBuf buf = Unpooled.wrappedBuffer("false".getBytes(StandardCharsets.UTF_8));
                ctx.writeAndFlush(buf);
            }
        }
        ctx.fireChannelRead(str);
    }
}
