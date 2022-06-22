package ru.yandex.practicum.service;

import ru.yandex.practicum.model.Epic;
import ru.yandex.practicum.model.SubTask;
import ru.yandex.practicum.model.Task;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public interface TaskManager {
    //   получение истории
    List<Task> getHistory() throws IOException;

    //    создание задач, эпиков, подзадач
    void createTasks(Task task) throws IOException;

    void createEpics(Epic epic);

    void createSubTasks(SubTask subTask);

    //    получение списка задач, эпиков, подзадач
    HashMap<Integer, Task> getAllTasks();

    HashMap<Integer, Epic> getAllEpics();

    HashMap<Integer, SubTask> getAllSubTasks();

    //    получение задач, эпиков, подзадач по идентификатору
    Task getTaskById(int id);

    Epic getEpicById(int id);

    SubTask getSubTaskById(int id);

    //    обновление задач, эпиков, подзадач
    void refreshTasks(Task task);

    void refreshEpics(Epic epic);

    void refreshSubTasks(SubTask subTask);

    //    удаление задач, эпиков, подзадач
    void clearTasks();

    void clearEpics();

    void clearSubTasks();

    //    удаление задач, эпиков, подзадач по  идентификатору
    void removeTaskById(int id);

    void removeEpicById(int id);

    void removeSubTaskById(int id);
}
