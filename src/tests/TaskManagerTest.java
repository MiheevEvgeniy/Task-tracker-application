package tests;

import managers.TaskManager;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public abstract class TaskManagerTest<T extends TaskManager> {
    private final T manager;

    public TaskManagerTest(T manager) {
        this.manager = manager;
    }

    @Test
    public void managerTesting() {

        assertDoesNotThrow(() -> manager.deleteEpicById(1000));
        assertDoesNotThrow(() -> manager.deleteTaskById(1000));
        assertDoesNotThrow(() -> manager.deleteTaskById(1000));
        assertDoesNotThrow(() -> manager.deleteSubtaskById(1000));
        assertDoesNotThrow(() -> manager.deleteSubtaskById(1000));

        assertDoesNotThrow(() -> manager.getTaskById(1000));
        assertDoesNotThrow(() -> manager.getEpicById(1000));
        assertDoesNotThrow(() -> manager.getSubtaskById(1000));

        assertDoesNotThrow(() -> manager.updateTask(new Task("Задача", "Описание")));
        assertDoesNotThrow(() -> manager.updateEpic(new Epic("Задача", "Описание")));
        assertDoesNotThrow(() -> manager.updateSubtask(new Subtask("Задача", "Описание", 1000)));

        Task task1 = new Task("Задача1", "Описание1", LocalDateTime.of(2023, 2, 1, 1, 0, 0), 300);
        Task task2 = new Task("Задача2", "Описание2", LocalDateTime.of(2023, 2, 1, 6, 0, 0), 300);
        manager.createTask(task1);
        manager.createTask(task2);

        Task savedTask1 = manager.getTaskById(1);
        Task savedTask2 = manager.getTaskById(2);

        assertNotNull(savedTask1, "Задача 1 не найдена");
        assertNotNull(savedTask2, "Задача 2 не найдена");
        assertEquals(task1, savedTask1, "Задачи №1 не совпадают");
        assertEquals(task2, savedTask2, "Задачи №2 не совпадают");

        Epic epic1 = new Epic("Эпик1", "Описание Эпика");
        Epic epic2 = new Epic("Эпик2", "Описание Эпика");
        manager.createEpic(epic1);
        manager.createEpic(epic2);

        Epic savedEpic1 = manager.getEpicById(3);
        Epic savedEpic2 = manager.getEpicById(4);

        assertNotNull(savedTask1, "Эпик 1 не найден");
        assertNotNull(savedTask2, "Эпик 2 не найден");
        assertEquals(epic1, savedEpic1, "Эпики №1 не совпадают");
        assertEquals(epic2, savedEpic2, "Эпики №2 не совпадают");

        Subtask subtask1 = new Subtask("Подзадача1", "Описание подзадачи1", LocalDateTime.of(2023, 3, 1, 1, 0, 0), 300, 3);
        Subtask subtask2 = new Subtask("Подзадача2", "Описание подзадачи2", LocalDateTime.of(2023, 3, 1, 1, 0, 0), 301, 3);
        Subtask subtask3 = new Subtask("Подзадача3", "Описание подзадачи3", 3);
        Subtask subtask4 = new Subtask("Подзадача4", "Описание подзадачи4", LocalDateTime.of(2023, 2, 1, 5, 0, 0), 300, 4);
        Subtask subtask5 = new Subtask("Подзадача5", "Описание подзадачи5", LocalDateTime.of(2023, 2, 1, 1, 0, 1), 300, 4);

        manager.createSubtask(subtask1);
        manager.createSubtask(subtask2);
        manager.createSubtask(subtask3);
        manager.createSubtask(subtask4);
        manager.createSubtask(subtask5);

        Subtask savedSubtask1 = manager.getSubtaskById(5);
        Subtask savedSubtask3 = manager.getSubtaskById(6);

        assertEquals(2, manager.getAllSubtasks().size());

        assertFalse(manager.getAllSubtasks().contains(subtask2), "Подзадача 2 пересекается с другой и не удалена");
        assertFalse(manager.getAllSubtasks().contains(subtask4), "Подзадача 4 пересекается с другой и не удалена");
        assertFalse(manager.getAllSubtasks().contains(subtask5), "Подзадача 5 пересекается с другой и не удалена");

        assertNotNull(savedSubtask1, "Подзадача 1 не найдена");
        assertNotNull(savedSubtask3, "Подзадача 3 не найдена");

        assertEquals(savedEpic1, manager.getEpicById(savedSubtask1.getEpicId()), "Эпик Подзадачи №1 неверный");
        assertEquals(savedEpic1, manager.getEpicById(savedSubtask3.getEpicId()), "Эпик Подзадачи №3 неверный");

        assertEquals(subtask1, savedSubtask1, "Подзадачи №1 не совпадают");
        assertEquals(subtask3, savedSubtask3, "Подзадачи №3 не совпадают");

        assertEquals(task1, manager.getTaskById(1), "Полученная задача не совпадает с нужной");
        assertEquals(epic1, manager.getEpicById(3), "Полученный эпик не совпадает с нужным");
        assertEquals(subtask1, manager.getSubtaskById(5), "Полученная подзадача не совпадает с нужной");

        for (Task task : manager.getPrioritizedTasks()) {
            System.out.println(task);
        }

        List<Epic> originalEpics = List.of(epic1, epic2);
        List<Task> originalTasks = List.of(task1, task2);
        List<Subtask> originalSubtasks = List.of(subtask1, subtask3);

        List<Epic> allEpics = manager.getAllEpics();
        List<Task> allTasks = manager.getAllTasks();
        List<Subtask> allSubtasks = manager.getAllSubtasks();

        assertArrayEquals(originalEpics.toArray(), allEpics.toArray(), "Список эпиков содержить неверные или неполные значения");
        assertArrayEquals(originalTasks.toArray(), allTasks.toArray(), "Список задач содержить неверные или неполные значения");
        assertArrayEquals(originalSubtasks.toArray(), allSubtasks.toArray(), "Список подзадач содержить неверные или неполные значения");

        Task taskUpdated = new Task("Задача1 Обновленная", "Описание1", 1);
        Epic epicUpdated = new Epic("Эпик1 Обновленный", "Описание Эпика", 3);
        Subtask subtaskUpdated = new Subtask("Подзадача1 Обновленный", "Описание подзадачи1", 5, 3);

        manager.updateTask(taskUpdated);
        manager.updateEpic(epicUpdated);
        manager.updateSubtask(subtaskUpdated);

        savedTask1 = manager.getTaskById(1);
        savedEpic1 = manager.getEpicById(3);
        savedSubtask1 = manager.getSubtaskById(5);

        assertEquals(subtask1, savedSubtask1, "Обновленная подзадача 1 совпадает с оригиналом");
        assertEquals(task1, savedTask1, "Обновленная задача 1 совпадает с оригиналом");
        assertEquals(epic1, savedEpic1, "Обновленный эпик 1 совпадает с оригиналом");

        manager.deleteTaskById(1);
        manager.deleteEpicById(4);
        manager.deleteSubtaskById(6);

        assertNotEquals(task1, manager.getTaskById(1), "Задача не удалилась");
        assertNotEquals(epic1, manager.getEpicById(4), "Эпик не удалился");
        assertNotEquals(subtask1, manager.getSubtaskById(6), "Подзадача не удалилась");

        manager.deleteTasks();
        manager.deleteSubtasks();
        manager.deleteEpics();

        List<Task> emptyTasks = Collections.emptyList();
        List<Task> emptyEpics = Collections.emptyList();
        List<Task> emptySubtasks = Collections.emptyList();

        assertArrayEquals(emptyTasks.toArray(), manager.getAllTasks().toArray(), "Задача не удалилась");
        assertArrayEquals(emptySubtasks.toArray(), manager.getAllSubtasks().toArray(), "Подзадача не удалилась");
        assertArrayEquals(emptyEpics.toArray(), manager.getAllEpics().toArray(), "Эпик не удалился");
    }

    @Test
    public void shouldReturnTasksSortedByStartTime() {

    }
}
