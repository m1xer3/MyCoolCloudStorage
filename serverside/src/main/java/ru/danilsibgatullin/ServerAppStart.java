package ru.danilsibgatullin;

public class ServerAppStart {
    public static void main(String[] args) {
        new NettyServer(7777);
    }
}
