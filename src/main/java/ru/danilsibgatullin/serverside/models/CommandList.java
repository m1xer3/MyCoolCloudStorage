package ru.danilsibgatullin.serverside.models;

import java.util.concurrent.ConcurrentLinkedQueue;

//Класс на данный момент не используется , оставил как хранилище текущих команд, его судьбу определю позже

public class CommandList {
    private ConcurrentLinkedQueue<String> commandListReturnString = new ConcurrentLinkedQueue<>();
    private ConcurrentLinkedQueue<String> commandListReturnByte = new ConcurrentLinkedQueue<>();

    public CommandList() {
        commandListReturnString.add("--cd"); //смены текущей папки
        commandListReturnByte.add("-nfl"); //передача нового файла
        commandListReturnString.add("-gfl"); //запрос на выгрузку файла
        commandListReturnString.add("mkdr"); //создание папки
        commandListReturnString.add("--rm"); //удаление файла или папки
        commandListReturnString.add("renm"); //переименование папки или файла
        commandListReturnString.add("-vsz"); //просмотр объема файла или папки
        commandListReturnString.add("-vdt"); //просмотр даты обновления
        commandListReturnString.add("-src"); //поиск
        commandListReturnString.add("sort"); //сортировка

    }

    public synchronized boolean searchCommandReturnString(String command){
        for (String s : commandListReturnString) {
            if (command.equals(s)){
                return true;
            }
        }
        return false;

    }
}
