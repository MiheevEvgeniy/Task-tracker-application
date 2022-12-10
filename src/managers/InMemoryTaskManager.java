package managers;

import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import tasks.Status;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryTaskManager implements TaskManager {

    private int id = 0;
    private final HistoryManager historyManager = Managers.getDefaultHistory();
    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HashMap<Integer, Subtask> subtasks = new HashMap<>();
    private final HashMap<Integer, Epic> epics = new HashMap<>();

    //-------------tasks--------------------
    @Override
    public ArrayList<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public void deleteTasks() {
        tasks.clear();
    }

    @Override
    public Task getTaskById(int id) {
        historyManager.add(tasks.get(id));
        return tasks.get(id);

    }

    @Override
    public void createTask(Task task) {
        id++;
        task.setId(id);
        task.setStatus(Status.NEW);
        tasks.put(id, task);
    }

    @Override
    public void updateTask(Task updatedTask) {
        if (tasks.containsValue(updatedTask)) {
            if (updatedTask.getStatus() == null) {
                updatedTask.setStatus(tasks.get(updatedTask.getId()).getStatus());
            }
            tasks.put(updatedTask.getId(), updatedTask);
        } else {
            System.out.println("Задача не найдена.");
        }
    }

    @Override
    public void deleteTaskById(int id) {
        if (!tasks.containsKey(id)) {
            System.out.println("Задача не найдена");
        } else {
            tasks.remove(id);
        }
    }


    //---------------epics----------------

    @Override
    public ArrayList<Epic> getAllEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public void deleteEpics() {
        epics.clear();
    }

    @Override
    public Epic getEpicById(int id) {
        historyManager.add(epics.get(id));
        return epics.get(id);
    }

    @Override
    public void createEpic(Epic epic) {
        id++;
        epic.setId(id);
        epic.setStatus(Status.NEW);
        epics.put(id, epic);
    }

    @Override
    public void updateEpic(Epic updatedEpic) {
        if (epics.get(updatedEpic.getId()).equals(updatedEpic)) {
            epics.get(updatedEpic.getId()).setName(updatedEpic.getName());
            epics.get(updatedEpic.getId()).setDescription(updatedEpic.getDescription());
        } else {
            System.out.println("Эпик не найден");
        }
    }

    @Override
    public void deleteEpicById(int id) {
        if (!epics.containsKey(id)) {
            System.out.println("Эпик не найден");
        } else {
            Epic epic = epics.get(id);
            epic.clear();
            epics.remove(id);
        }
    }


    //subtasks

    @Override
    public ArrayList<Subtask> getAllSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    @Override
    public void deleteSubtasks() {
        subtasks.clear();
        for (Epic epic : epics.values()) {
            epic.setStatus(Status.NEW);
            epic.clear();
        }
    }

    @Override
    public Subtask getSubtaskById(int id) {
        historyManager.add(subtasks.get(id));
        return subtasks.get(id);
    }

    @Override
    public ArrayList<Subtask> getSubtasksByEpic(int epicId) {
        ArrayList<Subtask> epicSubtasks = new ArrayList<>();
        for (Epic epic : epics.values()) {
            if (epic.getId() == epicId) {
                for (int key : epic.getSubtasks()) {
                    epicSubtasks.add(subtasks.get(key));
                }
                return epicSubtasks;
            }
        }
        return null;

    }

    @Override
    public void createSubtask(Subtask subtask) {
        id++;
        subtask.setId(id);
        subtask.setStatus(Status.NEW);
        epics.get(subtask.getEpicId()).addSubtask(id);
        subtasks.put(id, subtask);
        updateEpicStatus(subtask.getEpicId());
    }

    @Override
    public void updateSubtask(Subtask updatedSubtask) {
        final int id = updatedSubtask.getId();
        final int epicId = updatedSubtask.getEpicId();
        final Subtask savedSubtask = subtasks.get(id);

        if (savedSubtask == null) {
            System.out.println("Подзадача не найдена");
            return;
        }

        final Epic epic = epics.get(epicId);

        if (epic == null) {
            System.out.println("Эпик подзадачи не найден");
            return;
        }
        subtasks.put(id, updatedSubtask);
        updateEpicStatus(epicId);
    }

    @Override
    public void deleteSubtaskById(int id) {
        Subtask subtask = subtasks.remove(id);
        if (subtask == null) {
            return;
        }
        Epic epic = epics.get(subtask.getEpicId());
        epic.removeSubtask(id);
        updateEpicStatus(epic.getId());


    }

    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    private void updateEpicStatus(int epicId) {
        Epic epic = epics.get(epicId);
        boolean isDone = true;
        boolean isInProgress = false;
        for (int subtaskId : epic.getSubtasks()) {
            if (!subtasks.get(subtaskId).getStatus().equals(Status.DONE)) {
                isDone = false;
            }
            if (!subtasks.get(subtaskId).getStatus().equals(Status.NEW)) {
                isInProgress = true;
            }
        }
        if (isDone) {
            epic.setStatus(Status.DONE);
        } else if (isInProgress) {
            epic.setStatus(Status.IN_PROGRESS);
        } else {
            epic.setStatus(Status.NEW);
        }
    }
}
