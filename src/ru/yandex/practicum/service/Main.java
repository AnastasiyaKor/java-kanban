package service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import model.Epic;
import model.SubTask;
import model.Task;
import server.HTTPTaskManager;
import server.KVServer;
import server.KVTaskClient;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

public class Main {
    public static void main(String[] args) throws IOException, URISyntaxException, InterruptedException {
        new KVServer().start();
        KVTaskClient kvTaskClient = new KVTaskClient(new URL("http://localhost:8078/"));
        HTTPTaskManager fb = new HTTPTaskManager(new URL("http://localhost:8078/"));
        Task task = new Task("task", "deskr", 30, "2022-10-15T21:00:00");
        fb.createTasks(task);
        fb.getTaskById(0);
        Task task1 = new Task("task1", "deskr1", 30, "2022-10-15T22:00:00");
        fb.createTasks(task1);
        Task task2 = new Task("task1", "deskr1", 30, "2022-10-16T22:00:00");
        fb.createTasks(task2);
        Epic epic = new Epic("epic", "deskrEpic");
        fb.createEpics(epic);
        SubTask subTask = new SubTask("subtask", "deskr", 30,
                "2022-08-15T10:00:00", 3);
        fb.createSubTasks(subTask);
        fb.getTaskById(1);
        fb.getTaskById(1);
        System.out.println("___ИСТОРИЯ___");
        System.out.println(fb.getHistory());
        kvTaskClient.load("task");
        fb.getAllTasks();
    }
}
