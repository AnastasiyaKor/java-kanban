package service;

import model.Epic;
import model.SubTask;
import model.Task;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface TaskManager {

    //    создание задач, эпиков, подзадач
    void createTasks(Task task);

    void createEpics(Epic epic);

    void createSubTasks(SubTask subTask) throws IOException;

    //    получение списка задач, эпиков, подзадач
    HashMap<Integer, Task> getAllTasks();

    HashMap<Integer, Epic> getAllEpics();

    HashMap<Integer, SubTask> getAllSubTasks();

    //получение списка сортировки
    Map<LocalDateTime, Task> getPrioritizedTasks();

    //   получение истории
    List<Task> getHistory();

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

    void refreshStatusEpic(int id);
}
