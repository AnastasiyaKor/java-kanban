package service;

import model.Epic;
import model.SubTask;
import model.Task;
import server.HTTPTaskManager;
import server.KVServer;
import server.KVTaskClient;

import java.io.IOException;
import java.net.URISyntaxException;

public class Main {
    public static void main(String[] args) throws IOException {
        new KVServer().start();
        KVTaskClient kvTaskClient = new KVTaskClient("http://localhost:8078/");
        HTTPTaskManager fb = new HTTPTaskManager("http://localhost:8078/", false);
        Task task = new Task("task", "deskr", 30, "2022-10-15T21:00:00");
        fb.createTasks(task);
        fb.getTaskById(0);
        Task task1 = new Task("task1", "deskr1", 30, "2022-10-15T22:00:00");
        fb.createTasks(task1);
        Task task2 = new Task("task2", "deskr1", 30, "2022-10-16T22:00:00");
        fb.createTasks(task2);
        Epic epic = new Epic("epic", "deskrEpic");
        fb.createEpics(epic);
        SubTask subTask = new SubTask("subtask", "deskr", 30,
                "2022-08-15T10:00:00", 3);
        fb.createSubTasks(subTask);
        fb.getTaskById(1);
        fb.getTaskById(1);

        HTTPTaskManager taskManager = new HTTPTaskManager("http://localhost:8078/", true);
        System.out.println("задачи:");
        System.out.println(taskManager.getAllTasks());
        System.out.println("эпики:");
        System.out.println(taskManager.getAllEpics());
        System.out.println("подзадачи:");
        System.out.println(taskManager.getAllSubTasks());
        System.out.println("история:");
        System.out.println(taskManager.getHistory());
        System.out.println("прриоритет:");
        System.out.println(taskManager.getPrioritizedTasks());

    }
}
