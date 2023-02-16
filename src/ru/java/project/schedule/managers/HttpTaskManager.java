package ru.java.project.schedule.managers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ru.java.project.schedule.client.KVTaskClient;
import ru.java.project.schedule.server.HttpTaskServer;
import ru.java.project.schedule.server.KVServer;
import ru.java.project.schedule.server.adapters.LocalDateTimeAdapter;
import ru.java.project.schedule.server.deserializers.EpicDeserializer;
import ru.java.project.schedule.server.deserializers.SubtaskDeserializer;
import ru.java.project.schedule.server.deserializers.TaskDeserializer;
import ru.java.project.schedule.tasks.Epic;
import ru.java.project.schedule.tasks.Subtask;
import ru.java.project.schedule.tasks.Task;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class HttpTaskManager extends FileBackedTasksManager implements TaskManager{
    String url;
    String keyManager;
    KVTaskClient kvTaskClient;
    private final static Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .registerTypeAdapter(Subtask.class, new SubtaskDeserializer())
            .registerTypeAdapter(Epic.class, new EpicDeserializer())
            .registerTypeAdapter(Task.class, new TaskDeserializer())
            .create();
    public HttpTaskManager(String url, String keyManager) throws IOException, InterruptedException {
        this.url = url;
        this.keyManager = keyManager;
        kvTaskClient = new KVTaskClient(url);

    }
    public void save() {
        List<Task> tasks = new ArrayList<>();
        tasks.addAll(getPrioritizedTasks());
        tasks.addAll(getAllEpics());
        try{
            ManagerObjects managerObjects = new ManagerObjects(tasks, getHistory());
        System.out.println(gson.toJson(managerObjects));
        kvTaskClient.put(keyManager, gson.toJson(new ManagerObjects(tasks, getHistory())));
        }catch (IOException|InterruptedException e){
            System.out.println(e);
        }
    }
    static class ManagerObjects{
        private final List<Task> tasks;
        private final List<Task> history;

        public ManagerObjects(List<Task> tasks, List<Task> history) {
            this.tasks = tasks;
            this.history = history;
        }

        public List<Task> getTasks() {
            return tasks;
        }

        public List<Task> getHistory() {
            return history;
        }
    }
    public TaskManager load() {
        try{
            ManagerObjects managerObjects = gson.fromJson(kvTaskClient.load(keyManager), ManagerObjects.class);
            List<Task> tasks = managerObjects.getTasks();
            List<Task> history = managerObjects.getHistory();
            HttpTaskManager manager = new HttpTaskManager(url, keyManager);
            for (Task task: tasks) {
                manager.addAnyTask(task);
            }
            for (Task el:history) {
                manager.historyManager.add(el);
            }
            return manager;
        }catch (IOException|InterruptedException e){
            System.out.println(e);
            return null;
        }
    }
    @Override
    public void deleteTasks() {
        super.deleteTasks();
        save();
    }

    @Override
    public Task getTaskById(int id) {
        Task task = super.getTaskById(id);
        save();
        return task;
    }

    @Override
    public void createTask(Task task) {
        super.createTask(task);
        save();
    }

    @Override
    public void updateTask(Task updatedTask) {
        super.updateTask(updatedTask);
        save();
    }

    @Override
    public void deleteTaskById(int id) {
        super.deleteTaskById(id);
        save();
    }

    @Override
    public void deleteEpics() {
        super.deleteEpics();
        save();
    }

    @Override
    public Epic getEpicById(int id) {
        Epic epic = super.getEpicById(id);
        save();
        return epic;
    }

    @Override
    public void createEpic(Epic epic) {
        super.createEpic(epic);
        save();
    }

    @Override
    public void updateEpic(Epic updatedEpic) {
        super.updateEpic(updatedEpic);
        save();
    }

    @Override
    public void deleteEpicById(int id) {
        super.deleteEpicById(id);
        save();
    }

    @Override
    public void deleteSubtasks() {
        super.deleteSubtasks();
        save();
    }

    @Override
    public Subtask getSubtaskById(int id) {
        Subtask subtask = super.getSubtaskById(id);
        save();
        return subtask;
    }

    @Override
    public void createSubtask(Subtask subtask) {
        super.createSubtask(subtask);
        save();
    }

    @Override
    public void updateSubtask(Subtask updatedSubtask) {
        super.updateSubtask(updatedSubtask);
        save();
    }

    @Override
    public void deleteSubtaskById(int id) {
        super.deleteSubtaskById(id);
        save();
    }

}
