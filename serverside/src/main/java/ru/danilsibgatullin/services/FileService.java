package ru.danilsibgatullin.services;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/*
класс обработки команд связанных с файлами,
создаем отдельный объект для каждого подключения
 */
public class FileService {

    private final String workDir;

    public FileService (String userFolder) throws IOException {
        workDir ="serverfolder/"+userFolder+"/";
        initialUserDir();
    }

// При первом подключении создаем для юзера папку
    public void initialUserDir() throws IOException {
        if (!Files.exists(Path.of(workDir))){
            Files.createDirectory(Path.of(workDir));
            System.out.println("dir init");
        }
    }

//  Создание новой директории в текущей папке
    public void mkDir(String workDir,String newDir) throws IOException {
        Files.createDirectory(Path.of(workDir +"/"+newDir));
        System.out.println("dir create");
    }

// Удаление папки
    public void delDir(String workDir,String dirName) throws IOException {
        dirName= workDir + "/"+dirName;
        Path delPath = Path.of(dirName);
        Files.walk(delPath)
                .sorted(Comparator.reverseOrder()) //сортируем в обратной последовательности
                .map(Path::toFile)
                .forEach(File::delete); //удаляем с обратной стороны что бы не было DirectoryNotEmptyException
    }

//  Получение структуры каталога
    public List<File> getDirectoryContent (Path dir) throws IOException {
       return Files.list(dir).map(Path::toFile).collect(Collectors.toList());
    }




}
