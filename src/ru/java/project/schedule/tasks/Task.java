package ru.java.project.schedule.tasks;

import java.time.LocalDateTime;
import java.util.Objects;

public class Task {

    private String name;
    private String description;
    private int id;
    private Status status;

    private Types type;

    private int duration;

    private LocalDateTime startTime;

    public Task(String name, String description) {
        this.name = name;
        this.description = description;
        setStartTime(LocalDateTime.MAX);
        setDuration(0);
        this.type = Types.TASK;
    }

    public Task(String name, String description, Status status) {
        this.name = name;
        this.description = description;
        this.status = status;
        setStartTime(LocalDateTime.MAX);
        setDuration(0);
        this.type = Types.TASK;
    }

    public Task(String name, String description, int id) {
        this.name = name;
        this.description = description;
        setStartTime(LocalDateTime.MAX);
        setDuration(0);
        this.type = Types.TASK;
        this.id = id;
    }

    public Task(String name, String description, int id, Status status) {
        this.name = name;
        this.description = description;
        this.id = id;
        this.status = status;
        setStartTime(LocalDateTime.MAX);
        setDuration(0);
        this.type = Types.TASK;
    }

    public Task(String name, String description, LocalDateTime startTime, int duration) {
        this.name = name;
        this.description = description;
        this.duration = duration;
        this.type = Types.TASK;
        this.startTime = startTime;
    }

    public Task(String name, String description, int id, Status status, LocalDateTime startTime, int duration) {
        this.name = name;
        this.description = description;
        this.duration = duration;
        this.type = Types.TASK;
        this.startTime = startTime;
        this.id = id;
        this.status = status;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public int getDuration() {
        return duration;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public Types getType() {
        return type;
    }

    public LocalDateTime getEndTime() {
        if (startTime.equals(LocalDateTime.MAX)) {
            return startTime;
        }
        return startTime.plusMinutes(duration);
    }

    public void setType(Types type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Task{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                ", status='" + status + '\'' +
                ", startTime='" + startTime + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
