package ru.danilsibgatullin;


import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.Window;
import ru.danilsibgatullin.services.ConnectHolder;

public class ClientAppStart extends Application {


    public static void main(String[] args) throws Exception {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {

        String fxmlFileLogin = "/fxml/loginclient.fxml";
        FXMLLoader loader = new FXMLLoader();
        Parent loginView = loader.load(getClass().getResource(fxmlFileLogin));
        stage.setTitle("MyCoolCloudStorage");
        stage.setScene(new Scene(loginView));
        stage.setResizable(false);
        stage.setOnCloseRequest(event -> {
            ConnectHolder.closeConnect();
            Platform.exit();
        });
        stage.show();
    }

}
