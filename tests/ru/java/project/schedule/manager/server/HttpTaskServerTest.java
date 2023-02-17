package ru.java.project.schedule.manager.server;

import com.google.gson.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.java.project.schedule.managers.HttpTaskManager;
import ru.java.project.schedule.server.HttpTaskServer;
import ru.java.project.schedule.server.KVServer;
import ru.java.project.schedule.server.adapters.LocalDateTimeAdapter;
import ru.java.project.schedule.server.deserializers.EpicDeserializer;
import ru.java.project.schedule.server.deserializers.SubtaskDeserializer;
import ru.java.project.schedule.server.deserializers.TaskDeserializer;
import ru.java.project.schedule.tasks.Epic;
import ru.java.project.schedule.tasks.Subtask;
import ru.java.project.schedule.tasks.Task;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class HttpTaskServerTest {
    HttpTaskServer httpTaskServer;
    KVServer kvServer;
    HttpClient httpClient = HttpClient.newHttpClient();

    String url = "http://localhost:8080/";
    Gson gson;

    @BeforeEach
    public void startServers() {
        try {
            kvServer = new KVServer();
            kvServer.start();
            httpTaskServer = new HttpTaskServer(new HttpTaskManager(8078));
            httpTaskServer.start();
            gson = new GsonBuilder()
                    .setLenient()
                    .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                    .registerTypeAdapter(Subtask.class, new SubtaskDeserializer())
                    .registerTypeAdapter(Epic.class, new EpicDeserializer())
                    .registerTypeAdapter(Task.class, new TaskDeserializer())
                    .create();
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    @AfterEach
    public void stopServers() {
        kvServer.stop();
        httpTaskServer.stop();
    }

    @Test
    public void endpointTask() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .POST(HttpRequest.BodyPublishers.ofString("{'name':'JSON Task'," +
                            "'description':'JSON Description'," +
                            "'startTime':'10.11.2037 19:30'," +
                            "'duration':'103'}"))
                    .uri(URI.create(url + "tasks/task/"))
                    .version(HttpClient.Version.HTTP_1_1)
                    .build();
            HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
            httpClient.send(request, handler);

            request = HttpRequest.newBuilder()
                    .POST(HttpRequest.BodyPublishers.ofString("{'name':'JSON Task2'," +
                            "'description':'JSON Description2'," +
                            "'startTime':'10.11.2038 19:30'," +
                            "'duration':'103'}"))
                    .uri(URI.create(url + "tasks/task/"))
                    .version(HttpClient.Version.HTTP_1_1)
                    .build();
            handler = HttpResponse.BodyHandlers.ofString();
            httpClient.send(request, handler);

            request = HttpRequest.newBuilder()
                    .GET()
                    .uri(URI.create(url + "tasks/task/?id=1"))
                    .version(HttpClient.Version.HTTP_1_1)
                    .build();
            handler = HttpResponse.BodyHandlers.ofString();
            HttpResponse<String> response = httpClient.send(request, handler);
            JsonElement jsonElement = JsonParser.parseString(response.body());
            Task task = gson.fromJson(jsonElement.getAsJsonObject(), Task.class);

            assertNotNull(task, "Задача не найдена");
            assertEquals(new Task("JSON Task", "JSON Description", LocalDateTime.of(2037, Month.NOVEMBER, 10, 19, 30), 103), task);

            request = HttpRequest.newBuilder()
                    .GET()
                    .uri(URI.create(url + "tasks/task/"))
                    .version(HttpClient.Version.HTTP_1_1)
                    .build();
            handler = HttpResponse.BodyHandlers.ofString();
            response = httpClient.send(request, handler);

            assertNotEquals("[]", response.body());

            jsonElement = JsonParser.parseString(response.body());
            JsonArray jsonArray = jsonElement.getAsJsonArray();
            List<Task> tasks = new ArrayList<>();

            for (int i = 0; i < jsonArray.size(); i++) {
                tasks.add(gson.fromJson(jsonArray.get(i).getAsJsonObject(), Task.class));
            }
            assertNotNull(tasks);

            List<Task> expectedTasks = new ArrayList<>();
            expectedTasks.add(new Task("JSON Task", "JSON Description", LocalDateTime.of(2037, Month.NOVEMBER, 10, 19, 30), 103));
            expectedTasks.add(new Task("JSON Task2", "JSON Description2", LocalDateTime.of(2038, Month.NOVEMBER, 10, 19, 30), 103));
            assertEquals(expectedTasks, tasks);

            request = HttpRequest.newBuilder()
                    .DELETE()
                    .uri(URI.create(url + "tasks/task/?id=1"))
                    .version(HttpClient.Version.HTTP_1_1)
                    .build();
            handler = HttpResponse.BodyHandlers.ofString();
            httpClient.send(request, handler);

            request = HttpRequest.newBuilder()
                    .GET()
                    .uri(URI.create(url + "tasks/task/?id=1"))
                    .version(HttpClient.Version.HTTP_1_1)
                    .build();
            handler = HttpResponse.BodyHandlers.ofString();
            response = httpClient.send(request, handler);

            assertEquals("null", response.body());

            request = HttpRequest.newBuilder()
                    .DELETE()
                    .uri(URI.create(url + "tasks/task/"))
                    .version(HttpClient.Version.HTTP_1_1)
                    .build();
            handler = HttpResponse.BodyHandlers.ofString();
            httpClient.send(request, handler);

            request = HttpRequest.newBuilder()
                    .GET()
                    .uri(URI.create(url + "tasks/task/"))
                    .version(HttpClient.Version.HTTP_1_1)
                    .build();
            handler = HttpResponse.BodyHandlers.ofString();
            response = httpClient.send(request, handler);

            assertEquals("[]", response.body());


        } catch (IOException | InterruptedException e) {
            System.out.println(e);
            System.out.println(e.getStackTrace()[0]);
        }
    }

    @Test
    public void endpointSubtask() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .POST(HttpRequest.BodyPublishers.ofString("{'name':'JSON Epic'," +
                            "'description':'JSON Description'}"))
                    .uri(URI.create(url + "tasks/epic/"))
                    .version(HttpClient.Version.HTTP_1_1)
                    .build();
            HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
            httpClient.send(request, handler);

            request = HttpRequest.newBuilder()
                    .POST(HttpRequest.BodyPublishers.ofString("{'name':'JSON Subtask'," +
                            "'description':'JSON Description'," +
                            "'startTime':'10.11.2037 19:30'," +
                            "'duration':'103'," +
                            "'epicId':'1'}"))
                    .uri(URI.create(url + "tasks/subtask/"))
                    .version(HttpClient.Version.HTTP_1_1)
                    .build();

            handler = HttpResponse.BodyHandlers.ofString();
            httpClient.send(request, handler);

            request = HttpRequest.newBuilder()
                    .POST(HttpRequest.BodyPublishers.ofString("{'name':'JSON Subtask2'," +
                            "'description':'JSON Description2'," +
                            "'startTime':'10.11.2038 19:30'," +
                            "'duration':'103'," +
                            "'epicId':'1'}"))
                    .uri(URI.create(url + "tasks/subtask/"))
                    .version(HttpClient.Version.HTTP_1_1)
                    .build();
            handler = HttpResponse.BodyHandlers.ofString();
            httpClient.send(request, handler);


            request = HttpRequest.newBuilder()
                    .GET()
                    .uri(URI.create(url + "tasks/subtask/?id=2"))
                    .version(HttpClient.Version.HTTP_1_1)
                    .build();
            handler = HttpResponse.BodyHandlers.ofString();
            HttpResponse<String> response = httpClient.send(request, handler);
            JsonElement jsonElement = JsonParser.parseString(response.body());
            Subtask subtask = gson.fromJson(jsonElement.getAsJsonObject(), Subtask.class);

            assertNotNull(subtask, "Подзадача 1 не найдена");
            assertEquals(new Subtask("JSON Subtask", "JSON Description", LocalDateTime.of(2037, Month.NOVEMBER, 10, 19, 30), 103, 1), subtask);

            request = HttpRequest.newBuilder()
                    .GET()
                    .uri(URI.create(url + "tasks/subtask/?id=3"))
                    .version(HttpClient.Version.HTTP_1_1)
                    .build();
            handler = HttpResponse.BodyHandlers.ofString();
            response = httpClient.send(request, handler);
            jsonElement = JsonParser.parseString(response.body());
            Subtask subtask2 = gson.fromJson(jsonElement.getAsJsonObject(), Subtask.class);

            assertNotNull(subtask, "Подзадача 2 не найдена");
            assertEquals(new Subtask("JSON Subtask2", "JSON Description2", LocalDateTime.of(2038, Month.NOVEMBER, 10, 19, 30), 103, 1), subtask2);

            request = HttpRequest.newBuilder()
                    .GET()
                    .uri(URI.create(url + "tasks/subtask/"))
                    .version(HttpClient.Version.HTTP_1_1)
                    .build();
            handler = HttpResponse.BodyHandlers.ofString();
            response = httpClient.send(request, handler);

            assertNotEquals("[]", response.body());

            jsonElement = JsonParser.parseString(response.body());
            JsonArray jsonArray = jsonElement.getAsJsonArray();
            List<Subtask> subtasks = new ArrayList<>();


            for (int i = 0; i < jsonArray.size(); i++) {
                subtasks.add(gson.fromJson(jsonArray.get(i).getAsJsonObject(), Subtask.class));
            }
            assertNotNull(subtasks);

            List<Task> expectedTasks = new ArrayList<>();
            expectedTasks.add(new Subtask("JSON Subtask", "JSON Description", LocalDateTime.of(2037, Month.NOVEMBER, 10, 19, 30), 103, 1));
            expectedTasks.add(new Subtask("JSON Subtask2", "JSON Description2", LocalDateTime.of(2038, Month.NOVEMBER, 10, 19, 30), 103, 1));
            assertEquals(expectedTasks, subtasks);

            request = HttpRequest.newBuilder()
                    .DELETE()
                    .uri(URI.create(url + "tasks/subtask/?id=2"))
                    .version(HttpClient.Version.HTTP_1_1)
                    .build();
            handler = HttpResponse.BodyHandlers.ofString();
            httpClient.send(request, handler);

            request = HttpRequest.newBuilder()
                    .GET()
                    .uri(URI.create(url + "tasks/subtask/?id=2"))
                    .version(HttpClient.Version.HTTP_1_1)
                    .build();
            handler = HttpResponse.BodyHandlers.ofString();
            response = httpClient.send(request, handler);

            assertEquals("null", response.body());

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
