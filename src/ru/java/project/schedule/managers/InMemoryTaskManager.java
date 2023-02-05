package ru.java.project.schedule.managers;

import ru.java.project.schedule.exceptions.ValidateException;
import ru.java.project.schedule.tasks.Epic;
import ru.java.project.schedule.tasks.Status;
import ru.java.project.schedule.tasks.Subtask;
import ru.java.project.schedule.tasks.Task;

import java.time.LocalDateTime;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {

    protected int id = 0;
    protected final HistoryManager historyManager = Managers.getDefaultHistory();
    protected final Map<Integer, Task> tasks = new HashMap<>();
    protected final Map<Integer, Subtask> subtasks = new HashMap<>();
    protected final Map<Integer, Epic> epics = new HashMap<>();
    protected final TreeSet<Task> sortedTasksAndSubtasks = new TreeSet<>(Comparator.comparing(Task::getStartTime));

    //-------------ru.java.project.schedule.tasks--------------------
    @Override
    public ArrayList<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public void deleteTasks() {
        for (Task task : tasks.values()) {
            historyManager.remove(task.getId());
            sortedTasksAndSubtasks.remove(task);
        }
        tasks.clear();
    }

    @Override
    public Task getTaskById(int id) {
        historyManager.add(tasks.get(id));
        return tasks.get(id);

    }

    @Override
    public void createTask(Task task) {
        task.setId(id + 1);
        if (task.getStatus() == null) {
            task.setStatus(Status.NEW);
        }
        add(task);
        id++;
        tasks.put(id, task);
    }

    @Override
    public void updateTask(Task updatedTask) {
        if (tasks.containsValue(updatedTask)) {
            if (updatedTask.getStatus() == null) {
                updatedTask.setStatus(tasks.get(updatedTask.getId()).getStatus());
            }
            sortedTasksAndSubtasks.remove(tasks.get(updatedTask.getId()));
            System.out.println(getPrioritizedTasks());
            tasks.put(updatedTask.getId(), updatedTask);
            add(updatedTask);
        } else {
            System.out.println("Задача не найдена.");
        }
    }

    @Override
    public void deleteTaskById(int id) {
        if (!tasks.containsKey(id)) {
            System.out.println("Задача не найдена");
        } else {
            historyManager.remove(id);
            sortedTasksAndSubtasks.remove(tasks.get(id));
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
        for (Epic epic : epics.values()) {
            historyManager.remove(epic.getId());
            for (Integer subId : epic.getSubtasks()) {
                historyManager.remove(subId);
            }
        }
        subtasks.clear();
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
        if (epic.getStatus() == null) {
            epic.setStatus(Status.NEW);
        }
        epics.put(id, epic);
    }

    @Override
    public void updateEpic(Epic updatedEpic) {
        if (epics.get(updatedEpic.getId()) == null) {
            System.out.println("Эпик не найден");
            return;
        }
        if (epics.get(updatedEpic.getId()).equals(updatedEpic)) {
            epics.get(updatedEpic.getId()).setName(updatedEpic.getName());
            epics.get(updatedEpic.getId()).setDescription(updatedEpic.getDescription());
        }
    }

    @Override
    public void deleteEpicById(int id) {
        if (!epics.containsKey(id)) {
            System.out.println("Эпик не найден");
        } else {
            Epic epic = epics.get(id);
            for (Integer subId : epic.getSubtasks()) {
                subtasks.remove(subId);
                historyManager.remove(subId);
            }
            epic.clear();
            epics.remove(id);
            historyManager.remove(id);
        }
    }


    //subtasks

    @Override
    public ArrayList<Subtask> getAllSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    @Override
    public void deleteSubtasks() {
        for (Subtask subtask : subtasks.values()) {
            historyManager.remove(subtask.getId());
            sortedTasksAndSubtasks.remove(subtask);
        }
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
        subtask.setId(id + 1);
        if (subtask.getStatus() == null) {
            subtask.setStatus(Status.NEW);
        }
        add(subtask);
        id++;
        epics.get(subtask.getEpicId()).addSubtask(id);
        subtasks.put(id, subtask);
        updateEpic(subtask.getEpicId());
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
        if (updatedSubtask.getStatus() == null) {
            updatedSubtask.setStatus(savedSubtask.getStatus());
        }
        subtasks.put(id, updatedSubtask);
        sortedTasksAndSubtasks.remove(savedSubtask);
        add(updatedSubtask);
        updateEpic(epicId);
    }

    @Override
    public void deleteSubtaskById(int id) {
        if (!subtasks.containsKey(id)) {
            System.out.println("Подзадача не найдена");
        } else {
            sortedTasksAndSubtasks.remove(subtasks.get(id));
            Subtask subtask = subtasks.remove(id);
            historyManager.remove(id);
            Epic epic = epics.get(subtask.getEpicId());
            epic.removeSubtask(id);
            updateEpic(epic.getId());
        }
    }

    @Override
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

    private void add(Task task) {
        final LocalDateTime startTime = task.getStartTime();
        final LocalDateTime endTime = task.getEndTime();
        for (Task t : sortedTasksAndSubtasks) {
            final LocalDateTime existStart = t.getStartTime();
            final LocalDateTime existEnd = t.getEndTime();
            if (!endTime.isAfter(existStart)) {
                continue;
            }
            if (!existEnd.isAfter(startTime)) {
                continue;
            }
            throw new ValidateException("Задача пересекается с id = " + t.getId() + " c " + existStart + " по " + existEnd);
        }
        sortedTasksAndSubtasks.add(task);
    }

    protected void updateEpic(int epicId) {
        updateEpicStatus(epicId);
        updateEpicDuration(epicId);
    }

    private void updateEpicDuration(int epicId) {
        Epic epic = epics.get(epicId);
        List<Integer> subtasksId = epic.getSubtasks();
        if (subtasksId.isEmpty()) {
            epic.setDuration(0);
            return;
        }
        LocalDateTime start = LocalDateTime.MAX;
        LocalDateTime end = LocalDateTime.MIN;
        int duration = 0;
        for (int id : subtasksId) {
            final Subtask subtask = subtasks.get(id);
            final LocalDateTime startTime = subtask.getStartTime();
            final LocalDateTime endTime = subtask.getEndTime();
            if (startTime.isBefore(start)) {
                start = startTime;
            }
            if (endTime.isAfter(end)) {
                end = endTime;
            }
            duration += subtask.getDuration();
        }
        epic.setDuration(duration);
        epic.setStartTime(start);
        epic.setEndTime(end);
    }

    @Override
    public List<Task> getPrioritizedTasks() {
        return new ArrayList<>(sortedTasksAndSubtasks);
    }
}
