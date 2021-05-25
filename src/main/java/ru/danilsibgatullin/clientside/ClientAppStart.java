package ru.danilsibgatullin.clientside;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ru.danilsibgatullin.clientside.controllers.MainClientController;


public class ClientAppStart extends Application {
    public static void main(String[] args) throws Exception {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        String fxmlFile = "/fxml/mainclient.fxml";
        FXMLLoader loader = new FXMLLoader();
        Parent root = loader.load(getClass().getResource(fxmlFile));
        stage.setTitle("MyCoolCloudStorage");
        stage.setScene(new Scene(root));
        stage.show();
    }

}
