package ru.danilsibgatullin.handlers;


import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.*;
import java.nio.charset.StandardCharsets;


/*
Класс для обработки текстовых команд
 */
public class StringInHandlet extends SimpleChannelInboundHandler<String> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {

//        команда на то что пришла информация по объекту
       if(msg.startsWith("info")){
            String out = msg.substring("info ".length());
            String[] strArr = out.split("\n");
            StringBuilder sb = new StringBuilder();
            sb.append("Creation date :"+ strArr[0]+"\n");
            sb.append("Modify date :"+ strArr[1]+"\n");
            sb.append("Size :"+ strArr[2]+" bytes\n");
//            выводим информацию в окно
            Platform.runLater(()->{
                AnchorPane pane = new AnchorPane();
                Scene scene = new Scene(pane);
                Stage stage = new Stage();
                stage.setTitle("File info");
                stage.setResizable(false);
                TextArea text = new TextArea();
                text.appendText(sb.toString());
                Button closeButton = new Button("Close");
                closeButton.setOnAction(e->{
                    stage.close();
                });
                VBox vbox =new VBox();
                vbox.setAlignment(Pos.CENTER);
                vbox.getChildren().add(text);
                vbox.getChildren().add(closeButton);
                pane.getChildren().add(vbox);
                stage.setScene(scene);
                stage.show();
            });
        }
        else {
//         Создаем временный файл для вывода содержимого
            File file = new File("clientside/temp/temp.txt");
            FileOutputStream out = new FileOutputStream(file.getPath());
            out.write(msg.getBytes(StandardCharsets.UTF_8));

//        Запускаем тестовый редактор соответсвующей ОС
            if (System.getProperty("java.vm.version").contains("ubun")){
                ProcessBuilder processBulider = new ProcessBuilder("gedit",file.getAbsolutePath());
                processBulider.start();
            } else if (System.getProperty("java.vm.version").contains("win")){
                ProcessBuilder processBulider = new ProcessBuilder("notepad",file.getAbsolutePath());
                processBulider.start();
            }
        }
    }
}
