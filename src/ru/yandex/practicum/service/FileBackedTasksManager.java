package ru.yandex.practicum.service;

import ru.yandex.practicum.model.Epic;
import ru.yandex.practicum.model.SubTask;
import ru.yandex.practicum.model.Task;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static ru.yandex.practicum.service.TaskType.*;

public class FileBackedTasksManager extends InMemoryTaskManager implements TaskManager {

    // сохранение в историю
    public void save() {
        try (FileWriter fileWriter = new FileWriter("csvSave.csv")) {
            for (Task task : tasks.values()) {
                fileWriter.write(toStringTask(task));
                fileWriter.write("\n");
            }
            for (Epic task : epics.values()) {
                fileWriter.write(toStringEpic(task));
                fileWriter.write("\n");
            }
            for (SubTask task : subTasks.values()) {
                fileWriter.write(toStringSubtask(task));
                fileWriter.write("\n");
            }
            fileWriter.write(toStringHistory(historyManager));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String toStringTask(Task task) {
        return task.getId() + "," + TASK + "," + task.getName() + "," + task.getStatus() + "," + task.getDescription();
    }

    public String toStringEpic(Epic epic) {
        return epic.getId() + "," + EPIC + "," + epic.getName() + "," + epic.getStatus() + ","
                + epic.getDescription();
    }

    public String toStringSubtask(SubTask subTask) {
        return subTask.getId() + "," + SUB_TASK + "," + subTask.getName() + "," + subTask.getStatus() + ","
                + subTask.getDescription() + "," + subTask.getEpicId();
    }

    //подготовка истории к сохранению в строку
    public String toStringHistory(HistoryManager manager) {
        StringBuilder sb = new StringBuilder();
        for (Task task : manager.getHistory()) {
            sb.append(task.getId());
            sb.append(",");
        }
        String history = String.valueOf(sb);
        return history;
    }

    //метод создания истории из строки
    public List<Integer> fromStringHistory(String value) {
        List<Integer> historyId = new ArrayList<>();
        String line = toStringHistory(historyManager);
        String[] split = line.split(",");
        for (String str : split) {
            historyId.add(Integer.parseInt(str));
        }
        return historyId;
    }

    //метод создания задачи из строки
    public void fromString(String value) {
        try {
            String lines = Files.readString(Path.of("csvSave.csv"));
            String[] splitLines = lines.split(System.lineSeparator());
            Task task;
            for (String line : splitLines) {
                List<String> split = List.of(line.split(","));
                if (split.get(1).equals(String.valueOf(TASK))) {
                    task = new Task(split.get(2), split.get(4), split.get(3));
                    tasks.put(Integer.valueOf(split.get(0)), task);
                } else if (split.get(1).equals(String.valueOf(EPIC))) {
                    task = new Epic(split.get(2), split.get(4), split.get(3));
                    epics.put((Integer.valueOf(split.get(0))), (Epic) task);
                } else if (split.get(1).equals(String.valueOf(SUB_TASK))) {
                    task = new SubTask(split.get(2), split.get(4), split.get(3), Integer.parseInt(split.get(5)));
                    subTasks.put((Integer.valueOf(split.get(0))), (SubTask) task);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //восстановление данных менеджера из файла
    public FileBackedTasksManager loadFromFile(File file) {
        fromString("csvSave.csv");
        fromStringHistory("csvSave.csv");
        return new FileBackedTasksManager();
    }

    @Override
    public List<Task> getHistory() {
        List<Task> fileGetHistory = super.getHistory();
        save();
        return fileGetHistory;
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
    public HashMap<Integer, Task> getAllTasks() {
        HashMap<Integer, Task> getAllTasks = super.getAllTasks();
        save();
        return getAllTasks;
    }

    @Override
    public HashMap<Integer, Epic> getAllEpics() {
        HashMap<Integer, Epic> getAllEpics = super.getAllEpics();
        save();
        return getAllEpics;
    }

    @Override
    public HashMap<Integer, SubTask> getAllSubTasks() {
        HashMap<Integer, SubTask> getAllSubTasks = super.getAllSubTasks();
        save();
        return getAllSubTasks;
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

    public static void main(String[] args) {
        FileBackedTasksManager fb = new FileBackedTasksManager();
        Task task1 = new Task("Task1", "Task1 Description");
        fb.createTasks(task1);
        Task task2 = new Task("Task2", "Task2 Description");
        fb.createTasks(task2);
        //создаем эпик с 2 подзадачами
        Epic epic1 = new Epic("Epic1", "Epic1 Description");
        fb.createEpics(epic1);
        SubTask subTask1 = new SubTask("Sub Task1", "Sub Task1 Description", epic1.getId());
        fb.createSubTasks(subTask1);
        SubTask subTask2 = new SubTask("Sub Task2", "Sub Task2 Description", epic1.getId());
        fb.createSubTasks(subTask2);
        SubTask subTask3 = new SubTask("Sub Task3", "Sub Task3 Description", epic1.getId());
        fb.createSubTasks(subTask3);
        //создаем эпик с 1 подзадачей
        Epic epic2 = new Epic("Epic2", "Epic2 Description");
        fb.createEpics(epic2);

        fb.getEpicById(6);//помыть аквариум
        fb.getEpicById(2); //сходить в магазин
        fb.getTaskById(0); //полить цветы
        fb.getEpicById(6);//помыть аквариум
        fb.getTaskById(1); //накормить кота
        fb.getEpicById(6);//помыть аквариум
        fb.getSubTaskById(3); //купить молоко
        fb.getSubTaskById(4); //купить кофе
        fb.getSubTaskById(5); //купить вафли
        //смотрим историю
        System.out.println("смотрим историю:");
        System.out.println(fb.getHistory());
        fb.fromString("csvSave.csv");
        fb.loadFromFile(new File("csvSave"));
    }

}



