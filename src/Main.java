import managers.FileBackedTasksManager;
import tasks.Task;

import java.nio.file.Path;

public class Main {

    public static void main(String[] args) {
        FileBackedTasksManager tm = new FileBackedTasksManager();
        FileBackedTasksManager newTm = tm.getLoadedManager();
        for (Task task :newTm.getHistory()) {
            System.out.println(task.toString());
        }
        System.out.println(newTm.getAllEpics());
        System.out.println(newTm.getAllTasks());
        System.out.println(newTm.getAllSubtasks());
    }
}
