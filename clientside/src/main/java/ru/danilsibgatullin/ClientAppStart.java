package ru.danilsibgatullin;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ClientAppStart extends Application {

    private static Stage mainStage=new Stage();

    public static void main(String[] args) throws Exception {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        mainStage=stage;
        String fxmlFileLogin = "/fxml/loginclient.fxml";
        FXMLLoader loader = new FXMLLoader();
        Parent loginView = loader.load(getClass().getResource(fxmlFileLogin));
        mainStage.setTitle("MyCoolCloudStorage");
        mainStage.setScene(new Scene(loginView));
        mainStage.show();
    }

    public static void setMainStage(Stage stage){
        mainStage=stage;
    }

    public static Stage getMainStage(){
        return mainStage;
    }

}
