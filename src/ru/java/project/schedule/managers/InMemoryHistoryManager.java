package ru.java.project.schedule.managers;

import ru.java.project.schedule.tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager {
    private Node head;
    private Node tail;
    private final Map<Integer, Node> nodesById = new HashMap<>();

    @Override
    public void add(Task task) {
        if (task == null) {
            return;
        }
        if (nodesById.get(task.getId()) != null) {
            removeNode(task.getId());
        }
        linkLast(task);
        nodesById.put(task.getId(), tail);

    }

    @Override
    public void remove(int id) {
        removeNode(id);
    }


    @Override
    public List<Task> getHistory() {
        ArrayList<Task> history = new ArrayList<>();
        Node node = head;
        while (node != null) {
            history.add(node.currentItem);
            node = node.next;
        }
        return history;
    }

    private void linkLast(Task task) {
        final Node newNode = new Node(tail, task, null);
        if (head == null) {
            head = newNode;
        } else {
            tail.next = newNode;
        }
        tail = newNode;
    }

    private void removeNode(int id) {
        final Node node = nodesById.remove(id);
        if (node == null) {
            return;
        }

        if (node.prev != null) {
            node.prev.next = node.next;
            if (node.next == null) {
                tail = node.prev;
            } else {
                node.next.prev = node.prev;
            }
        } else {
            head = node.next;
            if (head == null) {
                tail = null;
            } else {
                head.prev = null;
            }
        }
    }

    private ArrayList<Node> getTasks() {
        return new ArrayList<>(nodesById.values());
    }

    private static class Node {
        Task currentItem;
        Node next;
        Node prev;

        Node(Node prev, Task item, Node next) {
            this.currentItem = item;
            this.next = next;
            this.prev = prev;
        }

        @Override
        public String toString() {
            return "Node{" +
                    "currentItem=" + currentItem +
                    '}';
        }
    }
}


