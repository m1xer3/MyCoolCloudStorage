package ru.danilsibgatullin.serverside.services;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;

/*
класс обработки команд связанных с файлами,
создаем отдельный объект для каждого подключения
 */
public class FileService {

    private final String systemDir;

    public FileService (String userfolder) throws IOException {
        systemDir="server/"+userfolder+"/";
        initialUserDir();
    }

// При первом подключении создаем для юзера папку
    public void initialUserDir() throws IOException {
        if (!Files.exists(Path.of(systemDir))){
            Files.createDirectory(Path.of(systemDir));
            System.out.println("dir init");
        }
    }

//  Создание новой директории в текущей папке
    public void mkDir(String newDir) throws IOException {
        Files.createDirectory(Path.of(systemDir+newDir));
        System.out.println("dir create");
    }

// Удаление папки
    public void delDir(String dirName) throws IOException {
        dirName=systemDir + dirName;
        Path delPath = Path.of(dirName);
        Files.walk(delPath)
                .sorted(Comparator.reverseOrder()) //сортируем в обратной последовательности
                .map(Path::toFile)
                .forEach(File::delete); //удаляем с обратной стороны что бы не было DirectoryNotEmptyException
    }

}
