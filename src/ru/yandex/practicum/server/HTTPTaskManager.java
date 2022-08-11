package server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import model.Epic;
import model.SubTask;
import model.Task;
import service.*;
import adapter.*;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class HTTPTaskManager extends FileBackedTasksManager implements TaskManager {

    private URL url;
    private static KVTaskClient kvTaskClient;

    public HTTPTaskManager(URL url) throws IOException, URISyntaxException {
        this.url = url;
        kvTaskClient = new KVTaskClient(new URL("http://localhost:8078/load/tasks"));
    }

    @Override
    public void save() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter());
        gsonBuilder.registerTypeAdapter(Task.class, new TaskAdapter());
        gsonBuilder.registerTypeAdapter(Epic.class, new EpicAdapter());
        gsonBuilder.registerTypeAdapter(SubTask.class, new SubTaskAdapter());
        Gson gson = gsonBuilder.create();
        String count = "";
        for (Task task : tasks.values()) {
            String gsonTask = gson.toJson(toStringTask(task));
            count = gsonTask + count;
            kvTaskClient.put("tasks", count);
        }
        for (Epic task : epics.values()) {
            String gsonEpic = gson.toJson(toStringEpic(task));
            count = gsonEpic + count;
            kvTaskClient.put("tasks", count);
        }
        for (SubTask task : subTasks.values()) {
            String gsonSubTask = gson.toJson(toStringSubtask(task));
            count = gsonSubTask + count;
            kvTaskClient.put("tasks", count);
        }
        String gsonHistory = gson.toJson(toStringHistory(historyManager));
        count = gsonHistory + count;
        kvTaskClient.put("tasks", count);
    }

    public void fromServer(URL url) {
        if (url.getPath().endsWith("/load/tasks")) {
            String gsonTask = kvTaskClient.load("tasks");
            if (gsonTask != null) {
                if (gsonTask.length() > 2) {
                    String result = gsonTask.substring(1, gsonTask.length() - 1);
                    List<String> split1;
                    split1 = List.of(result.split("\"\""));
                    List<String> split1Revers = new ArrayList<>();
                    for (String s : split1) {
                        if (!(s.isBlank())) {
                            split1Revers.add(0, s);
                        }
                    }
                    String[] split;
                    for (String str : split1Revers) {
                        split = str.split(",");
                        if ((split[1].equals("TASK"))) {
                            Task task;
                            task = new Task(split[2], split[4], split[3], Integer.parseInt(split[0]),
                                    String.valueOf(split[5]), Long.parseLong(split[6]));
                            tasks.put(Integer.valueOf(split[0]), task);
                            priorityTasks.put(task.getStartTime(), task);
                        } else if ((split[1].equals("EPIC"))) {
                            Epic epic;
                            epic = new Epic(split[2], split[4], split[3], Integer.parseInt(split[0]),
                                    String.valueOf(split[5]), Long.parseLong(split[6]));
                            epics.put(Integer.parseInt(split[0]), epic);
                            priorityTasks.put(epic.getStartTime(), epic);
                        } else if ((split[1].equals("SUB_TASK"))) {
                            SubTask subTask;
                            subTask = new SubTask(split[2], split[4], split[3], Integer.parseInt(split[0]),
                                    String.valueOf(split[5]), Long.parseLong(split[6]), Integer.parseInt(split[7]));
                            subTasks.put(Integer.valueOf(split[0]), subTask);
                            priorityTasks.put(subTask.getStartTime(), subTask);
                            refreshDateTimeEpic(subTask.getEpicId());
                            getSubtasksByEpicId(subTask.getEpicId()).add(Integer.valueOf(split[0]));//добавила условие
                        } else {
                            for (String i : split) {
                                if (tasks.containsKey(Integer.valueOf(i))) {
                                    historyManager.add(tasks.get(Integer.valueOf(i)));
                                } else if (epics.containsKey(Integer.valueOf(i))) {
                                    historyManager.add(epics.get(Integer.valueOf(i)));
                                } else if (subTasks.containsKey(Integer.valueOf(i))) {
                                    historyManager.add(subTasks.get(Integer.valueOf(i)));
                                }
                            }
                        }
                    }
                }
            }
        }
    }


    public static HTTPTaskManager load(URL url) throws IOException, URISyntaxException {
        HTTPTaskManager httpTaskManager = new HTTPTaskManager(url);
        httpTaskManager.fromServer(url);
        return httpTaskManager;
    }

    public HTTPTaskManager() {
        super();
        save();
    }

    @Override
    public void createTasks(Task task) {
        super.createTasks(task);
        save();
    }

    @Override
    public void createEpics(Epic epic) {
        super.createEpics(epic);
        save();
    }

    @Override
    public void createSubTasks(SubTask subTask) {
        super.createSubTasks(subTask);
        save();
    }

    @Override
    public Task getTaskById(int id) {
        Task getTaskById = super.getTaskById(id);
        save();
        return getTaskById;
    }

    @Override
    public Epic getEpicById(int id) {
        Epic getEpicById = super.getEpicById(id);
        save();
        return getEpicById;
    }

    @Override
    public SubTask getSubTaskById(int id) {
        SubTask getSubTaskById = super.getSubTaskById(id);
        save();
        return getSubTaskById;
    }

    @Override
    public void refreshTasks(Task task) {
        super.refreshTasks(task);
        save();
    }

    @Override
    public void refreshEpics(Epic epic) {
        super.refreshEpics(epic);
        save();
    }

    @Override
    public void refreshSubTasks(SubTask subTask) {
        super.refreshSubTasks(subTask);
        save();
    }

    @Override
    public void refreshStatusEpic(int epicId) {
        super.refreshStatusEpic(epicId);
        save();
    }

    @Override
    public void removeTaskById(int id) {
        super.removeTaskById(id);
        save();
    }

    @Override
    public void removeEpicById(int id) {
        super.removeEpicById(id);
        save();
    }

    @Override
    public void removeSubTaskById(int id) {
        super.removeSubTaskById(id);
        save();
    }
}
