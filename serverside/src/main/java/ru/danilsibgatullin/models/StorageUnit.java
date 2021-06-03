package ru.danilsibgatullin.models;

import java.io.File;
import java.io.Serializable;
import java.util.List;

/*
Общий класс для отображения структуры папок
 */
public class StorageUnit implements Serializable {

    private static final long serialVersionUID = 1L;

    private List<File> fileList;

    public StorageUnit(List<File> filesInFolder){
        this.fileList=filesInFolder;
    }

    public void setFileList(List<File> fileList) {
        this.fileList = fileList;
    }

    public List<File> getFileList() {
        return fileList;
    }
}