package managers;

import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;

import java.time.LocalDateTime;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {

    protected int id = 0;
    protected final HistoryManager historyManager = Managers.getDefaultHistory();
    protected final Map<Integer, Task> tasks = new HashMap<>();
    protected final Map<Integer, Subtask> subtasks = new HashMap<>();
    protected final Map<Integer, Epic> epics = new HashMap<>();
    protected final TreeSet<Task> sortedTasksAndSubtasks = new TreeSet<>(Comparator.comparing(Task::getStartTime));

    //-------------tasks--------------------
    @Override
    public ArrayList<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public void deleteTasks() {
        for (Task task : tasks.values()) {
            historyManager.remove(task.getId());
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
        if (!validate(task)) {
            System.out.println("Задача пересекается с другой");
            return;
        }
        id++;
        task.setId(id);
        if (task.getStatus() == null) {
            task.setStatus(Status.NEW);
        }
        tasks.put(id, task);
        sortedTasksAndSubtasks.add(task);
    }

    @Override
    public void updateTask(Task updatedTask) {
        if (!validate(updatedTask)) {
            System.out.println("Задача пересекается с другой");
            return;
        }
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
            historyManager.remove(id);
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
        if (!validate(subtask)) {
            System.out.println("Подзадача пересекается с другой");
            return;
        }
        id++;
        subtask.setId(id);
        if (subtask.getStatus() == null) {
            subtask.setStatus(Status.NEW);
        }
        epics.get(subtask.getEpicId()).addSubtask(id);
        epics.get(subtask.getEpicId()).addDuration(subtask.getDuration());
        if (epics.get(subtask.getEpicId()).getStartTime().isAfter(subtask.getStartTime())) {
            epics.get(subtask.getEpicId()).setStartTime(subtask.getStartTime());
        }
        if (epics.get(subtask.getEpicId()).getEndTime().isBefore(subtask.getEndTime())) {
            epics.get(subtask.getEpicId()).setEndTime(subtask.getEndTime());
        }
        subtasks.put(id, subtask);
        sortedTasksAndSubtasks.add(subtask);
        updateEpicStatus(subtask.getEpicId());
    }

    @Override
    public void updateSubtask(Subtask updatedSubtask) {
        if (!validate(updatedSubtask)) {
            System.out.println("Подзадача пересекается с другой");
            return;
        }
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
        updateEpicStatus(epicId);
    }

    @Override
    public void deleteSubtaskById(int id) {
        Subtask subtask = subtasks.remove(id);
        historyManager.remove(id);
        if (subtask == null) {
            return;
        }
        Epic epic = epics.get(subtask.getEpicId());
        epic.removeSubtask(id);
        updateEpicStatus(epic.getId());


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

    private boolean validate(Task task) {
        if (task.getStartTime().equals(LocalDateTime.MAX)
                && task.getEndTime().equals(LocalDateTime.MAX)) {
            return true;
        }
        List<Task> tasks = getPrioritizedTasks();
        for (Task sortedTask : tasks) {
            if (task.getStartTime().equals(sortedTask.getStartTime())
                    || task.getEndTime().equals(sortedTask.getEndTime())
                    || (task.getStartTime().isBefore(sortedTask.getStartTime())) && task.getEndTime().isAfter(sortedTask.getStartTime())
                    || (task.getStartTime().isBefore(sortedTask.getEndTime())) && task.getEndTime().isAfter(sortedTask.getEndTime())
                    || (task.getStartTime().isAfter(sortedTask.getStartTime())) && task.getEndTime().isBefore(sortedTask.getEndTime())) {
                return false;
            }
        }
        return true;
    }

    @Override
    public List<Task> getPrioritizedTasks() {
        return sortedTasksAndSubtasks.stream().toList();
    }
}
