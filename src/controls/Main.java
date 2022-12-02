package controls;

import objects.Epic;
import objects.Subtask;
import objects.Task;
import objects.Status;

public class Main {

    public static void main(String[] args) {
        TaskManager tm = new TaskManager();

        System.out.println("Тестирование задач...");
        System.out.println("Создание задач...");
        Task task1 = new Task("Переезд", "Описание");
        Task task2 = new Task("Купить молоко", "Описание");
        tm.createTask(task1);
        tm.createTask(task2);
        System.out.println(tm.getAllTasks());
        System.out.println("----------------------------");


        System.out.println("Создание эпика1...");
        Epic epic1 = new Epic("Эпик1", "Описание");
        tm.createEpic(epic1);
        System.out.println(tm.getAllEpics());
        System.out.println("Добавление подзадач...");
        Subtask subtask1 = new Subtask("ПЭ1.1", "Описание", epic1.getId());
        Subtask subtask2 = new Subtask("ПЭ1.2", "Описание", epic1.getId());
        tm.createSubtask(subtask1);
        tm.createSubtask(subtask2);
        System.out.println("Вывод подзадач эпика1");
        System.out.println(tm.getSubtasksByEpic(epic1.getId()));
        System.out.println("----------------------------");

        System.out.println("Создание эпика2...");
        Epic epic2 = new Epic("Эпик2", "Описание2");
        tm.createEpic(epic2);
        System.out.println(tm.getAllEpics());
        System.out.println("Добавление подзадач...");
        Subtask subtask3 = new Subtask("ПЭ2", "Описание", epic2.getId());
        tm.createSubtask(subtask3);
        System.out.println("Вывод подзадач эпика2");
        System.out.println(tm.getSubtasksByEpic(epic2.getId()));
        System.out.println("----------------------------");

        tm.deleteSubtaskById(5);

        System.out.println("Список всех эпиков, подзадач и задач:");
        System.out.println(tm.getAllEpics());
        System.out.println(tm.getAllSubtasks());
        System.out.println(tm.getAllTasks());
        System.out.println("----------------------------");

        System.out.println("Обновление задачи2");
        Task task3 = new Task("Купить яблоки", "Описание", tm.getAllTasks().get(1).getId(), Status.IN_PROGRESS);
        tm.updateTask(task3);
        System.out.println(tm.getAllTasks());

        System.out.println("----------------------------");

        System.out.println("Изменение статуса подзадачи и эпиков...");

        Subtask subtask1_new = new Subtask("ПЭ1.1", "Описание",4, Status.IN_PROGRESS, epic1.getId());
        tm.updateSubtask(subtask1_new);

        Subtask subtask2_new = new Subtask("ПЭ1.2", "Описание",5, Status.DONE, epic1.getId());
        tm.updateSubtask(subtask2_new);

        Subtask subtask3_new = new Subtask("ПЭ2", "Описание",7, Status.DONE, epic2.getId());
        tm.updateSubtask(subtask3_new);

        System.out.println(tm.getEpicById(3));
        System.out.println(tm.getSubtasksByEpic(epic1.getId()));
        System.out.println(tm.getEpicById(6));
        System.out.println(tm.getSubtasksByEpic(epic2.getId()));



        System.out.println("Удаление эпика...");
        System.out.println(tm.getEpicById(3));
        System.out.println(tm.getSubtasksByEpic(epic1.getId()));
        tm.deleteEpicById(3);
        System.out.println("Эпик удален...");
        System.out.println(tm.getEpicById(3));
        System.out.println(tm.getSubtasksByEpic(epic1.getId()));



    }
}
