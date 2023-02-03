package tests;

import managers.FileBackedTasksManager;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FileBackedTasksManagerTest extends TaskManagerTest<FileBackedTasksManager> {
    private final FileBackedTasksManager manager = new FileBackedTasksManager(Path.of("src", "resources", "save.csv").toFile());

    public FileBackedTasksManagerTest() {
        super(new FileBackedTasksManager());
    }

    @Test
    public void shouldSaveAndLoadCorrectly() {
        Task task1 = new Task("Задача1", "Описание1", LocalDateTime.of(2023, 2, 2, 0, 1, 0), 200);
        Task task2 = new Task("Задача2", "Описание2", LocalDateTime.of(2023, 3, 2, 0, 1, 0), 2050);
        manager.createTask(task1);
        manager.createTask(task2);

        Epic epic1 = new Epic("Эпик1", "Описание Эпика");
        Epic epic2 = new Epic("Эпик2", "Описание Эпика");
        manager.createEpic(epic1);
        manager.createEpic(epic2);

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

        FileBackedTasksManager testManager = manager.getLoadedManager();

        List<Task> emptyHistory = Collections.emptyList();
        assertArrayEquals(emptyHistory.toArray(), manager.getHistory().toArray(), "История не пустая");

        Task savedTask1 = testManager.getTaskById(1);
        Task savedTask2 = testManager.getTaskById(2);

        Epic savedEpic1 = testManager.getEpicById(3);
        Epic savedEpic2 = testManager.getEpicById(4);

        Subtask savedSubtask1 = testManager.getSubtaskById(5);
        Subtask savedSubtask3 = testManager.getSubtaskById(6);

        assertNotNull(savedTask1, "Задача 1 не найдена");
        assertNotNull(savedTask2, "Задача 2 не найдена");
        assertNotNull(savedEpic1, "Эпик 1 не найден");
        assertNotNull(savedEpic2, "Эпик 2 не найден");
        assertNotNull(savedSubtask1, "Подзадача 1 не найдена");
        assertNotNull(savedSubtask3, "Подзадача 3 не найдена");
        assertEquals(task1, savedTask1, "Задачи №1 не совпадают");
        assertEquals(task2, savedTask2, "Задачи №2 не совпадают");
        assertEquals(epic1, savedEpic1, "Эпики №1 не совпадают");
        assertEquals(epic2, savedEpic2, "Эпики №2 не совпадают");
        assertEquals(subtask1, savedSubtask1, "Подзадачи №1 не совпадают");
        assertEquals(subtask3, savedSubtask3, "Подзадачи №3 не совпадают");

        Task tempTask = new Task("Задача старая", "Описание", LocalDateTime.MIN, 0);
        for (Task task : manager.getPrioritizedTasks()) {
            assertTrue(task.getStartTime().isAfter(tempTask.getStartTime()), "Неверный порядок задач в отсортированном списке");
            tempTask = task;
        }
    }
}