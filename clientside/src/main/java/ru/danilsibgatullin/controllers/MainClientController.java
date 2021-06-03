package ru.danilsibgatullin.controllers;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import ru.danilsibgatullin.models.CurrentFolderContent;
import ru.danilsibgatullin.services.ConnectHolder;

import java.io.File;
import java.nio.charset.StandardCharsets;


//Класс контроллер основного окна приложения
public class MainClientController {

    CurrentFolderContent currentFolderContent =new CurrentFolderContent();

    @FXML
    TextField commandLine;

    @FXML
    TextField currentPath;

    @FXML
    ListView<String> listView;

    @FXML
    Button sendButton;

    @FXML
    Button delButton;

    @FXML
    Button openButton;

    @FXML
    Button uploadButton;

    @FXML
    Button disconnectButton;

    @FXML
    Button refreshButton;

    @FXML
    public void initialize(){
        //TODO заполнение текущей папки
        currentFolderContent =new CurrentFolderContent();
        for (File file : CurrentFolderContent.curFolder) {
            listView.getItems().add(file.getName());
        }
    }

    //Отправка информации на сервер
    @FXML
    public void send(){
        String toSend = commandLine.getText();
        ByteBuf buf =Unpooled.wrappedBuffer(toSend.getBytes(StandardCharsets.UTF_8));
        ConnectHolder.channel.writeAndFlush(buf);
    }

    //TODO корректное закрытие
    @FXML
    public void disconnect() {

    }

    @FXML
    public void refresh() {
        String toSend = "getd";
        ByteBuf buf =Unpooled.wrappedBuffer(toSend.getBytes(StandardCharsets.UTF_8));
        ConnectHolder.channel.writeAndFlush(buf);
        listView=currentFolderContent.getListViewFolderContent();
    }
}




