
import managers.Managers;
import managers.TaskManager;
import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;

public class Main {

    public static void main(String[] args) {
        TaskManager tm = Managers.getDefault();
        Task task1 = new Task("Переезд", "Описание");
        Task task2 = new Task("Купить молоко", "Описание");
        tm.createTask(task1);
        tm.createTask(task2);

        Epic epic1 = new Epic("Эпик1", "Описание");
        tm.createEpic(epic1);

        Subtask subtask1 = new Subtask("ПЭ1.1", "Описание", epic1.getId());
        tm.createSubtask(subtask1);
        Subtask subtask2 = new Subtask("ПЭ1.2", "Описание", epic1.getId());
        tm.createSubtask(subtask2);
        Subtask subtask3 = new Subtask("ПЭ1.3", "Описание", epic1.getId());
        tm.createSubtask(subtask3);

        Epic epic2 = new Epic("Эпик2", "Описание");
        tm.createEpic(epic2);

        tm.getTaskById(1);
        tm.getTaskById(2);
        tm.getTaskById(2);
        tm.getTaskById(1);
        tm.getEpicById(3);
        tm.getSubtaskById(4);
        tm.getSubtaskById(5);
        tm.getSubtaskById(4);
        tm.getSubtaskById(5);
        tm.getSubtaskById(6);
        tm.getSubtaskById(5);
        tm.getEpicById(7);
        tm.getEpicById(7);
        tm.getEpicById(3);
        tm.getTaskById(1);

        for (Task task: tm.getHistory()) {
            System.out.println(task);
        }
        System.out.println("---------------------");
        tm.deleteEpicById(3);
        for (Task task: tm.getHistory()) {
            System.out.println(task);
        }

    }
}
