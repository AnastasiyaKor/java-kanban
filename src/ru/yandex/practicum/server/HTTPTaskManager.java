package server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import model.Epic;
import model.SubTask;
import model.Task;
import service.*;
import adapter.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class HTTPTaskManager extends FileBackedTasksManager {

    private String uri;
    private static KVTaskClient kvTaskClient;
    boolean b;
    GsonBuilder gsonBuilder = new GsonBuilder();
    Gson gson1 = new Gson();

    public HTTPTaskManager(String uri, boolean b) {
        kvTaskClient = new KVTaskClient(uri);
        if (b) {
            load();
        }
        this.uri = uri;
    }

    @Override
    public void save() {
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter());
        gsonBuilder.registerTypeAdapter(Task.class, new TaskAdapter());
        gsonBuilder.registerTypeAdapter(SubTask.class, new SubTaskAdapter());
        Gson gson = gsonBuilder.create();
        String jsonTasks = gson.toJson(new ArrayList<>(tasks.values()));
        kvTaskClient.put("tasks", jsonTasks);
        String jsonEpics = gson1.toJson(new ArrayList<>(epics.values()));
        kvTaskClient.put("epics", jsonEpics);
        String jsonSubtask = gson.toJson(new ArrayList<>(subTasks.values()));
        kvTaskClient.put("subtasks", jsonSubtask);
        String jsonHistory = gson.toJson(getHistory().stream().map(Task::getId).collect(Collectors.toList()));
        kvTaskClient.put("history", jsonHistory);
    }

    private void load() {
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter());
        gsonBuilder.registerTypeAdapter(Task.class, new TaskAdapter());
        gsonBuilder.registerTypeAdapter(SubTask.class, new SubTaskAdapter());
        Gson gson = gsonBuilder.create();
        ArrayList<Task> tasksJson = gson.fromJson(kvTaskClient.load("tasks"),
                new TypeToken<ArrayList<Task>>() {
                }.getType());
        ArrayList<Epic> epicJson = gson1.fromJson(kvTaskClient.load("epics"),
                new TypeToken<ArrayList<Epic>>() {
                }.getType());
        ArrayList<SubTask> subtaskJson = gson.fromJson(kvTaskClient.load("subtasks"),
                new TypeToken<ArrayList<SubTask>>() {
                }.getType());
        ArrayList<Integer> historyJson = gson.fromJson(kvTaskClient.load("history"),
                new TypeToken<ArrayList<Integer>>() {
                }.getType());
        Task task;
        if (tasksJson != null) {
            for (Task t : tasksJson) {
                int idTask = Integer.parseInt(String.valueOf(t.getId()));
                if (idTask > id) {
                    id = idTask;
                }
                task = new Task(t.getName(), t.getDescription(), String.valueOf(t.getStatus()), idTask,
                        String.valueOf(t.getStartTime()), t.getDuration());
                tasks.put(idTask, task);
                priorityTasks.put(task.getStartTime(), task);
            }
        }
        if (epicJson != null) {
            for (Epic e : epicJson) {
                int idTask = Integer.parseInt(String.valueOf(e.getId()));
                if (idTask > id) {
                    id = idTask;
                }
                task = new Epic(e.getName(), e.getDescription(), e.getStatus(), idTask,
                        String.valueOf(e.getStartTime()), e.getDuration());
                epics.put(idTask, (Epic) task);
            }
        }
        if (subtaskJson != null) {
            for (SubTask s : subtaskJson) {
                int idTask = Integer.parseInt(String.valueOf(s.getId()));
                if (idTask > id) {
                    id = idTask;
                }
                task = new SubTask(s.getName(), s.getDescription(), String.valueOf(s.getStatus()), idTask,
                        String.valueOf(s.getStartTime()), s.getDuration(),
                        s.getEpicId());
                subTasks.put(idTask, (SubTask) task);
                epics.get(s.getEpicId()).getSubTasksId().add(idTask);
                refreshDateTimeEpic(subTasks.get(id).getEpicId());
                priorityTasks.put(task.getStartTime(), task);
            }
        }
        if (historyJson != null) {
            for (Integer i : historyJson) {
                if (tasks.containsKey(i)) {
                    historyManager.add(tasks.get(i));
                } else if (epics.containsKey(i)) {
                    historyManager.add(epics.get(i));
                } else {
                    refreshDateTimeEpic(subTasks.get(id).getEpicId());
                    historyManager.add(subTasks.get(i));
                }
            }
        }
    }
}
