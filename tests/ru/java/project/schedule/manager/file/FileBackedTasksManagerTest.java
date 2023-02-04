package ru.java.project.schedule.manager.file;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.java.project.schedule.manager.TaskManagerTest;
import ru.java.project.schedule.managers.FileBackedTasksManager;
import ru.java.project.schedule.tasks.Epic;
import ru.java.project.schedule.tasks.Subtask;
import ru.java.project.schedule.tasks.Task;

import java.io.File;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FileBackedTasksManagerTest extends TaskManagerTest<FileBackedTasksManager> {

    private File file;

    @BeforeEach
    public void setUp() {
        file = new File("src/ru/java/project/schedule/resources/test_" + System.nanoTime() + ".csv");
        manager = new FileBackedTasksManager(file);
        initTasks();
    }

    @AfterEach
    public void tearDown() {
        assertTrue(file.delete(), "Временный файл сохранения не удален");
    }

    public FileBackedTasksManagerTest() {
        super(new FileBackedTasksManager());
    }

    @Test
    public void loadFromFile() {
        FileBackedTasksManager testManager = manager.getLoadedManager();

        List<Task> emptyHistory = Collections.emptyList();
        assertArrayEquals(emptyHistory.toArray(), manager.getHistory().toArray(), "История не пустая");

        Task savedTask1 = testManager.getTaskById(task1.getId());
        Task savedTask2 = testManager.getTaskById(task2.getId());

        Epic savedEpic1 = testManager.getEpicById(epic1.getId());
        Epic savedEpic2 = testManager.getEpicById(epic2.getId());

        Subtask savedSubtask1 = testManager.getSubtaskById(subtask1.getId());
        Subtask savedSubtask2 = testManager.getSubtaskById(subtask2.getId());
        Subtask savedSubtask3 = testManager.getSubtaskById(subtask3.getId());
        Subtask savedSubtask4 = testManager.getSubtaskById(subtask4.getId());
        Subtask savedSubtask5 = testManager.getSubtaskById(subtask5.getId());

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
        assertEquals(subtask2, savedSubtask2, "Подзадачи №2 не совпадают");
        assertEquals(subtask3, savedSubtask3, "Подзадачи №3 не совпадают");
        assertEquals(subtask4, savedSubtask4, "Подзадачи №4 не совпадают");
        assertEquals(subtask5, savedSubtask5, "Подзадачи №5 не совпадают");

        Task tempTask = new Task("Задача старая", "Описание", LocalDateTime.MIN, 0);
        for (Task task : manager.getPrioritizedTasks()) {
            assertTrue(task.getStartTime().isAfter(tempTask.getStartTime()), "Неверный порядок задач в отсортированном списке");
            tempTask = task;
        }
    }
}