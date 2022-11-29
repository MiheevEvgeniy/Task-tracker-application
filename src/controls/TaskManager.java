package controls;

import objects.Epic;
import objects.Subtask;
import objects.Task;

import java.util.Collection;
import java.util.HashMap;

public class TaskManager {
    private int id = 0;

    private HashMap<Integer, Object> allTasks = new HashMap<>();
    private HashMap<Integer, Object> allSubtasks = new HashMap<>();
    private HashMap<Integer, Object> allEpics = new HashMap<>();


    //-------------Tasks--------------------
    public HashMap<Integer, Object> getAllTasks() {
        return allTasks;
    }

    public void deleteAllTasks() {
        allTasks.clear();
    }

    public Task getTaskByID(int id) {

        return (Task) allTasks.get(id);

    }

    public void createTask(Task task) {
        task.setId(id);
        task.setStatus("NEW");
        allTasks.put(id, task);
        id++;
    }

    public void updateTask(Task updatedTask, int id) {
        updatedTask.setId(id);
        for (int key : allTasks.keySet()) {
            if (allTasks.get(key).equals(updatedTask)) {
                if (updatedTask.getStatus() == null) {
                    Task task = (Task) allTasks.get(key);
                    updatedTask.setStatus(task.getStatus());
                }
                allTasks.put(key, updatedTask);
                return;
            }
        }
        System.out.println("Задача с указанным id не найдена.");
    }

    public void deleteTaskByID(int id) {
        if (!allTasks.containsKey(id)) {
            System.out.println("Задача не найдена");
        } else {
            allTasks.remove(id);
        }
    }


    //---------------Epics----------------


    public HashMap<Integer, Object> getAllEpics() {
        return allEpics;
    }

    public void deleteAllEpics() {
        allEpics.clear();
    }

    public Epic getEpicByID(int id) {
        return (Epic) allEpics.get(id);
    }

    public void createEpic(Epic epic) {
        epic.setId(id);
        epic.setStatus("NEW");
        allEpics.put(id, epic);
        id++;
    }

    public void updateEpic(Epic updatedEpic) {
        for (int key : allEpics.keySet()) {
            if (allEpics.get(key).equals(updatedEpic)) {
                allEpics.put(key, updatedEpic);
                return;
            }
        }
        System.out.println("Эпик с указанным id не найден");
    }

    public void deleteEpicByID(int id) {
        if (!allEpics.containsKey(id)) {
            System.out.println("Эпик не найден");
        } else {
            Epic epic = (Epic) allEpics.get(id);
            for (Subtask subtask : epic.subtasks.values()) {
                deleteSubtaskByID(subtask.getId());
            }
            allEpics.remove(id);
        }
    }


    //Subtasks

    public HashMap<Integer, Object> getAllSubtasks() {
        return allSubtasks;
    }

    public void deleteAllSubtasks() {
        allSubtasks.clear();
    }

    public Subtask getSubtaskByID(int id) {
        return (Subtask) allSubtasks.get(id);
    }

    public Collection<Subtask> getSubtasksByEpic(Epic epic) {
        for (Object value :allEpics.values()) {
            Epic val = (Epic) value;
            if (val.equals(epic)) {
                return val.subtasks.values();
            }
        }
        return null;

    }

    public void createSubtask(Subtask subtask) {
        subtask.setId(id);
        subtask.setStatus("NEW");
        subtask.epic.subtasks.put(id, subtask);
        allSubtasks.put(id, subtask);
        id++;
    }

    public void updateSubtask(Subtask updatedSubtask, int id) {
        updatedSubtask.setId(id);
        for (int key : allSubtasks.keySet()) {
            if (key == updatedSubtask.getId() && updatedSubtask.epic.subtasks.containsKey(updatedSubtask.getId())) {
                if (updatedSubtask.getStatus() == null) {
                    Subtask subtask = (Subtask) allSubtasks.get(key);
                    updatedSubtask.setStatus(subtask.getStatus());
                }
                allSubtasks.put(key, updatedSubtask);
                updatedSubtask.epic.subtasks.put(key, updatedSubtask);

                if (updatedSubtask.getStatus().equals("IN PROGRESS") || updatedSubtask.getStatus().equals("DONE")) {
                    updatedSubtask.epic.setStatus("IN PROGRESS");
                }
                if (updatedSubtask.getStatus().equals("DONE")) {
                    boolean isDone = true;
                    for (Subtask subtask : updatedSubtask.epic.subtasks.values()) {
                        if (!subtask.getStatus().equals("DONE")) {
                            isDone = false;
                        }
                    }
                    if (isDone) {
                        updatedSubtask.epic.setStatus("DONE");
                    }
                }

                return;
            }

        }
        System.out.println("Подзадача с указанным id не найдена");
    }

    public void deleteSubtaskByID(int id) {
        if (!allSubtasks.containsKey(id)) {
            System.out.println("Подзадача не найдена");
        } else {
            allSubtasks.remove(id);
        }

    }


}
