package ru.danilsibgatullin.models;

import java.io.File;
import java.io.Serializable;
import java.util.List;

/*
Общий класс для отображения структуры папок который будет передаваться клиенту
 */
public class StorageUnit implements Serializable {

    private static final long serialVersionUID = 1L;

    private List<File> fileList;

    public String getUserCurrentPath() {
        return userCurrentPath;
    }

    private String userCurrentPath;

    public StorageUnit(List<File> filesInFolder, String str) {
        this.userCurrentPath = str;
        this.fileList = filesInFolder;
    }


    public List<File> getFileList() {
        return fileList;
    }
}