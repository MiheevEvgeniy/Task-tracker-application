package ru.java.project.schedule;

import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import ru.java.project.schedule.client.KVTaskClient;
import ru.java.project.schedule.managers.HttpTaskManager;
import ru.java.project.schedule.server.HttpTaskServer;
import ru.java.project.schedule.server.KVServer;
import ru.java.project.schedule.server.adapters.LocalDateTimeAdapter;
import ru.java.project.schedule.server.deserializers.EpicDeserializer;
import ru.java.project.schedule.server.deserializers.SubtaskDeserializer;
import ru.java.project.schedule.server.deserializers.TaskDeserializer;
import ru.java.project.schedule.tasks.Epic;
import ru.java.project.schedule.tasks.Status;
import ru.java.project.schedule.tasks.Subtask;
import ru.java.project.schedule.tasks.Task;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Main {

    public static void main(String[] args) throws IOException, InterruptedException {
        KVServer kvServer = new KVServer();
        kvServer.start();
        HttpTaskServer httpTaskServer = new HttpTaskServer(new HttpTaskManager("http://localhost:8078/","1"));
        httpTaskServer.start();
    }
}