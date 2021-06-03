package ru.danilsibgatullin.controllers;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import ru.danilsibgatullin.services.ConnectHolder;

import java.io.IOException;
import java.nio.charset.StandardCharsets;


/*
Класс контроллер окна авторизации
 */
public class LoginClientController {

    private boolean chanelInit=false; // Флаг инициации канала
    private boolean isAuthorized=false;
    private boolean isHide=false;

    private FXMLLoader loader = new FXMLLoader();
    private Parent mainView;
    private Stage stage = new Stage();
    private ConnectHolder connectHolder;


    @FXML
    Button connectButton;

    @FXML
    TextField server;

    @FXML
    TextField port;

    @FXML
    TextField userName;

    @FXML
    TextField pass;


    @FXML
    public void connect() throws InterruptedException {
//  Подлучение канала в случае если уже инициировали канал второй раз этого не делаем
        if(!chanelInit){
            connectHolder= new ConnectHolder(this,server.getText(),Integer.parseInt(port.getText()));
            chanelInit=true;
        }
        String toSend = "auth "+userName.getText()+" "+pass.getText();
        ByteBuf buf =Unpooled.wrappedBuffer(toSend.getBytes(StandardCharsets.UTF_8));
        ConnectHolder.channel.writeAndFlush(buf);
    }

    public void changeSceneToMain(){
        Platform.runLater(()->{
            try {
                mainView  = loader.load(getClass().getResource("/fxml/mainclient.fxml"));
                stage.setScene(new Scene(mainView));
                stage.setResizable(false);
                stage.show();
                Stage stage2 = (Stage) connectButton.getScene().getWindow();
                stage2.hide();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public boolean isAuthorized() {
        return isAuthorized;
    }

    public void setAuthorized(boolean authorized) {
        isAuthorized = authorized;
    }

    public boolean isHide() {
        return isHide;
    }

    public void setHide(boolean hide) {
        isHide = hide;
    }

}
