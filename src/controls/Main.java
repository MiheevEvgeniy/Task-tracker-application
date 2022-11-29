package controls;

import objects.Epic;
import objects.Subtask;
import objects.Task;

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

        System.out.println("Создание эпика1...");
        Epic epic1 = new Epic("Эпик1", "Описание");
        tm.createEpic(epic1);
        System.out.println("Добавление подзадач...");
        Subtask subtask1 = new Subtask("ПЭ1.1", "Описание", epic1);
        Subtask subtask2 = new Subtask("ПЭ1.2", "Описание", epic1);
        tm.createSubtask(subtask1);
        tm.createSubtask(subtask2);
        System.out.println("Вывод подзадач эпика1");
        System.out.println(tm.getSubtasksByEpic(epic1));

        System.out.println("Создание эпика2...");
        Epic epic2 = new Epic("Эпик2", "Описание2");
        tm.createEpic(epic2);
        System.out.println("Добавление подзадач...");
        Subtask subtask3 = new Subtask("ПЭ2", "Описание", epic2);
        tm.createSubtask(subtask3);
        System.out.println("Вывод подзадач эпика2");
        System.out.println(tm.getSubtasksByEpic(epic2));

        System.out.println("Список всех эпиков, подзадач и задач:");
        System.out.println(tm.getAllEpics());
        System.out.println(tm.getAllSubtasks());
        System.out.println(tm.getAllTasks());

        System.out.println("Обновление задачи2");
        Task task3 = new Task("Купить хлеб", "Описание", "IN PROGRESS");
        tm.updateTask(task3, 1);
        System.out.println(tm.getAllTasks());

        System.out.println("Изменение статуса подзадачи и эпиков...");

        Subtask subtask1_new = new Subtask("ПЭ1.1", "Описание", "IN PROGRESS", epic1);
        tm.updateSubtask(subtask1_new, 3);

        Subtask subtask2_new = new Subtask("ПЭ1.2", "Описание", "DONE", epic1);
        tm.updateSubtask(subtask2_new, 4);

        Subtask subtask3_new = new Subtask("ПЭ2", "Описание", "DONE", epic2);
        tm.updateSubtask(subtask3_new, 6);

        System.out.println(tm.getEpicByID(2));
        System.out.println(tm.getSubtasksByEpic(epic1));
        System.out.println(tm.getEpicByID(5));
        System.out.println(tm.getSubtasksByEpic(epic2));

        System.out.println("Удаление эпика...");
        System.out.println(tm.getEpicByID(2));
        System.out.println(tm.getSubtasksByEpic(epic1));
        tm.deleteEpicByID(2);
        System.out.println("Эпик удален...");
        System.out.println(tm.getEpicByID(2));
        System.out.println(tm.getSubtasksByEpic(epic1));


    }
}
