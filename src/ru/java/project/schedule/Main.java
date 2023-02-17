package ru.java.project.schedule;

import ru.java.project.schedule.managers.HttpTaskManager;
import ru.java.project.schedule.server.HttpTaskServer;
import ru.java.project.schedule.server.KVServer;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        KVServer kvServer = new KVServer();
        kvServer.start();
        HttpTaskServer httpTaskServer = new HttpTaskServer(new HttpTaskManager(8078));
        httpTaskServer.start();
    }
}