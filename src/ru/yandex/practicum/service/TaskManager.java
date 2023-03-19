package service;

import model.Epic;
import model.SubTask;
import model.Task;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface TaskManager {

    void createTasks(Task task);

    void createEpics(Epic epic);

    void createSubTasks(SubTask subTask) throws IOException;

    HashMap<Integer, Task> getAllTasks();

    HashMap<Integer, Epic> getAllEpics();

    HashMap<Integer, SubTask> getAllSubTasks();

    Map<LocalDateTime, Task> getPrioritizedTasks();

    List<Task> getHistory();

    Task getTaskById(int id);

    Epic getEpicById(int id);

    SubTask getSubTaskById(int id);

    ArrayList<Integer> getSubtasksByEpicId(int epicId);

    void refreshTasks(Task task);

    void refreshEpics(Epic epic);

    void refreshSubTasks(SubTask subTask);

    void clearTasks();

    void clearEpics();

    void clearSubTasks();

    void removeTaskById(int id);

    void removeEpicById(int id);

    void removeSubTaskById(int id);

    void refreshStatusEpic(int id);
}
