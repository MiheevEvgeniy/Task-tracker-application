import managers.FileBackedTasksManager;
import tasks.Task;

public class Main {

    public static void main(String[] args) {
        FileBackedTasksManager tm = new FileBackedTasksManager("save.csv");
        for (Task task : tm.getHistory()) {
            System.out.println(task);
        }
    }
}
