package server;

import adapter.LocalDateTimeAdapter;
import adapter.SubTaskAdapter;
import adapter.TaskAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import model.Epic;
import model.SubTask;
import model.Task;
import service.FileBackedTasksManager;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class HTTPTaskManager extends FileBackedTasksManager {

    private final String uri;
    private final KVTaskClient kvTaskClient;
    private final Gson gson1 = new Gson();
    private final Gson gson = new GsonBuilder().registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .registerTypeAdapter(Task.class, new TaskAdapter())
            .registerTypeAdapter(SubTask.class, new SubTaskAdapter())
            .create();

    public HTTPTaskManager(String uri, boolean b) {
        kvTaskClient = new KVTaskClient(uri);
        if (b) {
            load();
        }
        this.uri = uri;
    }

    @Override
    public void save() {
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
        if (tasksJson != null) {
            for (Task t : tasksJson) {
                int idTask = Integer.parseInt(String.valueOf(t.getId()));
                if (idTask > id) {
                    id = idTask;
                }
                tasks.put(idTask, t);
                priorityTasks.put(t.getStartTime(), t);
            }
        }
        if (epicJson != null) {
            for (Epic e : epicJson) {
                int idTask = Integer.parseInt(String.valueOf(e.getId()));
                if (idTask > id) {
                    id = idTask;
                }
                epics.put(idTask, e);
            }
        }
        if (subtaskJson != null) {
            for (SubTask s : subtaskJson) {
                int idTask = Integer.parseInt(String.valueOf(s.getId()));
                if (idTask > id) {
                    id = idTask;
                }
                subTasks.put(idTask, s);
                priorityTasks.put(s.getStartTime(), s);
            }
        }
        if (historyJson != null) {
            for (Integer i : historyJson) {
                if (tasks.containsKey(i)) {
                    historyManager.add(tasks.get(i));
                } else if (epics.containsKey(i)) {
                    historyManager.add(epics.get(i));
                } else {
                    historyManager.add(subTasks.get(i));
                }
            }
        }
    }
}
