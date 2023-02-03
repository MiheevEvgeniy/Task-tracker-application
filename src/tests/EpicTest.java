package tests;

import managers.FileBackedTasksManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class EpicTest {
    private FileBackedTasksManager manager;

    @BeforeEach
    public void createManagerToStart() {
        manager = new FileBackedTasksManager();
    }

    @Test
    public void shouldHaveNewStatusIfCreated() {
        Epic epic = new Epic("Эпик", "Описание");
        manager.createEpic(epic);

        final Epic savedEpic = manager.getEpicById(1);

        assertNotNull(savedEpic, "Эпик не найден");
        assertEquals(epic, savedEpic, "Сохраненный и введенный эпики не совпадают");

        Status epicStatus = manager.getEpicById(1).getStatus();

        assertEquals(Status.NEW, epicStatus, ("Неверный статус эпика"));
    }

    @Test
    public void shouldHaveNewStatusIfSubtasksAreNew() {
        Epic epic = new Epic("Эпик", "Описание");
        manager.createEpic(epic);

        final Epic savedEpic = manager.getEpicById(1);

        assertNotNull(savedEpic, "Эпик не найден");
        assertEquals(epic, savedEpic, "Сохраненный и введенный эпики не совпадают");

        Subtask subtask1 = new Subtask("Подзадача", "Описание подзадачи", 1);
        Subtask subtask2 = new Subtask("Подзадача", "Описание подзадачи", 1);

        manager.createSubtask(subtask1);
        manager.createSubtask(subtask2);

        final Subtask savedSubtask1 = manager.getSubtaskById(2);
        final Subtask savedSubtask2 = manager.getSubtaskById(3);

        assertNotNull(savedSubtask1, "Подзадача 1 не найдена");
        assertNotNull(savedSubtask2, "Подзадача 2 не найдена");

        assertEquals(subtask1, savedSubtask1, "Сохраненная и введенная подзадача 1 не совпадают");
        assertEquals(subtask2, savedSubtask2, "Сохраненная и введенная подзадача 2 не совпадают");

        Status subtask1Status = savedSubtask1.getStatus();
        Status subtask2Status = savedSubtask2.getStatus();

        assertEquals(Status.NEW, subtask1Status, "Статус подзадачи 1 неверен");
        assertEquals(Status.NEW, subtask2Status, "Статус подзадачи 2 неверен");

        Status epicStatus = manager.getEpicById(1).getStatus();

        assertEquals(Status.NEW, epicStatus, "Неверный статус эпика");
    }

    @Test
    public void shouldHaveDoneStatusIfAllSubtaskAreDone() {
        Epic epic = new Epic("Эпик", "Описание");
        manager.createEpic(epic);

        final Epic savedEpic = manager.getEpicById(1);

        assertNotNull(savedEpic, "Эпик не найден");
        assertEquals(epic, savedEpic, "Сохраненный и введенный эпики не совпадают");

        Subtask subtask1 = new Subtask("Подзадача", "Описание подзадачи", Status.DONE, 1);
        Subtask subtask2 = new Subtask("Подзадача", "Описание подзадачи", Status.DONE, 1);

        manager.createSubtask(subtask1);
        manager.createSubtask(subtask2);

        final Subtask savedSubtask1 = manager.getSubtaskById(2);
        final Subtask savedSubtask2 = manager.getSubtaskById(3);

        assertNotNull(savedSubtask1, "Подзадача 1 не найдена");
        assertNotNull(savedSubtask2, "Подзадача 2 не найдена");

        assertEquals(subtask1, savedSubtask1, "Сохраненная и введенная подзадача 1 не совпадают");
        assertEquals(subtask2, savedSubtask2, "Сохраненная и введенная подзадача 2 не совпадают");

        Status subtask1Status = savedSubtask1.getStatus();
        Status subtask2Status = savedSubtask2.getStatus();

        assertEquals(Status.DONE, subtask1Status, "Статус подзадачи 1 неверен");
        assertEquals(Status.DONE, subtask2Status, "Статус подзадачи 2 неверен");

        Status epicStatus = manager.getEpicById(1).getStatus();

        assertEquals(Status.DONE, epicStatus, "Неверный статус эпика");
    }

    @Test
    public void shouldHaveInProgressStatusIfAnySubtaskIsDone() {
        Epic epic = new Epic("Эпик", "Описание", Status.DONE);
        manager.createEpic(epic);

        final Epic savedEpic = manager.getEpicById(1);

        assertNotNull(savedEpic, "Эпик не найден");
        assertEquals(epic, savedEpic, "Сохраненный и введенный эпики не совпадают");

        Subtask subtask1 = new Subtask("Подзадача", "Описание подзадачи", LocalDateTime.of(2023, 2, 2, 0, 1, 0), 200, 1);
        Subtask subtask2 = new Subtask("Подзадача", "Описание подзадачи", 3, Status.DONE, LocalDateTime.of(2023, 1, 2, 0, 1, 0), 200, 1);

        manager.createSubtask(subtask1);
        manager.createSubtask(subtask2);

        final Subtask savedSubtask1 = manager.getSubtaskById(2);
        final Subtask savedSubtask2 = manager.getSubtaskById(3);

        assertNotNull(savedSubtask1, "Подзадача 1 не найдена");
        assertNotNull(savedSubtask2, "Подзадача 2 не найдена");

        assertEquals(subtask1, savedSubtask1, "Сохраненная и введенная подзадача 1 не совпадают");
        assertEquals(subtask2, savedSubtask2, "Сохраненная и введенная подзадача 2 не совпадают");

        Status subtask1Status = savedSubtask1.getStatus();
        Status subtask2Status = savedSubtask2.getStatus();

        assertEquals(Status.NEW, subtask1Status, "Статус подзадачи 1 неверен");
        assertEquals(Status.DONE, subtask2Status, "Статус подзадачи 2 неверен");

        Status epicStatus = manager.getEpicById(1).getStatus();

        assertEquals(Status.IN_PROGRESS, epicStatus, "Неверный статус эпика");
    }

    @Test
    public void shouldHaveInProgressStatusIfAnySubtaskIsInProgress() {
        Epic epic = new Epic("Эпик", "Описание", Status.DONE);
        manager.createEpic(epic);

        final Epic savedEpic = manager.getEpicById(1);

        assertNotNull(savedEpic, "Эпик не найден");
        assertEquals(epic, savedEpic, "Сохраненный и введенный эпики не совпадают");

        Subtask subtask1 = new Subtask("Подзадача", "Описание подзадачи", LocalDateTime.of(2023, 2, 2, 0, 1, 0), 200, 1);
        Subtask subtask2 = new Subtask("Подзадача", "Описание подзадачи", 3, Status.IN_PROGRESS, LocalDateTime.of(2023, 1, 2, 0, 1, 0), 200, 1);

        manager.createSubtask(subtask1);
        manager.createSubtask(subtask2);

        final Subtask savedSubtask1 = manager.getSubtaskById(2);
        final Subtask savedSubtask2 = manager.getSubtaskById(3);

        assertNotNull(savedSubtask1, "Подзадача 1 не найдена");
        assertNotNull(savedSubtask2, "Подзадача 2 не найдена");

        assertEquals(subtask1, savedSubtask1, "Сохраненная и введенная подзадача 1 не совпадают");
        assertEquals(subtask2, savedSubtask2, "Сохраненная и введенная подзадача 2 не совпадают");

        Status subtask1Status = savedSubtask1.getStatus();
        Status subtask2Status = savedSubtask2.getStatus();

        assertEquals(Status.NEW, subtask1Status, "Статус подзадачи 1 неверен");
        assertEquals(Status.IN_PROGRESS, subtask2Status, "Статус подзадачи 2 неверен");

        Status epicStatus = manager.getEpicById(1).getStatus();

        assertEquals(Status.IN_PROGRESS, epicStatus, "Неверный статус эпика");
    }

    @Test
    public void shouldCountTaskEndTimeCorrectly() {
        final Task task = new Task("Задача", "Описание", LocalDateTime.of(2023, 2, 2, 0, 0), 300);
        manager.createTask(task);
        Task savedTask = manager.getTaskById(1);

        assertEquals(LocalDateTime.of(2023, 2, 2, 0, 0), savedTask.getStartTime());
        assertEquals(LocalDateTime.of(2023, 2, 2, 5, 0), savedTask.getEndTime());

        final Epic epic = new Epic("Эпик", "Описание");
        manager.createEpic(epic);

        final Subtask subtask1 = new Subtask("Подзадача 1", "Описание", LocalDateTime.of(2023, 5, 2, 0, 0), 300, 2);
        final Subtask subtask2 = new Subtask("Подзадача 2", "Описание", LocalDateTime.of(2023, 1, 2, 0, 0), 1300, 2);
        final Subtask subtask3 = new Subtask("Подзадача 3", "Описание", LocalDateTime.of(2023, 10, 2, 0, 0), 520, 2);
        manager.createSubtask(subtask1);
        manager.createSubtask(subtask2);
        manager.createSubtask(subtask3);

        Task savedSubtask1 = manager.getSubtaskById(3);
        Task savedSubtask2 = manager.getSubtaskById(4);
        Task savedSubtask3 = manager.getSubtaskById(5);

        assertEquals(LocalDateTime.of(2023, 5, 2, 0, 0), savedSubtask1.getStartTime());
        assertEquals(LocalDateTime.of(2023, 5, 2, 5, 0), savedSubtask1.getEndTime());

        assertEquals(LocalDateTime.of(2023, 1, 2, 0, 0), savedSubtask2.getStartTime());
        assertEquals(LocalDateTime.of(2023, 1, 2, 21, 40), savedSubtask2.getEndTime());

        assertEquals(LocalDateTime.of(2023, 10, 2, 0, 0), savedSubtask3.getStartTime());
        assertEquals(LocalDateTime.of(2023, 10, 2, 8, 40), savedSubtask3.getEndTime());

        Task savedEpic = manager.getEpicById(2);

        assertEquals(2120, savedEpic.getDuration());
        assertEquals(LocalDateTime.of(2023, 1, 2, 0, 0), savedEpic.getStartTime());
        assertEquals(LocalDateTime.of(2023, 10, 2, 8, 40), savedEpic.getEndTime());
    }
}