package objects;

import java.util.ArrayList;
import java.util.Objects;

public class Epic extends Task {




    private ArrayList<Integer> subtasks = new ArrayList<>();


    public Epic(String name, String description) {
        super(name, description);
    }

    @Override
    public String toString() {
        return "Epic{" +
                "name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", id=" + getId() +
                ", status='" + getStatus() + '\'' +
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Epic epic = (Epic) o;
        return getId() == epic.getId() ;
    }

    @Override
    public int hashCode() {
        return Objects.hash( getId());
    }
}
