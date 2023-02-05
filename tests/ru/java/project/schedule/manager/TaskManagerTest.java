package ru.java.project.schedule.manager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import ru.java.project.schedule.exceptions.ValidateException;
import ru.java.project.schedule.managers.TaskManager;
import ru.java.project.schedule.tasks.Epic;
import ru.java.project.schedule.tasks.Status;
import ru.java.project.schedule.tasks.Subtask;
import ru.java.project.schedule.tasks.Task;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public abstract class TaskManagerTest<T extends TaskManager> {
    protected T manager;

    protected Task task1;
    protected Task task2;
    protected Epic epic1;
    protected Epic epic2;
    protected Subtask subtask1;
    protected Subtask subtask2;
    protected Subtask subtask3;
    protected Subtask subtask4;
    protected Subtask subtask5;

    public TaskManagerTest(T manager) {
        this.manager = manager;
    }

    @BeforeEach
    protected void initTasks() {
        manager.deleteTasks();
        manager.deleteSubtasks();
        manager.deleteEpics();
        task1 = new Task("Test Task", "Description", 1, Status.NEW, LocalDateTime.now(), 400);
        manager.createTask(task1);

        task2 = new Task("Test Task", "Description", 2, Status.NEW, task1.getEndTime(), 400);
        manager.createTask(task2);

        epic1 = new Epic("Test Epic", "Description", 3, Status.NEW);
        manager.createEpic(epic1);

        epic2 = new Epic("Test Epic", "Description", 4, Status.NEW);
        manager.createEpic(epic2);

        subtask1 = new Subtask("Test Subtask", "Description", 5, Status.NEW, task2.getEndTime(), 400, 3);
        manager.createSubtask(subtask1);

        subtask2 = new Subtask("Test Subtask", "Description", 6, Status.NEW, subtask1.getEndTime(), 400, 3);
        manager.createSubtask(subtask2);

        subtask3 = new Subtask("Test Subtask", "Description", 7, Status.NEW, subtask2.getEndTime(), 400, 3);
        manager.createSubtask(subtask3);

        subtask4 = new Subtask("Test Subtask", "Description", 8, Status.NEW, subtask3.getEndTime(), 400, 4);
        manager.createSubtask(subtask4);

        subtask5 = new Subtask("Test Subtask", "Description", 9, Status.NEW, subtask4.getEndTime(), 400, 4);
        manager.createSubtask(subtask5);
    }

    @Test
    public void getTask() {
        final Task savedTask1 = manager.getTaskById(task1.getId());

        assertNotNull(savedTask1, "Задача 1 не найдена");
        assertEquals(task1, savedTask1, "Задачи 1 не совпадают");

        final Task savedTask2 = manager.getTaskById(task2.getId());

        assertNotNull(savedTask2, "Задача 2 не найдена");
        assertEquals(task2, savedTask2, "Задачи 2 не совпадают");
    }

    @Test
    public void getSubtask() {
        final Subtask savedSubtask1 = manager.getSubtaskById(subtask1.getId());

        assertNotNull(savedSubtask1, "Подзадача 1 не найдена");
        assertEquals(subtask1, savedSubtask1, "Подзадачи 1 не совпадают");

        final Subtask savedSubtask2 = manager.getSubtaskById(subtask2.getId());

        assertNotNull(savedSubtask2, "Подзадача 2 не найдена");
        assertEquals(subtask2, savedSubtask2, "Подзадачи 2 не совпадают");

        final Subtask savedSubtask3 = manager.getSubtaskById(subtask3.getId());

        assertNotNull(savedSubtask3, "Подзадача 3 не найдена");
        assertEquals(subtask3, savedSubtask3, "Подзадачи 3 не совпадают");

        final Subtask savedSubtask4 = manager.getSubtaskById(subtask4.getId());

        assertNotNull(savedSubtask4, "Подзадача 4 не найдена");
        assertEquals(subtask4, savedSubtask4, "Подзадачи 4 не совпадают");

        final Subtask savedSubtask5 = manager.getSubtaskById(subtask5.getId());

        assertNotNull(savedSubtask5, "Подзадача 5 не найдена");
        assertEquals(subtask5, savedSubtask5, "Подзадачи 5 не совпадают");
    }

    @Test
    public void getEpic() {
        final Epic savedEpic1 = manager.getEpicById(epic1.getId());

        assertNotNull(savedEpic1, "Эпик 1 не найден");
        assertEquals(epic1, savedEpic1, "Эпики 1 не совпадают");

        final Epic savedEpic2 = manager.getEpicById(epic2.getId());

        assertNotNull(savedEpic2, "Эпик 2 не найден");
        assertEquals(epic2, savedEpic2, "Эпики 2 не совпадают");
    }

    @Test
    public void deleteAnyNonExistentTask() {
        assertDoesNotThrow(() -> manager.deleteEpicById(1000));
        assertDoesNotThrow(() -> manager.deleteTaskById(1000));
        assertDoesNotThrow(() -> manager.deleteSubtaskById(1000));
    }

    @Test
    public void getAnyNonExistentTask() {
        assertDoesNotThrow(() -> manager.getTaskById(1000));
        assertDoesNotThrow(() -> manager.getEpicById(1000));
        assertDoesNotThrow(() -> manager.getSubtaskById(1000));
    }

    @Test
    public void updateAnyNonExistentTask() {
        assertDoesNotThrow(() -> manager.updateTask(new Task("Test Task", "Description", 1000, Status.NEW, LocalDateTime.now(), 400)));
        assertDoesNotThrow(() -> manager.updateEpic(new Epic("Test Task", "Description", 1001, Status.NEW)));
        assertDoesNotThrow(() -> manager.updateSubtask(new Subtask("Test Subtask", "Description", 1003, Status.NEW, task2.getEndTime(), 400, 1001)));
    }

    @Test
    public void getAllTasks() {
        final List<Task> tasks = manager.getAllTasks();

        assertNotNull(tasks, "Задачи не найдены");
        assertEquals(2, tasks.size(), "Количество задач неверное");
        assertEquals(task1, tasks.get(0), "Задачи 1 не совпадают");
        assertEquals(task2, tasks.get(1), "Задачи 2 не совпадают");
    }

    @Test
    public void getAllEpics() {
        final List<Epic> epics = manager.getAllEpics();

        assertNotNull(epics, "Эпики не найдены");
        assertEquals(2, epics.size(), "Количество эпиков неверное");
        assertEquals(epic1, epics.get(0), "Эпики 1 не совпадают");
        assertEquals(epic2, epics.get(1), "Эпики 2 не совпадают");
    }

    @Test
    public void getAllSubtasks() {
        final List<Subtask> subtasks = manager.getAllSubtasks();

        assertNotNull(subtasks, "Подзадачи не найдены");
        assertEquals(5, subtasks.size(), "Количество подзадач неверное");
        assertEquals(subtask1, subtasks.get(0), "Подзадачи 1 не совпадают");
        assertEquals(subtask2, subtasks.get(1), "Подзадачи 2 не совпадают");
        assertEquals(subtask3, subtasks.get(2), "Подзадачи 3 не совпадают");
        assertEquals(subtask4, subtasks.get(3), "Подзадачи 4 не совпадают");
        assertEquals(subtask5, subtasks.get(4), "Подзадачи 5 не совпадают");
    }

    @Test
    public void add() {
        Task wrongTimeTask1 = new Task("Test Task", "Description", 100, Status.NEW, task1.getEndTime(), 400);
        Task wrongTimeTask2 = new Task("Test Task", "Description", 101, Status.NEW, task1.getEndTime().plusMinutes(200), 400);
        Task wrongTimeTask3 = new Task("Test Task", "Description", 102, Status.NEW, subtask5.getEndTime().minusSeconds(1), 400);

        assertThrows(ValidateException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                manager.createTask(wrongTimeTask1);
            }
        });
        assertThrows(ValidateException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                manager.createTask(wrongTimeTask2);
            }
        });
        assertThrows(ValidateException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                manager.createTask(wrongTimeTask3);
            }
        });
        assertFalse(manager.getAllTasks().contains(wrongTimeTask1), "Добавлены данные с пересечением");
        assertFalse(manager.getAllTasks().contains(wrongTimeTask2), "Добавлены данные с пересечением");
        assertFalse(manager.getAllTasks().contains(wrongTimeTask3), "Добавлены данные с пересечением");
    }

    @Test
    public void updateTask() {
        Task newTask = new Task("Updated Test Task", "Updated Description", 1, Status.NEW, task1.getStartTime(), 400);
        manager.updateTask(newTask);

        Task savedTask = manager.getTaskById(1);
        assertNotNull(savedTask, "Обновленная задача не найдена");
        assertEquals(newTask, savedTask, "Задачи не совпадают");
    }

    @Test
    public void updateEpic() {
        Epic newEpic = new Epic("Updated Test Epic", "Updated Description", 3, Status.NEW);
        manager.updateEpic(newEpic);

        Task savedEpic = manager.getEpicById(3);
        assertNotNull(savedEpic, "Обновленный эпик не найден");
        assertEquals(newEpic, savedEpic, "Эпики не совпадают");
    }

    @Test
    public void updateSubtask() {
        Subtask newSubtask = new Subtask("Updated Test Subtask", "Updated Description", 5, Status.NEW, task2.getEndTime(), 400, 3);
        manager.updateSubtask(newSubtask);

        Task savedSubtask = manager.getSubtaskById(5);
        assertNotNull(savedSubtask, "Обновленная подзадача не найдена");
        assertEquals(newSubtask, savedSubtask, "Подзадачи не совпадают");
    }

    @Test
    public void deleteTask() {
        manager.deleteTaskById(task1.getId());
        assertNotEquals(task1, manager.getTaskById(task1.getId()), "Задача не удалилась");
    }

    @Test
    public void deleteEpic() {
        manager.deleteEpicById(epic1.getId());
        assertNotEquals(epic1, manager.getEpicById(epic1.getId()), "Эпик не удалился");
    }

    @Test
    public void deleteSubtask() {
        manager.deleteSubtaskById(subtask1.getId());
        assertNotEquals(subtask1, manager.getSubtaskById(subtask1.getId()), "Подзадача не удалилась");
    }

    @Test
    public void deleteTasks() {
        manager.deleteTasks();

        List<Task> emptyTasks = Collections.emptyList();

        assertArrayEquals(emptyTasks.toArray(), manager.getAllTasks().toArray(), "Задачи не удалились");
    }

    @Test
    public void deleteEpics() {
        manager.deleteEpics();

        List<Task> emptyEpics = Collections.emptyList();

        assertArrayEquals(emptyEpics.toArray(), manager.getAllEpics().toArray(), "Эпики не удалились");
    }

    @Test
    public void deleteSubtasks() {
        manager.deleteSubtasks();

        List<Task> emptySubtasks = Collections.emptyList();

        assertArrayEquals(emptySubtasks.toArray(), manager.getAllSubtasks().toArray(), "Подзадачи не удалились");
    }

    @Test
    public void shouldHaveNewEpicStatusIfCreated() {
        assertEquals(Status.NEW, manager.getEpicById(epic1.getId()).getStatus(), ("Неверный статус эпика 1"));
        assertEquals(Status.NEW, manager.getEpicById(epic1.getId()).getStatus(), ("Неверный статус эпика 2"));
    }

    @Test
    public void shouldHaveDoneEpicStatusIfAllSubtaskAreDone() {
        manager.updateSubtask(new Subtask("Test Subtask", "Description", 8, Status.DONE, subtask3.getEndTime(), 400, 4));
        manager.updateSubtask(new Subtask("Test Subtask", "Description", 9, Status.DONE, subtask4.getEndTime(), 400, 4));

        Status epicStatus = manager.getEpicById(epic2.getId()).getStatus();

        assertEquals(Status.DONE, epicStatus, "Неверный статус эпика");
    }

    @Test
    public void shouldHaveInProgressEpicStatusIfAnySubtaskIsDone() {
        manager.updateSubtask(new Subtask("Test Subtask", "Description", 8, Status.DONE, subtask3.getEndTime(), 400, 4));

        Status epicStatus = manager.getEpicById(epic2.getId()).getStatus();

        assertEquals(Status.IN_PROGRESS, epicStatus, "Неверный статус эпика");
    }

    @Test
    public void shouldHaveInProgressEpicStatusIfAnySubtaskIsInProgress() {
        manager.updateSubtask(new Subtask("Test Subtask", "Description", 8, Status.IN_PROGRESS, subtask3.getEndTime(), 400, 4));

        Status epicStatus = manager.getEpicById(epic2.getId()).getStatus();

        assertEquals(Status.IN_PROGRESS, epicStatus, "Неверный статус эпика");
    }
}
