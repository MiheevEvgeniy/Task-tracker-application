package ru.java.project.schedule.server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import ru.java.project.schedule.managers.Managers;
import ru.java.project.schedule.managers.TaskManager;
import ru.java.project.schedule.server.adapters.LocalDateTimeAdapter;
import ru.java.project.schedule.server.deserializers.EpicDeserializer;
import ru.java.project.schedule.server.deserializers.SubtaskDeserializer;
import ru.java.project.schedule.server.deserializers.TaskDeserializer;
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
    public static final int PORT = 8080;
    private final HttpServer server;
    private final Gson gson;
    private final TaskManager manager;
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

    public HttpTaskServer() throws IOException {
        this(Managers.getDefault());
    }

    public HttpTaskServer(TaskManager httpTaskManager) throws IOException {
        gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .registerTypeAdapter(Subtask.class, new SubtaskDeserializer())
                .registerTypeAdapter(Epic.class, new EpicDeserializer())
                .registerTypeAdapter(Task.class, new TaskDeserializer())
                .create();
        this.manager = httpTaskManager;
        this.server = HttpServer.create(new InetSocketAddress(PORT), 0);
        server.createContext("/tasks", this::handler);
    }

    public void start() {
        server.start();
        System.out.println("Server started...");
    }

    public void stop() {
        server.stop(0);
    }

    private void handler(HttpExchange exchange) {
        try (exchange) {
            Endpoint endpoint = getEndpoint(exchange);
            switch (Objects.requireNonNull(endpoint)) {
                case GET_ALL_TASKS -> writeResponse(200, gson.toJson(manager.getAllTasks()), exchange);
                case GET_TASK -> writeResponse(200, gson.toJson(manager.getTaskById(Integer
                        .parseInt(exchange
                                .getRequestURI()
                                .getRawQuery()
                                .split("=")[1]))), exchange);
                case POST_TASK -> {
                    try {
                        Task task = gson.fromJson(new String(exchange.getRequestBody().readAllBytes()), Task.class);
                        if (exchange.getRequestURI().getRawQuery() != null) {
                            String[] rawQuery = exchange
                                    .getRequestURI()
                                    .getRawQuery()
                                    .split("=");
                            if (rawQuery[0].equals("id")) {
                                task.setId(Integer
                                        .parseInt(rawQuery[1]));
                                manager.updateTask(task);
                                writeResponse(201, "Задача обновлена", exchange);
                            } else {
                                writeResponse(405, "Неизвестный параметр запроса: " + rawQuery[0], exchange);
                            }
                        } else {
                            manager.createTask(task);
                            writeResponse(201, "Задача добавлена", exchange);
                        }
                    } catch (Exception e) {
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
                case GET_ALL_SUBTASKS -> writeResponse(200, gson.toJson(manager.getAllSubtasks()), exchange);
                case GET_SUBTASK -> writeResponse(200, gson.toJson(manager.getSubtaskById(Integer
                        .parseInt(exchange
                                .getRequestURI()
                                .getRawQuery()
                                .split("=")[1]))), exchange);
                case GET_EPIC_SUBTASKS -> writeResponse(200, gson.toJson(manager.getSubtasksByEpic(Integer
                        .parseInt(exchange
                                .getRequestURI()
                                .getRawQuery()
                                .split("=")[1]))), exchange);
                case POST_SUBTASK -> {
                    try {
                        Subtask subtask = gson.fromJson(new String(exchange.getRequestBody().readAllBytes()), Subtask.class);
                        if (exchange.getRequestURI().getRawQuery() != null) {
                            String[] rawQuery = exchange
                                    .getRequestURI()
                                    .getRawQuery()
                                    .split("=");
                            if (rawQuery[0].equals("id")) {
                                subtask.setId(Integer
                                        .parseInt(rawQuery[1]));
                                manager.updateSubtask(subtask);
                                writeResponse(201, "Подзадача обновлена", exchange);
                            } else {
                                writeResponse(405, "Неизвестный параметр запроса: " + rawQuery[0], exchange);
                            }
                        } else {
                            manager.createSubtask(subtask);
                            writeResponse(201, "Подзадача добавлена", exchange);
                        }
                    } catch (Exception e) {
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
                case GET_ALL_EPICS -> writeResponse(200, gson.toJson(manager.getAllEpics()), exchange);

                case GET_EPIC -> writeResponse(200, gson.toJson(manager.getEpicById(Integer
                        .parseInt(exchange
                                .getRequestURI()
                                .getRawQuery()
                                .split("=")[1]))), exchange);
                case POST_EPIC -> {
                    try {
                        Epic epic = gson.fromJson(new String(exchange.getRequestBody().readAllBytes()), Epic.class);
                        if (exchange.getRequestURI().getRawQuery() != null) {
                            String[] rawQuery = exchange
                                    .getRequestURI()
                                    .getRawQuery()
                                    .split("=");
                            if (rawQuery[0].equals("id")) {
                                epic.setId(Integer
                                        .parseInt(rawQuery[1]));
                                manager.updateEpic(epic);
                                writeResponse(201, "Эпик обновлен", exchange);
                            } else {
                                writeResponse(405, "Неизвестный параметр запроса: " + rawQuery[0], exchange);
                            }
                        } else {
                            manager.createEpic(epic);
                            writeResponse(201, "Эпик добавлен", exchange);
                        }
                    } catch (Exception e) {
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
                case GET_PRIORITIZED_TASKS -> writeResponse(200, gson.toJson(manager.getPrioritizedTasks()), exchange);

                case GET_HISTORY -> writeResponse(200, gson.toJson(manager.getHistory()), exchange);

                default -> writeResponse(404, "Метод не существует", exchange);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Endpoint getEndpoint(HttpExchange exchange) {
        try {
            final String path = exchange.getRequestURI().getPath().substring(7);
            String requestMethod = exchange.getRequestMethod();
            String query = exchange.getRequestURI().getQuery();
            switch (path) {
                case "" -> {
                    switch (requestMethod) {
                        case "GET" -> {
                            return Endpoint.GET_PRIORITIZED_TASKS;
                        }
                        default -> {
                            System.out.println("/ Ждёт GET-запрос, а получил: " + exchange.getRequestMethod());
                            exchange.sendResponseHeaders(405, 0);
                            return null;
                        }
                    }
                }
                case "task/" -> {
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
                case "subtask/" -> {
                    switch (requestMethod) {
                        case "GET" -> {
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
                case "subtask/epic/" -> {
                    switch (requestMethod) {
                        case "GET" -> {
                            return Endpoint.GET_EPIC_SUBTASKS;
                        }
                        default -> {
                            System.out.println("/subtask/epic Ждёт GET-запрос, а получил: " + exchange.getRequestMethod());
                            exchange.sendResponseHeaders(405, 0);
                            return null;
                        }
                    }
                }
                case "epic/" -> {
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
                case "history/" -> {
                    switch (requestMethod) {
                        case "GET" -> {
                            return Endpoint.GET_HISTORY;
                        }
                        default -> {
                            System.out.println("/history Ждёт GET-запрос, а получил: " + exchange.getRequestMethod());
                            exchange.sendResponseHeaders(405, 0);
                            return null;
                        }
                    }
                }
                default -> {
                    System.out.println("Неизвестный запрос: " + exchange.getRequestURI());
                    exchange.sendResponseHeaders(404, 0);
                    return null;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static void writeResponse(int respCode, String resp, HttpExchange exchange) throws IOException {
        if (resp.isBlank()) {
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
}
