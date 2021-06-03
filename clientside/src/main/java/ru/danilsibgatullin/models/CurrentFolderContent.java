package ru.danilsibgatullin.models;

import javafx.scene.control.ListView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class CurrentFolderContent {
    public static List<File> curFolder =new ArrayList<>();

    public void clearCurFolder(){
        curFolder=new ArrayList<>();
    }

    public ListView<String> getListViewFolderContent(){
        ListView<String> out = new ListView<>();
        for (File file : curFolder) {
            out.getItems().add(file.getName());
        }
        return out;
    }
}
