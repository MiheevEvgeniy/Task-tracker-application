package managers;


import customExceptions.ManagerSaveException;
import tasks.*;

import java.io.*;
import java.nio.file.Path;

public class FileBackedTasksManager extends InMemoryTaskManager {
    private final String filename;

    public FileBackedTasksManager(String filename) {
        this.filename = filename;
        loadFromFile(Path.of(filename).toFile());
    }


    private void save() throws ManagerSaveException {

        try (Writer filewriter = new FileWriter(filename)) {

            for (Task task : super.getAllTasks()) {
                filewriter.write(task.getId() + "," + Types.TASK + "," + toString(task) + ",\n");
            }
            for (Epic epic : super.getAllEpics()) {
                filewriter.write(epic.getId() + "," + Types.EPIC + "," + toString(epic) + ",\n");
            }
            for (Subtask subtask : super.getAllSubtasks()) {
                filewriter.write(subtask.getId() + "," + Types.SUBTASK + "," + toString(subtask) + "," + subtask.getEpicId() + "\n");
            }
            filewriter.write("\n");
            filewriter.write(historyToString(super.historyManager));

        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка сохранения");
        }
    }

    public void loadFromFile(File file) {
        try (BufferedReader fileReader = new BufferedReader(new FileReader(file))) {
            String[] history;
            while (fileReader.ready()) {
                String line = fileReader.readLine();

                if (line.split(",").length != 1) {
                    String[] values = line.split(",");

                    if (values[1].equals(Types.TASK.toString())) {
                        super.createTask(new Task(values[2], values[4], Integer.parseInt(values[0]), Status.valueOf(values[3])));
                    }
                    if (values[1].equals(Types.EPIC.toString())) {
                        super.createEpic(new Epic(values[2], values[4], Integer.parseInt(values[0]), Status.valueOf(values[3])));
                    }
                    if (values[1].equals(Types.SUBTASK.toString())) {
                        super.createSubtask(new Subtask(values[2], values[4], Integer.parseInt(values[0]), Status.valueOf(values[3]), Integer.parseInt(values[5])));
                    }
                } else {
                    history = fileReader.readLine().split(",");
                    for (String el : history) {
                        if (getTaskById(Integer.parseInt(el)) != null) {
                            super.historyManager.add(getTaskById(Integer.parseInt(el)));
                        } else if (getEpicById(Integer.parseInt(el)) != null) {
                            super.historyManager.add(getEpicById(Integer.parseInt(el)));
                        } else {
                            super.historyManager.add(getSubtaskById(Integer.parseInt(el)));
                        }
                    }
                    break;
                }
            }

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

    }

    public String historyToString(HistoryManager manager) {
        StringBuilder stringBuilder = new StringBuilder();

        for (Task element : manager.getHistory()) {
            stringBuilder.append(element.getId());
            stringBuilder.append(',');
        }
        return stringBuilder.toString();
    }

    public String toString(Task task) {
        return task.getName() + "," + task.getStatus() + "," + task.getDescription();
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
