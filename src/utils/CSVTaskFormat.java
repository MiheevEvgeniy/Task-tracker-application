package utils;

import managers.HistoryManager;
import tasks.*;

import java.util.ArrayList;
import java.util.List;

public class CSVTaskFormat {

    public static String getHeader() {
        return "id,type,name,status,description,epic";
    }

    public static String historyToString(HistoryManager manager) {
        final List<Task> history = manager.getHistory();
        if (history.isEmpty()) {
            return "";
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(history.get(0).getId());
        for (int i = 1; i < history.size(); i++) {
            Task task = history.get(i);
            stringBuilder.append(",");
            stringBuilder.append(task.getId());
        }
        return stringBuilder.toString();
    }

    public static List<Integer> historyFromString(String line) {
        List<Integer> history = new ArrayList<>();
        for (String id : line.split(",")) {
            history.add(Integer.parseInt(id));
        }
        return history;
    }

    public static String toString(Task task) {
        if (task.getType() == Types.SUBTASK) {
            Subtask subtask = (Subtask) task;
            return subtask.getId() + "," + task.getType() + "," + subtask.getName() + ","
                    + subtask.getStatus() + "," + subtask.getDescription() + "," + subtask.getEpicId() + ",\n";
        }
        return task.getId() + "," + task.getType() + "," + task.getName() + ","
                + task.getStatus() + "," + task.getDescription() + ",\n";
    }

    public static Task fromString(String line) {
        String[] values = line.split(",");
        if (Types.valueOf(values[1]).equals(Types.TASK)) {
            return new Task(values[2], values[4], Integer.parseInt(values[0]), Status.valueOf(values[3]));
        } else if (Types.valueOf(values[1]).equals(Types.SUBTASK)) {
            return new Subtask(values[2], values[4], Integer.parseInt(values[0]), Status.valueOf(values[3]),
                    Integer.parseInt(values[5]));
        }
        return new Epic(values[2], values[4], Integer.parseInt(values[0]), Status.valueOf(values[3]));
    }
}
