package service;

import model.*;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        TaskManager manager = Managers.getDefault();
        Task task = new Task("task1", "description1", 30, 2022,
                05, 14, 20, 00);
        Task task2 = new Task("task2", "description2", 30, 2022,
                05, 14, 20, 30);
        Task task3 = new Task("task3", "description3", 30, 2022,
                05, 10, 21, 30);
        manager.createTasks(task);
        manager.createTasks(task2);
        manager.createTasks(task3);
        //создаем эпик с 3 подзадачами
        Epic epic1 = new Epic("epic1", "описание эпик 1");
        manager.createEpics(epic1);
        SubTask subTask1 = new SubTask("подзадача1", "описание подзадачи1", 40, 2022,
                04, 13, 12, 00, epic1.getId());
        manager.createSubTasks(subTask1);
        SubTask subTask2 = new SubTask("подзадача2", "описание подзадачи", 30, 2022,
                07, 12, 15, 00, epic1.getId());
        manager.createSubTasks(subTask2);
        SubTask subTask3 = new SubTask("подзадача3", "описание подзадачи3", 30, 2022,
                07, 12, 11, 00, epic1.getId());
        manager.createSubTasks(subTask3);
        manager.getTaskById(2);
        manager.getTaskById(1);
        manager.getTaskById(0);
        manager.getEpicById(3);
        manager.getSubTaskById(6);
        manager.getSubTaskById(5);
        manager.getSubTaskById(4);
        System.out.println(manager.getHistory());
    }
}
