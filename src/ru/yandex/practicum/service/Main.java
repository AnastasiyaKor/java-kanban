package service;

import model.*;

public class Main {

    public static void main(String[] args){
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
        System.out.println("вызываем задачу:");
        System.out.println(manager.getAllTasks());
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
        System.out.println("вызываем эпик:");
        System.out.println(manager.getAllEpics());
        System.out.println("вызываем подзадачи:");
        System.out.println(manager.getAllSubTasks());
        System.out.println("вызываем сортировку задач");
        System.out.println(manager.getPrioritizedTasks());
        System.out.println("задачи приоритета");
        System.out.println(manager.getPrioritizedTasks());
       manager.removeSubTaskById(4);
        System.out.println("вызываем подзадачи после удаления:");
        System.out.println(manager.getAllSubTasks());
        System.out.println("задачи приоритета после удаления");
        System.out.println(manager.getPrioritizedTasks());
        System.out.println("эпики");
        manager.getAllEpics();
    }
}
