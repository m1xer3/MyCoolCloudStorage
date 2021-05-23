package ru.danilsibgatullin.serverside;

import ru.danilsibgatullin.serverside.models.NettyServer;

public class ServerAppStart {
    public static void main(String[] args) {
        new NettyServer(5678);
    }
}
