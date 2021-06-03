package ru.danilsibgatullin.handlers;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;
import ru.danilsibgatullin.controllers.MainClientController;
import ru.danilsibgatullin.models.CurrentFolderContent;
import ru.danilsibgatullin.models.StorageUnit;

import java.io.File;

public class FileStructureHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        super.channelRead(ctx, msg);
    }
//    protected void channelRead0(ChannelHandlerContext ctx, Object o) throws Exception {
//        System.out.println(o.toString());
//        StorageUnit storageUnit =(StorageUnit)o;
//        for(File fl : storageUnit.getFileList()){
//            System.out.println(fl.getName());
//            MainClientController.listView.getItems().add(fl);
//        };
//    }
}
