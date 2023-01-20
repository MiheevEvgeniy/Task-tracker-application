package managers;


import exceptions.ManagerSaveException;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import utils.CSVTaskFormat;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;

public class FileBackedTasksManager extends InMemoryTaskManager {

    private String filename;

    public FileBackedTasksManager(File file) {
        this.filename = file.toPath().toString();
    }

    public FileBackedTasksManager() {
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public FileBackedTasksManager getLoadedManager() {
        return loadFromFile(Path.of(filename).toFile());
    }

    private void save() throws ManagerSaveException {

        try (BufferedWriter filewriter = new BufferedWriter(new FileWriter(filename))) {
            filewriter.write(CSVTaskFormat.getHeader());
            filewriter.newLine();

            for (Task task : super.getAllTasks()) {
                filewriter.write(CSVTaskFormat.toString(task));
            }
            for (Epic epic : super.getAllEpics()) {
                filewriter.write(CSVTaskFormat.toString(epic));
            }
            for (Subtask subtask : super.getAllSubtasks()) {
                filewriter.write(CSVTaskFormat.toString(subtask));
            }
            filewriter.write("\n");
            filewriter.write(CSVTaskFormat.historyToString(super.historyManager));

        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка сохранения");
        }
    }

    public static FileBackedTasksManager loadFromFile(File file) {
        final FileBackedTasksManager tasksManager = new FileBackedTasksManager(file);
        try {
            final String csv = Files.readString(file.toPath());
            final String[] lines = csv.split("\n");
            int generatorId = 0;
            List<Integer> history = Collections.emptyList();

            for (int i = 1; i < lines.length; i++) {
                String line = lines[i];
                if (line.isEmpty()) {
                    history = CSVTaskFormat.historyFromString(lines[i + 1]);
                    break;
                }
                final Task task = CSVTaskFormat.fromString(line);
                final int id = task.getId();
                if (id > generatorId) {
                    generatorId = id;
                }
                tasksManager.addAnyTask(task);
            }
            for (Integer taskId : history) {
                tasksManager.historyManager.add(tasksManager.findTask(taskId));
            }
            tasksManager.id = generatorId;

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return tasksManager;
    }

    protected void addAnyTask(Task task) {
        switch (task.getType()) {
            case TASK -> super.createTask(task);
            case SUBTASK -> super.createSubtask((Subtask) task);
            case EPIC -> super.createEpic((Epic) task);
        }
    }

    protected Task findTask(int id) {
        final Task task = tasks.get(id);
        if (task != null) {
            return task;
        }
        final Task subtask = subtasks.get(id);
        if (subtask != null) {
            return subtask;
        }
        return epics.get(id);
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

    public static void main(String[] args) {
        FileBackedTasksManager tm = new FileBackedTasksManager(Path.of("save.csv").toFile());
        tm.createTask(new Task("Задача1", "Описание1"));
        tm.createTask(new Task("Задача2", "Описание2"));
        tm.createEpic(new Epic("Эпик1", "Описание Эпика"));
        tm.createSubtask(new Subtask("Подзадача1", "Описание подзадачи1", 3));
        tm.createSubtask(new Subtask("Подзадача2", "Описание подзадачи2", 3));
        tm.createSubtask(new Subtask("Подзадача3", "Описание подзадачи3", 3));

        tm.getTaskById(1);
        tm.getEpicById(3);
        tm.getEpicById(3);
        tm.getTaskById(2);
        tm.getTaskById(1);
        tm.getSubtaskById(4);
        tm.getSubtaskById(6);
        tm.getSubtaskById(5);
        tm.getSubtaskById(4);

        System.out.println("История старая:");
        for (Task task : tm.getHistory()) {
            System.out.println(task.toString());
        }
        System.out.println("Данные:");
        System.out.println(tm.getAllEpics());
        System.out.println(tm.getAllTasks());
        System.out.println(tm.getAllSubtasks());

        System.out.println("--------------------------------");
        System.out.println("История новая:");
        FileBackedTasksManager newTm = tm.getLoadedManager();
        for (Task task : newTm.getHistory()) {
            System.out.println(task.toString());
        }
        System.out.println("Данные:");
        System.out.println(newTm.getAllEpics());
        System.out.println(newTm.getAllTasks());
        System.out.println(newTm.getAllSubtasks());
    }
}
