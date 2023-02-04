package ru.java.project.schedule.manager.memory;

import ru.java.project.schedule.manager.TaskManagerTest;
import ru.java.project.schedule.managers.InMemoryTaskManager;

class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {
    public InMemoryTaskManagerTest() {
        super(new InMemoryTaskManager());
    }
}