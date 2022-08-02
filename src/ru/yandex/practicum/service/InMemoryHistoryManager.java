package service;

import model.*;

import java.util.*;

class InMemoryHistoryManager implements HistoryManager {
    private final Map<Integer, Node> customLinkedList = new HashMap<>();
    private Node<Task> head;
    private Node<Task> tail;
    private int size = 0;

    //добавление задачи в конец списка
    public void linkLast(Task task) {
        if (size == 0) {
            head = new Node<>(null, task, null);
            tail = head;
        } else {
            Node secondTail = tail;
            tail = new Node<>(secondTail, task, null);
            secondTail.next = tail;
        }
        size++;
        customLinkedList.put(task.getId(), tail);
    }

    //обновление истории
    @Override
    public void add(Task task) {
        if (customLinkedList.containsKey(task.getId())) {
            removeNode(customLinkedList.get(task.getId()));
        }
        linkLast(task);
    }

    // просмотр истории
    @Override
    public List<Task> getHistory() {
        return getTask();
    }

    //сбор задач в список
    private List<Task> getTask() {
        List<Task> linkLastList = new ArrayList<>();
        Node<Task> node = head;
        while (node != null) {
            linkLastList.add(node.task);
            node = node.next;
        }
        return linkLastList;
    }

    @Override
    public boolean remove(int id) {
        removeNode(customLinkedList.get(id));
        customLinkedList.remove(id);
        return false;
    }

    //удаление ноды
    public boolean removeNode(Node node) {
        if (node != null) {
            Node nodeNext = node.next;
            Node nodePrev = node.prev;


            if (nodeNext != null) {
                nodeNext.prev = nodePrev;
            } else {
                tail = nodePrev;
            }
            if (nodePrev != null) {
                nodePrev.next = nodeNext;
            } else {
                head = nodeNext;
            }
        }
        size--;
        return true;
    }

    private static class Node<Task> {

        public Node<Task> prev;
        public Task task;
        public Node<Task> next;

        public Node(Node<Task> prev, Task task, Node<Task> next) {
            this.prev = prev;
            this.task = task;
            this.next = next;

        }
    }
}
