package ru.danilsibgatullin.services;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import ru.danilsibgatullin.models.StorageUnit;
import ru.danilsibgatullin.models.UserChanel;

import java.io.File;
import java.io.IOException;
import java.nio.channels.Channel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.sql.Array;
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

//  Создание файла
    public void createFile(String path) throws IOException {
        Path newPath = Path.of(path);
        if (!Files.exists(newPath)) {
            Files.createFile(newPath);
        }
    }

//  Получение структуры каталога
    public void getDirectoryContent (UserChanel ch,ChannelHandlerContext ctx,Path dir) throws IOException {
        List<File> list = Files.list(dir).map(Path::toFile).collect(Collectors.toList());
        ctx.writeAndFlush(new StorageUnit(list,ch.getCurrentPath()));
    }

//    Изменение текущей папки
    public void changeDir(UserChanel channel, String newDir){
        String currentPath = channel.getCurrentPath();
        if ("~".equals(newDir)){
            channel.setCurrentPath(channel.getUserSystemPath());
        }else if (newDir.contains("/")){
            channel.setCurrentPath(newDir);
        } else if ("..".equals(newDir)){
            String[] strArr = currentPath.split("/");
            if (strArr.length>2){
                int lastDirIndex = currentPath.lastIndexOf("/");
                channel.setCurrentPath(currentPath.substring(0,lastDirIndex));
            }
        } else{
            channel.setCurrentPath(currentPath+ "/" + newDir);
        }
    }

//    Просмотр содержимого файла
    public void viewFile(ChannelHandlerContext ctx, String currentPath, String fileName) throws IOException {
        Path readPath =Path.of(currentPath +"/" +fileName);
        StringBuilder sb = new StringBuilder();
        Files.readAllLines(readPath).stream().forEach(s -> sb.append(s+"\n"));
        ctx.writeAndFlush("read "+ sb);
    }

//    Отправка файла
    public void sendFile (ChannelHandlerContext ctx, String currentPath, String fileName) throws IOException {
        Path readPath =Path.of(currentPath +"/" +fileName);
        if(readPath.toFile().isFile()){
            String toSend = readPath.getFileName()+"#";
            byte[] out1 = toSend.getBytes(StandardCharsets.UTF_8);
            byte[] out2 = Files.readAllBytes(readPath);
            byte[] out = new byte[out1.length+out2.length];
            System.arraycopy(out1,0,out,0,out1.length);
            System.arraycopy(out2,0,out,out1.length,out2.length);
            ctx.writeAndFlush(out);
        }
    }

//  информация по файлу и папкам
    public void infoStorageUnit(ChannelHandlerContext ctx, String currentPath, String fileName) throws IOException {
        Path readPath =Path.of(currentPath +"/" +fileName);
        String size;
        String date;
        String mod;
        if(readPath.toFile().isDirectory()){
            long compSize=0;
            BasicFileAttributes atr = Files.readAttributes(readPath, BasicFileAttributes.class);
            List<File> filesInDirectory= Files.walk(readPath).map(Path::toFile).filter(f->f.isFile()).collect(Collectors.toList());
            for (File file : filesInDirectory) {
                BasicFileAttributes fileAtr = Files.readAttributes(file.toPath(), BasicFileAttributes.class);
                compSize+=fileAtr.size();
            }
            size = String.valueOf(compSize) ;
            date = atr.creationTime().toString();
            mod = atr.lastModifiedTime().toString();
        }
        else {
            BasicFileAttributes atr = Files.readAttributes(readPath, BasicFileAttributes.class);
            size = String.valueOf(atr.size()) ;
            date = atr.creationTime().toString();
            mod = atr.lastModifiedTime().toString();
        }
        ctx.writeAndFlush("info "+date+ "\n" +mod + "\n" + size);
    }

//    Поиск файла
    public void searchFile(ChannelHandlerContext ctx,UserChanel userChanel,String userFolder,String fileName) throws IOException {
        File find = Files.walk(Path.of(userFolder))
                .map(Path::toFile)
                .filter(f-> f.isFile())
                .filter(f ->(f.getName().equals(fileName)))
                .findFirst()
                .get();
        changeDir(userChanel,find.getParent());
        getDirectoryContent(userChanel,ctx, Path.of(find.getParent()));
    }
}
