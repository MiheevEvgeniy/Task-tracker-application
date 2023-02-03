package tests;

import managers.InMemoryHistoryManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Task;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

class InMemoryHistoryManagerTest {
    private InMemoryHistoryManager history;

    @BeforeEach
    public void createNewHistory() {
        history = new InMemoryHistoryManager();
    }

    @Test
    public void shouldNotHaveDuplicates() {
        Task task1 = new Task("Задача", "Описание", 1);
        history.add(task1);
        history.add(task1);
        assertEquals(1, history.getHistory().size(), "Количество элементов неверное");
    }

    @Test
    public void shouldNotThrowExceptionIfRemoveNonExistentId() {
        assertDoesNotThrow(() -> history.remove(1000), "Удаление несуществующего элемента вызывает ошибку");
    }

    @Test
    public void shouldMoveElementsCorrectlyWhenDeleteFirst() {
        Task task1 = new Task("Задача1", "Описание1", 1);
        Task task2 = new Task("Задача2", "Описание2", 2);
        Task task3 = new Task("Задача3", "Описание3", 3);
        Task task4 = new Task("Задача4", "Описание4", 4);
        Task task5 = new Task("Задача5", "Описание5", 5);
        history.add(task1);
        history.add(task2);
        history.add(task3);
        history.add(task4);
        history.add(task5);

        history.remove(1);

        List<Task> savedHistory = history.getHistory();

        assertEquals(task2, savedHistory.get(0), "Первый элемент неверный");
        assertEquals(4, savedHistory.size(), "Количество элементов неверное");
        assertEquals(task5, savedHistory.get(savedHistory.size() - 1), "Последний элемент неверный");
    }

    @Test
    public void shouldMoveElementsCorrectlyWhenDeleteLast() {
        Task task1 = new Task("Задача1", "Описание1", 1);
        Task task2 = new Task("Задача2", "Описание2", 2);
        Task task3 = new Task("Задача3", "Описание3", 3);
        Task task4 = new Task("Задача4", "Описание4", 4);
        Task task5 = new Task("Задача5", "Описание5", 5);
        history.add(task1);
        history.add(task2);
        history.add(task3);
        history.add(task4);
        history.add(task5);

        history.remove(5);

        List<Task> savedHistory = history.getHistory();

        assertEquals(task1, savedHistory.get(0), "Первый элемент неверный");
        assertEquals(4, savedHistory.size(), "Количество элементов неверное");
        assertEquals(task4, savedHistory.get(savedHistory.size() - 1), "Последний элемент неверный");
    }

    @Test
    public void shouldMoveElementsCorrectlyWhenDeleteMiddle() {
        Task task1 = new Task("Задача1", "Описание1", 1);
        Task task2 = new Task("Задача2", "Описание2", 2);
        Task task3 = new Task("Задача3", "Описание3", 3);
        Task task4 = new Task("Задача4", "Описание4", 4);
        Task task5 = new Task("Задача5", "Описание5", 5);
        history.add(task1);
        history.add(task2);
        history.add(task3);
        history.add(task4);
        history.add(task5);

        history.remove(3);

        List<Task> savedHistory = history.getHistory();

        assertEquals(task4, savedHistory.get(2), "Элемент заместился неверно");
        assertEquals(4, savedHistory.size(), "Количество элементов неверное");
        assertEquals(task5, savedHistory.get(savedHistory.size() - 1), "Последний элемент неверный");
    }
}