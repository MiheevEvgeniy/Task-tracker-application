package managers;

import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.util.List;

public interface TaskManager {
    //-------------tasks--------------------
    List<Task> getAllTasks();

    void deleteTasks();

    Task getTaskById(int id);

    void createTask(Task task);

    void updateTask(Task updatedTask);

    void deleteTaskById(int id);

    //---------------epics----------------


    List<Epic> getAllEpics();

    void deleteEpics();

    Epic getEpicById(int id);

    void createEpic(Epic epic);

    void updateEpic(Epic updatedEpic);

    void deleteEpicById(int id);


    //subtasks
    List<Subtask> getAllSubtasks();

    void deleteSubtasks();

    Subtask getSubtaskById(int id);

    List<Subtask> getSubtasksByEpic(int epicId);

    void createSubtask(Subtask subtask);

    void updateSubtask(Subtask updatedSubtask);

    void deleteSubtaskById(int id);

    List<Task> getHistory();

    List<Task> getPrioritizedTasks();

}
