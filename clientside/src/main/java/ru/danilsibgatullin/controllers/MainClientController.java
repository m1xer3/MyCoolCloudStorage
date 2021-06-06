package ru.danilsibgatullin.controllers;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
import ru.danilsibgatullin.services.ConnectHolder;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;



//Класс контроллер основного окна приложения
public class MainClientController {

    private List<File> unitList;
    private String path; //текущая папка

    @FXML
    TextField currentPath;

    @FXML
    ListView<File> listView;

    @FXML
    Button delButton;

    @FXML
    Button downloadButton;

    @FXML
    TextField searchLine;

    @FXML
    Button uploadButton;

    @FXML
    Button disconnectButton;

    @FXML
    Button refreshButton;

    @FXML
    Button createDirButton;

    @FXML
    ChoiceBox<String> sortList;

    @FXML
    public void initialize(){
        ConnectHolder.setMainControl(this);
        ObservableList<String> sortItems = FXCollections.observableArrayList("Size", "Name");
        sortList.setItems(sortItems);
        refresh();
    }


    @FXML
    public void disconnect() {
        ConnectHolder.closeConnect();
        Platform.exit();
    }

    @FXML
    public void refresh() {
        listView.getItems().clear();
        String toSend = "getd";
        ByteBuf buf =Unpooled.wrappedBuffer(toSend.getBytes(StandardCharsets.UTF_8));
        ConnectHolder.channel.writeAndFlush(buf);
    }

    @FXML
    public void changeDir(MouseEvent mouse){
        if (mouse.getClickCount()==2){
            File f = listView.getSelectionModel().getSelectedItem();
            if(f.isFile()){
                String toSend = "-cat "+f.toPath().getFileName();
                ByteBuf buf =Unpooled.wrappedBuffer(toSend.getBytes(StandardCharsets.UTF_8));
                ConnectHolder.channel.writeAndFlush(buf);
            } else {
                listView.getItems().clear();
                String toSend = "--cd "+f.toPath().getFileName();
                ByteBuf buf =Unpooled.wrappedBuffer(toSend.getBytes(StandardCharsets.UTF_8));
                ConnectHolder.channel.writeAndFlush(buf);
            }
        }
    }

    @FXML
    public void uploadFile() throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Text Files", "*.txt"),
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"),
                new FileChooser.ExtensionFilter("Audio Files", "*.wav", "*.mp3", "*.aac"),
                new FileChooser.ExtensionFilter("All Files", "*.*"));
        File selectedFile = fileChooser.showOpenDialog(uploadButton.getScene().getWindow());
        if(selectedFile == null) {
            return;
        }
        selectedFile.getName();
        String toSend = "-nfl "+selectedFile.getName()+"|";
        byte[] fileInArray = new byte[(int)selectedFile.length()];
        FileInputStream f = new FileInputStream(selectedFile.getAbsolutePath());
        f.read(fileInArray);
        ByteBuf buf1 =Unpooled.wrappedBuffer(toSend.getBytes(StandardCharsets.UTF_8));
        ByteBuf buf2 =Unpooled.wrappedBuffer(fileInArray);
        ByteBuf bufOut = Unpooled.wrappedBuffer(buf1,buf2);
        ConnectHolder.channel.writeAndFlush(bufOut);

    }

    @FXML
    public void delete() {
        File f =listView.getSelectionModel().getSelectedItem();
        String strOut = "--rm "+f.getName();
        ByteBuf bufOut = Unpooled.wrappedBuffer(strOut.getBytes(StandardCharsets.UTF_8));
        ConnectHolder.channel.writeAndFlush(bufOut);
    }


    @FXML
    public void createDir() {
        DialogPane pane = new DialogPane();
        Scene scene = new Scene(pane);
        Stage stage = new Stage();
        stage.setTitle("New folder");
        TextField field = new TextField();
        Button okButton = new Button("OK");
        okButton.setOnAction(e->{
            String strOut = "mkdr "+field.getText();;
            ByteBuf bufOut = Unpooled.wrappedBuffer(strOut.getBytes(StandardCharsets.UTF_8));
            ConnectHolder.channel.writeAndFlush(bufOut);
            stage.close();
        });
        Button closeButton = new Button("Close");
        closeButton.setOnAction(e->{
            stage.close();
        });
        VBox vbox =new VBox();
        vbox.setAlignment(Pos.CENTER);
        vbox.getChildren().add(field);
        vbox.getChildren().add(okButton);
        vbox.getChildren().add(closeButton);
        pane.setContent(vbox);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void download() {
        File f =listView.getSelectionModel().getSelectedItem();
        String strOut = "load "+f.getName();
        ByteBuf bufOut = Unpooled.wrappedBuffer(strOut.getBytes(StandardCharsets.UTF_8));
        ConnectHolder.channel.writeAndFlush(bufOut);
    }

    @FXML
    public void viewInfo() {
        if(!listView.getSelectionModel().isEmpty()){
            File f =listView.getSelectionModel().getSelectedItem();
            String strOut = "info "+f.getName();
            ByteBuf bufOut = Unpooled.wrappedBuffer(strOut.getBytes(StandardCharsets.UTF_8));
            ConnectHolder.channel.writeAndFlush(bufOut);
        }
    }

    @FXML
    public void search() {
        String strOut = "srch "+ searchLine.getText();
        ByteBuf bufOut = Unpooled.wrappedBuffer(strOut.getBytes(StandardCharsets.UTF_8));
        ConnectHolder.channel.writeAndFlush(bufOut);
    }

    @FXML
    public void sort() {
        String sortVar = sortList.getValue();
        if("Size".equals(sortVar)){
            unitList.sort((o1, o2) -> (int) (o1.length()- o2.length()));
        }
        if("Name".equals(sortVar)){
            Collections.sort(unitList);
        }
        addItemsToList(path,listView,unitList);
        refreshList(listView);
    }


    public void addItemsToList(String curPath,ListView<File> listView, List<File> addList){
        unitList=addList;
        path=curPath;
        Platform.runLater(()->{
            listView.getItems().add(new File("~"));
            listView.getItems().add(new File(".."));
            for (File f : addList) {
                listView.getItems().add(f);
            }
            setCurrentPath(curPath);
        });
    }

    public ListView<File> getListView() {
        return listView;
    }

    private void setCurrentPath(String str){
        String[] out = str.split("/");
        StringBuilder sb = new StringBuilder();
        sb.append("~/");
        for(int i=2;i< out.length;i++){
            sb.append(out[i]);
            sb.append("/");
        }
        currentPath.setText(sb.toString());
    }

    public void refreshList(ListView<File> listView){
        listView.getItems().clear();
        listView.setCellFactory(new Callback<ListView<File>, ListCell<File>>() {
            @Override
            public ListCell<File> call(ListView<File> fileListView) {
                return new ListCell<File>(){
                    @Override
                    protected void updateItem(File file, boolean empty) {
                        super.updateItem(file, empty);
                        if (file == null||empty){
                            setText(null);
                        }else {
                            if(file.isFile()){
                                String formatStr = String.format("%-30s",file.getName());
                                String formatCapacity = String.format("%-20d bytes",file.length());
                                String text = String.format("%s %s",formatStr,formatCapacity);
                                setText(text);
                            }else {
                                String formatStr = String.format("%-30s",file.getName());
                                String formatCapacity = String.format("%-20s", "");
                                String text = String.format("%s %s",formatStr,formatCapacity);
                                setText(text);
                            }

                        }
                    }
                };
            }
        });
    }


}




