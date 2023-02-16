package ru.java.project.schedule.server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import ru.java.project.schedule.managers.HttpTaskManager;
import ru.java.project.schedule.managers.Managers;
import ru.java.project.schedule.managers.TaskManager;
import ru.java.project.schedule.server.adapters.LocalDateTimeAdapter;
import ru.java.project.schedule.server.deserializers.*;
import ru.java.project.schedule.tasks.Epic;
import ru.java.project.schedule.tasks.Subtask;
import ru.java.project.schedule.tasks.Task;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Objects;

public class HttpTaskServer {
    private final HttpServer server;
    private final static Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .registerTypeAdapter(Subtask.class, new SubtaskDeserializer())
            .registerTypeAdapter(Epic.class, new EpicDeserializer())
            .registerTypeAdapter(Task.class, new TaskDeserializer())
            .create();
    private static TaskManager manager = Managers.getDefault();
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

    private final int PORT = 8080;
    public HttpTaskServer(HttpTaskManager httpTaskManager) throws IOException {
        manager = httpTaskManager;
        this.server= HttpServer.create(new InetSocketAddress(PORT), 0);
        server.createContext("/tasks/task/", new TaskHandler());
        server.createContext("/tasks/subtask/", new SubtaskHandler());
        server.createContext("/tasks/epic/", new EpicHandler());
        server.createContext("/tasks/", new MainHandler());
        server.createContext("/tasks/history/", new HistoryHandler());
    }
    public void start(){
        server.start();
        System.out.println("Server started...");
    }
    public void stop(){
        server.stop(0);
    }
    private static Endpoint getEndpoint(String requestPath, String requestMethod, String query){
        String[] pathElements = requestPath.split("/");
        if(pathElements.length ==2 && pathElements[1].equals("tasks")){
            switch (requestMethod) {
                case "GET" -> {
                    return Endpoint.GET_PRIORITIZED_TASKS;
                }
                default -> {
                    return null;
                }
            }
        }
        switch (pathElements[2]) {
            case "task" -> {
                switch (requestMethod) {
                    case "GET" -> {
                        if (query == null) {
                            return Endpoint.GET_ALL_TASKS;
                        } else {
                            return Endpoint.GET_TASK;
                        }
                    }
                    case "POST" -> {
                        return Endpoint.POST_TASK;
                    }
                    case "DELETE" -> {
                        if (query == null) {
                            return Endpoint.DELETE_ALL_TASKS;
                        } else {
                            return Endpoint.DELETE_TASK;
                        }
                    }
                    default -> {
                        return null;
                    }
                }
            }
            case "subtask" -> {
                switch (requestMethod) {
                    case "GET" -> {
                        if (pathElements[3].equals("epic")){
                            return Endpoint.GET_EPIC_SUBTASKS;
                        }
                        if (query == null) {
                            return Endpoint.GET_ALL_SUBTASKS;
                        } else {
                            return Endpoint.GET_SUBTASK;
                        }
                    }
                    case "POST" -> {
                        return Endpoint.POST_SUBTASK;
                    }
                    case "DELETE" -> {
                        if (query == null) {
                            return Endpoint.DELETE_ALL_SUBTASKS;
                        } else {
                            return Endpoint.DELETE_SUBTASK;
                        }
                    }
                    default -> {
                        return null;
                    }
                }
            }
            case "epic" -> {
                switch (requestMethod) {
                    case "GET" -> {
                        if (query == null) {
                            return Endpoint.GET_ALL_EPICS;
                        } else {
                            return Endpoint.GET_EPIC;
                        }
                    }
                    case "POST" -> {
                        return Endpoint.POST_EPIC;
                    }
                    case "DELETE" -> {
                        if (query == null) {
                            return Endpoint.DELETE_ALL_EPICS;
                        } else {
                            return Endpoint.DELETE_EPIC;
                        }
                    }
                    default -> {
                        return null;
                    }
                }
            }
            case "history" -> {
                switch (requestMethod) {
                    case "GET" -> {
                        return Endpoint.GET_HISTORY;
                    }
                    default -> {
                        return null;
                    }
                }
            }
            default -> {
                return null;
            }
        }
    }
    private static void writeResponse(int respCode, String resp, HttpExchange exchange) throws IOException {
        if(resp.isBlank()) {
            exchange.sendResponseHeaders(respCode, 0);
        } else {
            byte[] bytes = resp.getBytes(DEFAULT_CHARSET);
            exchange.sendResponseHeaders(respCode, bytes.length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(bytes);
            }
        }
        exchange.close();
    }
    static class TaskHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            Endpoint endpoint = getEndpoint(exchange.getRequestURI().getPath(),
                    exchange.getRequestMethod(),
                    exchange.getRequestURI().getRawQuery());
            switch (Objects.requireNonNull(endpoint)) {
                case GET_ALL_TASKS -> {
                    writeResponse(200, gson.toJson(manager.getAllTasks()), exchange);
                }
                case GET_TASK -> writeResponse(200, gson.toJson(manager.getTaskById(Integer
                                .parseInt(exchange
                                        .getRequestURI()
                                        .getRawQuery()
                                        .split("=")[1]))).toString(), exchange);
                case POST_TASK -> {
                    try{
                        manager.createTask(gson.fromJson(new String(exchange.getRequestBody().readAllBytes()), Task.class));
                        writeResponse(201, "Задача добавлена", exchange);
                    } catch (Exception e){
                        writeResponse(500, e.toString(), exchange);
                        System.out.println(e);
                    }

                }
                case DELETE_ALL_TASKS -> {
                    manager.deleteTasks();
                    writeResponse(200, "Задачи удалены", exchange);
                }
                case DELETE_TASK -> {
                    int idParam = Integer.parseInt(exchange.getRequestURI().getRawQuery().split("=")[1]);
                    manager.deleteTaskById(idParam);
                    writeResponse(200, "Задача удалена", exchange);
                }
                default -> writeResponse(404, "Данный метод для задач не существует", exchange);
            }
        }
    }
    static class SubtaskHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            Endpoint endpoint = getEndpoint(exchange.getRequestURI().getPath(),
                    exchange.getRequestMethod(),
                    exchange.getRequestURI().getRawQuery());
            switch (Objects.requireNonNull(endpoint)) {
                case GET_ALL_SUBTASKS -> {
                    writeResponse(200, gson.toJson(manager.getAllSubtasks()), exchange);
                }
                case GET_SUBTASK -> writeResponse(200, manager.getSubtaskById(Integer
                                .parseInt(exchange
                                        .getRequestURI()
                                        .getRawQuery()
                                        .split("=")[1]))
                        .toString(), exchange);
                case GET_EPIC_SUBTASKS -> writeResponse(200, manager.getSubtasksByEpic(Integer
                                .parseInt(exchange
                                        .getRequestURI()
                                        .getRawQuery()
                                        .split("=")[1]))
                        .toString(), exchange);
                case POST_SUBTASK -> {
                    try{
                        manager.createSubtask(gson.fromJson(new String(exchange.getRequestBody().readAllBytes()), Subtask.class));
                        writeResponse(201, "Подзадача добавлена", exchange);
                    } catch (Exception e){
                        writeResponse(500, e.toString(), exchange);
                        System.out.println(e);
                    }
                }
                case DELETE_ALL_SUBTASKS -> {
                    manager.deleteTasks();
                    writeResponse(200, "Подзадачи удалены", exchange);
                }
                case DELETE_SUBTASK -> {
                    int idParam = Integer.parseInt(exchange.getRequestURI().getRawQuery().split("=")[1]);
                    manager.deleteSubtaskById(idParam);
                    writeResponse(200, "Подзадача удалена", exchange);
                }
                default -> writeResponse(404, "Данный метод для подзадач не существует", exchange);
            }
        }
    }
    static class EpicHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            Endpoint endpoint = getEndpoint(exchange.getRequestURI().getPath(),
                    exchange.getRequestMethod(),
                    exchange.getRequestURI().getRawQuery());
            switch (Objects.requireNonNull(endpoint)) {
                case GET_ALL_EPICS -> {
                    writeResponse(200, manager.getAllEpics().toString(), exchange);
                }
                case GET_EPIC -> writeResponse(200, manager.getEpicById(Integer
                                .parseInt(exchange
                                        .getRequestURI()
                                        .getRawQuery()
                                        .split("=")[1]))
                        .toString(), exchange);
                case POST_EPIC -> {
                    try{
                        manager.createEpic(gson.fromJson(new String(exchange.getRequestBody().readAllBytes()), Epic.class));
                        writeResponse(201, "Эпик добавлен", exchange);
                    } catch (Exception e){
                        writeResponse(500, e.toString(), exchange);
                        System.out.println(e);
                    }
                }
                case DELETE_ALL_EPICS -> {
                    manager.deleteEpics();
                    writeResponse(200, "Эпики удалены", exchange);
                }
                case DELETE_EPIC -> {
                    int idParam = Integer.parseInt(exchange.getRequestURI().getRawQuery().split("=")[1]);
                    manager.deleteEpicById(idParam);
                    writeResponse(200, "Эпик удален", exchange);
                }
                default -> writeResponse(404, "Данный метод для эпиков не существует", exchange);
            }
        }
    }
    static class MainHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            Endpoint endpoint = getEndpoint(exchange.getRequestURI().getPath(),
                    exchange.getRequestMethod(),
                    exchange.getRequestURI().getRawQuery());
            switch (Objects.requireNonNull(endpoint)) {
                case GET_PRIORITIZED_TASKS -> {
                    writeResponse(200, manager.getPrioritizedTasks().toString(), exchange);
                }
                default -> writeResponse(404, "Данный метод для эпиков не существует", exchange);
            }
        }
    }
    static class HistoryHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            Endpoint endpoint = getEndpoint(exchange.getRequestURI().getPath(),
                    exchange.getRequestMethod(),
                    exchange.getRequestURI().getRawQuery());
            switch (Objects.requireNonNull(endpoint)) {
                case GET_HISTORY -> {
                    writeResponse(200, manager.getHistory().toString(), exchange);
                }
                default -> writeResponse(404, "Данный метод для эпиков не существует", exchange);
            }
        }
    }
}
