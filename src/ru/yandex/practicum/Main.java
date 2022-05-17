package ru.yandex.practicum;

import ru.yandex.practicum.model.*;
import ru.yandex.practicum.service.*;

public class Main {

    public static void main(String[] args) {
        TaskManager manager = Managers.getDefault();

        //создаем 2 задачи
        Task task1 = new Task("Полить цветы", "полить все цветы на кухне");
        manager.createTasks(task1);
        Task task2 = new Task("Накормить кота", "не забыть дать жидкий корм");
        manager.createTasks(task2);
        //создаем эпик с 2 подзадачами
        Epic epic1 = new Epic("Сходить в магазин", " ");
        manager.createEpics(epic1);
        SubTask subTask1 = new SubTask("купить молоко", " ", epic1.getId());
        manager.createSubTasks(subTask1);
        SubTask subTask2 = new SubTask("купить кофе", " ", epic1.getId());
        manager.createSubTasks(subTask2);
        //создаем эпик с 1 подзадачей
        Epic epic2 = new Epic("Помыть аквариум", " ");
        manager.createEpics(epic2);
        SubTask subTask3 = new SubTask("убрать лишние растения", " ", epic2.getId());
        manager.createSubTasks(subTask3);
        //печатаем списки задач
        manager.getAllTasks();
        manager.getAllEpics();
        manager.getAllSubTasks();
        //меняем статус у задач
        task1.setStatus(Status.DONE);
        manager.refreshTasks(task1);
        manager.getAllTasks();

        subTask1.setStatus(Status.IN_PROGRESS);
        manager.refreshSubTasks(subTask1);
        manager.getAllSubTasks();
        manager.getAllEpics();

        task2.setStatus(Status.IN_PROGRESS);
        manager.refreshTasks(task2);
        manager.getAllTasks();
        //удаляем задачу
        manager.removeTaskById(1);
        manager.getAllTasks();
        //получаем список задач по идентификатору
        manager.getEpicById(1);
        manager.getSubTaskById(2);
        manager.getTaskById(1);
        //смотрим историю
        System.out.println(manager.getHistory());
    }
}
