package managers;

import tasks.Task;

import java.util.LinkedList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    LinkedList<Task> history = new LinkedList<>();

    @Override
    public void add(Task task) {
        if (task == null) {
            return;
        }
        if (history.size()+1 > 10) {
            history.removeFirst();
        }
        history.add(task);

    }

    @Override
    public List<Task> getHistory() {
        return history;
    }
}
