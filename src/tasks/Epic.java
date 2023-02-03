package tasks;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Objects;

public class Epic extends Task {

    private ArrayList<Integer> subtasks = new ArrayList<>();

    private LocalDateTime endTime;

    public Epic(String name, String description) {
        super(name, description);
        this.endTime = LocalDateTime.MIN;
        setStartTime(LocalDateTime.MAX);
        setDuration(0);
        setType(Types.EPIC);
    }

    public Epic(String name, String description, int id, Status status) {
        super(name, description, id, status);
        this.endTime = LocalDateTime.MIN;
        setStartTime(LocalDateTime.MAX);
        setDuration(0);
        setType(Types.EPIC);
    }

    public Epic(String name, String description, int id) {
        super(name, description, id);
        this.endTime = LocalDateTime.MIN;
        setStartTime(LocalDateTime.MAX);
        setDuration(0);
        setType(Types.EPIC);
    }

    public Epic(String name, String description, Status status) {
        super(name, description, status);
        this.endTime = LocalDateTime.MIN;
        setStartTime(LocalDateTime.MAX);
        setDuration(0);
        setType(Types.EPIC);
    }

    public void addDuration(int newDuration) {
        setDuration(getDuration() + newDuration);
    }

    @Override
    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        return "Epic{" +
                "name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", id=" + getId() +
                ", status='" + getStatus() + '\'' +
                ", startTime='" + getStartTime() + '\'' +
                '}';
    }

    public ArrayList<Integer> getSubtasks() {
        return subtasks;
    }

    public void setSubtasks(ArrayList<Integer> subtasks) {
        this.subtasks = subtasks;
    }

    public void addSubtask(int value) {
        this.subtasks.add(value);
    }

    public void clear() {
        subtasks.clear();
    }

    public void removeSubtask(Integer id) {
        subtasks.remove(id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Epic epic = (Epic) o;
        return getId() == epic.getId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
