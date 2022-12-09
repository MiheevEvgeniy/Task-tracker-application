package controls;

import objects.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager{
    List<Task> historyList = new ArrayList<>();
    Object[] historyObjects = new Object[10];

    private int lastHistoryObject = 0;

    @Override
    public void add(Task task) {
        historyObjects[lastHistoryObject] = task;

        if (lastHistoryObject == 9) {
            lastHistoryObject = 0;
        } else{
            lastHistoryObject++;
        }
    }

    @Override
    public List<Task> getHistory(){
        historyList.clear();
        for (Object obj: historyObjects) {
            Task value = (Task) obj;
            if (obj == null){continue;}
            historyList.add(value);
        }
        return historyList;
    }
}
