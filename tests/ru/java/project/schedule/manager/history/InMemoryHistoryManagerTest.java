package ru.java.project.schedule.manager.history;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.java.project.schedule.managers.HistoryManager;
import ru.java.project.schedule.managers.InMemoryHistoryManager;
import ru.java.project.schedule.tasks.Epic;
import ru.java.project.schedule.tasks.Status;
import ru.java.project.schedule.tasks.Subtask;
import ru.java.project.schedule.tasks.Task;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {
    protected Task task;
    protected Epic epic;
    protected Subtask subtask;
    HistoryManager history;

    @BeforeEach
    public void setUp() {
        history = new InMemoryHistoryManager();
        task = new Task("Test Task", "Description", 1, Status.NEW, LocalDateTime.now(), 400);

        epic = new Epic("Test Epic", "Description", 2, Status.NEW);

        subtask = new Subtask("Test Subtask", "Description", 3, Status.NEW, task.getEndTime(), 400, 3);
    }

    @Test
    public void duplicates() {
        history.add(task);
        history.add(task);
        assertEquals(1, history.getHistory().size(), "Количество элементов неверное");
    }

    @Test
    public void nonExistentElement() {
        assertDoesNotThrow(() -> history.remove(1000), "Удаление несуществующего элемента вызывает ошибку");
    }

    @Test
    public void moveElementsCorrectlyWhenDeleteFirst() {
        history.add(task);
        history.add(epic);
        history.add(subtask);

        history.remove(1);

        List<Task> savedHistory = history.getHistory();

        assertEquals(epic, savedHistory.get(0), "Первый элемент неверный");
        assertEquals(2, savedHistory.size(), "Количество элементов неверное");
        assertEquals(subtask, savedHistory.get(savedHistory.size() - 1), "Последний элемент неверный");
    }

    @Test
    public void moveElementsCorrectlyWhenDeleteLast() {
        history.add(task);
        history.add(epic);
        history.add(subtask);

        history.remove(3);

        List<Task> savedHistory = history.getHistory();

        assertEquals(task, savedHistory.get(0), "Первый элемент неверный");
        assertEquals(2, savedHistory.size(), "Количество элементов неверное");
        assertEquals(epic, savedHistory.get(savedHistory.size() - 1), "Последний элемент неверный");
    }

    @Test
    public void moveElementsCorrectlyWhenDeleteMiddle() {
        history.add(task);
        history.add(epic);
        history.add(subtask);

        history.remove(2);

        List<Task> savedHistory = history.getHistory();

        assertEquals(subtask, savedHistory.get(1), "Элемент заместился неверно");
        assertEquals(2, savedHistory.size(), "Количество элементов неверное");
        assertEquals(subtask, savedHistory.get(savedHistory.size() - 1), "Последний элемент неверный");
    }

    @Test
    public void getHistory() {
        List<Task> savedHistory = history.getHistory();
        assertNotNull(savedHistory, "История пустая");
        assertTrue(savedHistory.isEmpty(), "История пустая");
    }

    @Test
    public void add() {
        history.add(task);
        history.add(epic);
        history.add(subtask);

        List<Task> savedHistory = history.getHistory();

        assertEquals(3, savedHistory.size(), "Количество элементов неверное");
    }

    @Test
    void addDifferentTask() {
        history.add(task);
        history.add(epic);
        history.add(subtask);

        List<Task> savedHistory = history.getHistory();

        assertNotNull(savedHistory, "История не пустая");
        assertEquals(3, savedHistory.size(), "История не пустая");
        assertEquals(task, savedHistory.get(0), "Задачи в порядке добавления");
        assertEquals(epic, savedHistory.get(1), "Задачи в порядке добавления");
        assertEquals(subtask, savedHistory.get(2), "Задачи в порядке добавления");
    }
}