package ru.danilsibgatullin.controllers;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import ru.danilsibgatullin.handlers.AuthoritiClientHadler;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class LoginClientController {

    private Channel channel;
    private EventLoopGroup client =new NioEventLoopGroup();
    private Bootstrap connectServer = new Bootstrap();
    private boolean isAutorized =false;
    private boolean answerResived =false;
    private MainClientController control = new MainClientController();

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
    public void connect() throws InterruptedException, IOException {
//  Подлучение канала
       channel=connectServer.group(client)
                .channel(NioSocketChannel.class)
                .handler(new AuthoritiClientHadler(this))
                .connect(server.getText(),Integer.parseInt(port.getText()))
                .sync()
                .channel();

        String toSend = "auth "+userName.getText()+" "+pass.getText();
        ByteBuf buf =Unpooled.wrappedBuffer(toSend.getBytes(StandardCharsets.UTF_8));
        channel.writeAndFlush(buf);
    }

    public void changeSceneToMain() throws IOException {
        Platform.runLater(()->{
            FXMLLoader loader = new FXMLLoader();
            Parent mainView = null;
            try {
                mainView = loader.load(getClass().getResource("/fxml/mainclient.fxml"));
                control.setChannel(channel);
                Stage stage = new Stage();
                stage.setScene(new Scene(mainView));
                stage.show();
                Stage stage2 = (Stage) connectButton.getScene().getWindow();
                stage2.hide();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public Button getConnectButton() {
        return connectButton;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public boolean isAutorized() {
        return isAutorized;
    }

    public void setAutorized(boolean autorized) {
        isAutorized = autorized;
    }

    public boolean isAnswerResived() {
        return answerResived;
    }

    public void setAnswerResived(boolean answerResived) {
        this.answerResived = answerResived;
    }

}
