package ru.yandex.practicum.service;

import ru.yandex.practicum.model.*;

import java.util.*;

class InMemoryHistoryManager implements HistoryManager {
    Map<Integer, Node> customLinkedList = new LinkedHashMap<>();
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
        for (Node<Task> node : customLinkedList.values()) {
            linkLastList.add(node.task);
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
        Node node1 = node;
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
        size--;
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InMemoryHistoryManager that = (InMemoryHistoryManager) o;
        return size == that.size && Objects.equals(customLinkedList, that.customLinkedList) && Objects.equals(head, that.head) && Objects.equals(tail, that.tail);
    }

    @Override
    public int hashCode() {
        return Objects.hash(customLinkedList, head, tail, size);
    }

    @Override
    public String toString() {
        return "InMemoryHistoryManager{" +
                "customLinkedList=" + customLinkedList +
                ", head=" + head +
                ", tail=" + tail +
                ", size=" + size +
                '}';
    }
}
