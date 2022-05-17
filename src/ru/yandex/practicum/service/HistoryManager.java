package ru.yandex.practicum.service;

import ru.yandex.practicum.model.Task;

import java.util.List;

public interface HistoryManager {

    void add(Task task);//помечает задачи, как просмотренные

    List<Task> getHistory();//возвращает задачи в список
}
