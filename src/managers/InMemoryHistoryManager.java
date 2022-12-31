package managers;

import tasks.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {
    Node head;
    Node tail;
    HashMap<Integer, Node> nodesById = new HashMap<>();

    @Override
    public void add(Task task) {
        if (task == null) {
            return;
        }
        linkLast(task);

    }

    @Override
    public void remove(int id) {
        removeNode(nodesById.get(id));
        nodesById.remove(id);
    }


    @Override
    public List<Task> getHistory() {
        ArrayList<Task> history = new ArrayList<>();
        Node node = head;
        while (node != null){
            history.add(node.currentItem);
            node = node.next;
        }
        return history;
    }

    private void linkLast(Task task) {
        if (nodesById.get(task.getId()) != null) {
            removeNode(nodesById.get(task.getId()));
        }

        final Node newNode = new Node(tail, task, null);
        if (tail == null){
            head = newNode;
        } else {
            tail.next = newNode;
        }
        tail = newNode;
        nodesById.put(task.getId(), newNode);

    }

    private void removeNode(Node node){
        Node next = node.next;
        Node prev = node.prev;

        if (prev == null) {
            head = next;
        } else {
            prev.next = next;
            node.prev = null;
        }

        if (next == null) {
            tail = prev;
        } else {
            next.prev = prev;
            node.next = null;
        }

        node.currentItem = null;
    }

    private ArrayList<Node> getTasks(){
        return  new ArrayList<>(nodesById.values());
    }
    private static class Node{
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


