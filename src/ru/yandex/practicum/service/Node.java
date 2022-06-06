package ru.yandex.practicum.service;

class Node<Task> {

    public Node<Task> prev;
    public Task task;
    public Node<Task> next;


    public Node(Node<Task> prev, Task task, Node<Task> next) {
        this.prev = prev;
        this.task = task;
        this.next = null;

    }

}


