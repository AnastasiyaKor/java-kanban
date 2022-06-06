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
        SubTask subTask3 = new SubTask("купить вафли", " ", epic1.getId());
        manager.createSubTasks(subTask3);
        //создаем эпик с 1 подзадачей
        Epic epic2 = new Epic("Помыть аквариум", " ");
        manager.createEpics(epic2);
        //вызываем задачи
        manager.getEpicById(6);//помыть аквариум
        manager.getEpicById(2); //сходить в магазин
        manager.getTaskById(0); //полить цветы
        manager.getEpicById(6);//помыть аквариум
        manager.getTaskById(1); //накормить кота
        manager.getEpicById(6);//помыть аквариум
        //смотрим историю
        System.out.println(manager.getHistory());
        //удалаяем задачу
        manager.removeEpicById(6);
        //проверяем, удалилась ли задача из истории
        System.out.println("после удаления:");
        System.out.println(manager.getHistory());
        //удалаяем эпик
        manager.removeEpicById(2);
        //проверяем, удалилась ли задача с подзадачами из истории
        System.out.println("после удаления эпика:");
        System.out.println(manager.getHistory());

    }
}
