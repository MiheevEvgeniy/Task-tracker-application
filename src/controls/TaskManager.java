package controls;

import objects.Epic;
import objects.Subtask;
import objects.Task;
import objects.TaskStatuses;

import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    private int id = 0;

    private final HashMap<Integer, Task> Tasks = new HashMap<>();
    private final HashMap<Integer, Subtask> Subtasks = new HashMap<>();
    private final HashMap<Integer, Epic> Epics = new HashMap<>();
    
    //-------------Tasks--------------------
    public ArrayList<Task> getAllTasks() {
        return new ArrayList<>(Tasks.values());
    }

    public void deleteTasks() {
        Tasks.clear();
    }

    public Task getTaskById(int id) {

        return Tasks.get(id);

    }

    public void createTask(Task task) {
        id++;
        task.setId(id);
        task.setStatus(TaskStatuses.NEW.name());
        Tasks.put(id, task);
    }

    public void updateTask(Task updatedTask) {
        if (Tasks.containsValue(updatedTask)) {

                if (Tasks.get(updatedTask.getId()).equals(updatedTask)) {
                    if (updatedTask.getStatus() == null) {
                        updatedTask.setStatus(Tasks.get(updatedTask.getId()).getStatus());
                    }
                    Tasks.put(updatedTask.getId(), updatedTask);
                }
        } else {
            System.out.println("Задача не найдена.");
        }
    }

    public void deleteTaskById(int id) {
        if (!Tasks.containsKey(id)) {
            System.out.println("Задача не найдена");
        } else {
            Tasks.remove(id);
        }
    }


    //---------------Epics----------------


    public ArrayList<Epic> getAllEpics() {
        return new ArrayList<>(Epics.values());
    }

    public void deleteEpics() {
        Epics.clear();
    }

    public Epic getEpicById(int id) {
        return Epics.get(id);
    }

    public void createEpic(Epic epic) {
        id++;
        epic.setId(id);
        epic.setStatus(TaskStatuses.NEW.name());
        Epics.put(id, epic);
    }

    public void updateEpic(Epic updatedEpic) {
            if (Epics.get(updatedEpic.getId()).equals(updatedEpic)) {
                Epics.get(updatedEpic.getId()).setName(updatedEpic.getName());
                Epics.get(updatedEpic.getId()).setDescription(updatedEpic.getDescription());
        }else {
        System.out.println("Эпик не найден");
        }
    }

    public void deleteEpicById(int id) {
        if (!Epics.containsKey(id)) {
            System.out.println("Эпик не найден");
        } else {
            Epic epic = Epics.get(id);
            for (int key : epic.getSubtasks()) {
                deleteSubtaskById(key);
            }
            Epics.remove(id);
        }
    }


    //Subtasks

    public ArrayList<Subtask> getAllSubtasks() {
        return new ArrayList<>(Subtasks.values());
    }

    public void deleteSubtasks() {
        Subtasks.clear();
        for (Epic epic: Epics.values()) {
            epic.setStatus(TaskStatuses.NEW.name());
            epic.setSubtasks(new ArrayList<>());
        }
    }

    public Subtask getSubtaskById(int id) {
        return Subtasks.get(id);
    }

    public ArrayList<Subtask> getSubtasksByEpic(int epicId) {
        ArrayList<Subtask> epicSubtasks = new ArrayList<>();
        for (Epic epic :Epics.values()) {
            if (epic.getId() == epicId) {
                for (int key: epic.getSubtasks()) {
                    epicSubtasks.add(getSubtaskById(key));
                }
                return epicSubtasks;
            }
        }
        return null;

    }

    public void createSubtask(Subtask subtask) {
        id++;
        subtask.setId(id);
        subtask.setStatus(TaskStatuses.NEW.name());
        getEpicById(subtask.getEpicId()).addSubtask(id);
        Subtasks.put(id, subtask);
        updateEpicStatus(subtask.getEpicId());
    }

    public void updateSubtask(Subtask updatedSubtask) {
            Epic subtaskEpic = getEpicById(updatedSubtask.getEpicId());
            for (int key : Subtasks.keySet()) {
                if (key == updatedSubtask.getId()
                        && subtaskEpic.getSubtasks().contains(updatedSubtask.getId())) {
                    if (updatedSubtask.getStatus() == null) {
                        Subtask subtask = Subtasks.get(key);
                        updatedSubtask.setStatus(subtask.getStatus());
                    }
                    Subtasks.put(key, updatedSubtask);

                    updateEpicStatus(updatedSubtask.getEpicId());
                    return;
                }

            }
            System.out.println("Подзадача не найдена");

    }
    private void updateEpicStatus(int epicId){
        Epic epic = getEpicById(epicId);
        boolean isDone = true;
        boolean isInProgress = false;
        for (int subtaskId : epic.getSubtasks()) {
            System.out.println(subtaskId);
            if (!Subtasks.get(subtaskId).getStatus().equals(TaskStatuses.DONE.name())) {
                isDone = false;
            }
            if (!Subtasks.get(subtaskId).getStatus().equals(TaskStatuses.NEW.name())) {
                isInProgress = true;
            }
        }
        if (isDone) {
            epic.setStatus(TaskStatuses.DONE.name());
        } else if (isInProgress) {
            epic.setStatus(TaskStatuses.IN_PROGRESS.name());
        } else {
            epic.setStatus(TaskStatuses.NEW.name());
        }
    }

    public void deleteSubtaskById(int id) {
        if (!Subtasks.containsKey(id)) {
            System.out.println("Подзадача не найдена");
        } else {
            int epicId = getSubtaskById(id).getEpicId();
            Subtasks.remove(id);
            ArrayList<Integer> subtaskes = new ArrayList<>();
            for (int key: getEpicById(epicId).getSubtasks()) {
                if (key != id) {
                    subtaskes.add(key);
                }
            }
            getEpicById(epicId).setSubtasks(subtaskes);
            updateEpicStatus(epicId);
        }

    }


}
