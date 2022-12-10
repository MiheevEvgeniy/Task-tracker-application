package managers;

import tasks.Task;

import java.util.LinkedList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    LinkedList<Task> historyList = new LinkedList<>();

    @Override
    public void add(Task task) {
        if (historyList.size() > 9) {
            historyList.removeFirst();
            historyList.add(task);
        } else {
            historyList.add(task);
        }
    }

    @Override
    public List<Task> getHistory() {
        return historyList;
    }
}