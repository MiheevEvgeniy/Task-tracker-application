package ru.java.project.schedule.tasks;

import java.time.LocalDateTime;
import java.util.Objects;

public class Subtask extends Task {

    private int epicId;

    public Subtask(String name, String description, LocalDateTime startTime, int duration, int epicId) {
        super(name, description, startTime, duration);
        setType(Types.SUBTASK);
        this.epicId = epicId;
    }

    public Subtask(String name, String description, int id, Status status, LocalDateTime startTime, int duration, int epicId) {
        super(name, description, id, status, startTime, duration);
        setType(Types.SUBTASK);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }

    @Override
    public String toString() {
        return "Subtask{" +
                "name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", id=" + getId() + '\'' +
                ", status='" + getStatus() + '\'' +
                ", startTime='" + getStartTime() + '\'' +
                ", endTime='" + getEndTime() + '\'' +
                ", epicId='" + getEpicId() + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Subtask subtask = (Subtask) o;
        return Objects.equals(epicId, subtask.epicId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), epicId);
    }

}
