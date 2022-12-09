package controls;

import objects.Epic;
import objects.Subtask;
import objects.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public interface TaskManager {


    //-------------tasks--------------------
    ArrayList<Task> getAllTasks();
    void deleteTasks();
    Task getTaskById(int id);
    void createTask(Task task);
    void updateTask(Task updatedTask);
    void deleteTaskById(int id);

    //---------------epics----------------


    ArrayList<Epic> getAllEpics();

    void deleteEpics();

    Epic getEpicById(int id);

    void createEpic(Epic epic);

    void updateEpic(Epic updatedEpic);

    void deleteEpicById(int id);


    //subtasks
    ArrayList<Subtask> getAllSubtasks();

    void deleteSubtasks();

    Subtask getSubtaskById(int id);

    ArrayList<Subtask> getSubtasksByEpic(int epicId);

    void createSubtask(Subtask subtask);

    void updateSubtask(Subtask updatedSubtask);

    void deleteSubtaskById(int id);

}
