package ru.java.project.schedule.managers;


import ru.java.project.schedule.exceptions.ManagerSaveException;
import ru.java.project.schedule.tasks.Epic;
import ru.java.project.schedule.tasks.Subtask;
import ru.java.project.schedule.tasks.Task;
import ru.java.project.schedule.utils.CSVTaskFormat;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;

public class FileBackedTasksManager extends InMemoryTaskManager {

    private File filename;

    public FileBackedTasksManager(File file) {
        this.filename = file;
    }

    public FileBackedTasksManager() {
    }

    public void setFilename(File filename) {
        this.filename = filename;
    }

    public FileBackedTasksManager getLoadedManager() {
        return loadFromFile(Path.of(filename.toPath().toString()).toFile());
    }

    protected void save() throws ManagerSaveException {
        if (filename == null) {
            return;
        }
        try (BufferedWriter filewriter = new BufferedWriter(new FileWriter(filename.toPath().toString()))) {
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

    }
}
