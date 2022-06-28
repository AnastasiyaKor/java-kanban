package ru.yandex.practicum.service;

import ru.yandex.practicum.model.Epic;
import ru.yandex.practicum.model.SubTask;
import ru.yandex.practicum.model.Task;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static ru.yandex.practicum.service.TaskType.*;

public class FileBackedTasksManager extends InMemoryTaskManager {

    private final File fileCSV;
    private static final String FILE_HEADER = "id,type,name,status,description,epic";

    public FileBackedTasksManager(File fileCSV) {
        this.fileCSV = fileCSV;
    }

    // сохранение в историю
    private void save() {
        try (FileWriter fileWriter = new FileWriter(fileCSV)) {
            fileWriter.write(FILE_HEADER);
            fileWriter.write("\n");
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
            fileWriter.write(" ");
            fileWriter.write("\n");
            fileWriter.write(toStringHistory(historyManager));
        } catch (IOException e) {
            e.printStackTrace();
            throw new ManagerSaveException("Произошла проблема с сохранением в файл FileBackedTasksManager");
        }
    }

    private String toStringTask(Task task) {
        return task.getId() + "," + TASK + "," + task.getName() + "," + task.getStatus() + ","
                + task.getDescription();
    }

    private String toStringEpic(Epic epic) {
        return epic.getId() + "," + EPIC + "," + epic.getName() + "," + epic.getStatus() + ","
                + epic.getDescription();
    }

    private String toStringSubtask(SubTask subTask) {
        return subTask.getId() + "," + SUB_TASK + "," + subTask.getName() + ","
                + subTask.getStatus() + "," + subTask.getDescription() + "," + subTask.getEpicId();
    }

    //подготовка истории к сохранению в строку
    private String toStringHistory(HistoryManager manager) {
        StringBuilder sb = new StringBuilder();
        for (Task task : manager.getHistory()) {
            sb.append(task.getId());
            sb.append(" ");
        }
        String history = sb.toString();
        String[] split = history.split(" ");
        return String.join(",", split);
    }

    //метод создания истории из строки
    private List<Integer> fromStringHistory(String fileCSV) {
        List<Integer> historyId = new ArrayList<>();
        if (fileCSV.isBlank()) {
            if (historyManager != null) {
                String line = toStringHistory(historyManager);
                String[] split = line.split(",");
                for (int i = 1; i < split.length; i++) {
                    historyId.add(i);
                }
            }
        }
        return historyId;
    }

    //метод создания задачи из строки
    private void fromString(String fileCSV) {
        try (FileReader reader = new FileReader(fileCSV);
             BufferedReader br = new BufferedReader(reader)) {
            String line1 = br.readLine();
            String line;
            List<String> split;
            Task task;
            while (br.ready()) {
                line = br.readLine();
                if (!(line.equals(" "))) {
                    split = List.of(line.split(","));
                    if (split.get(1).equals(String.valueOf(TASK))) {
                        task = new Task(split.get(2), split.get(4), split.get(3));
                        tasks.put(Integer.valueOf(split.get(0)), task);
                    } else if (split.get(1).equals(String.valueOf(EPIC))) {
                        task = new Epic(split.get(2), split.get(4), split.get(3));
                        epics.put((Integer.valueOf(split.get(0))), (Epic) task);
                    } else if (split.get(1).equals(String.valueOf(SUB_TASK))) {
                        task = new SubTask(split.get(2), split.get(4), split.get(3),
                                Integer.parseInt(split.get(5)));
                        subTasks.put((Integer.valueOf(split.get(0))), (SubTask) task);
                        if (getSubtasksByEpicId(Integer.parseInt(split.get(5))) != null) {
                            getSubtasksByEpicId(Integer.parseInt(split.get(5))).add((Integer.valueOf(split.get(0))));
                        }
                    }
                    if (Integer.parseInt(split.get(0)) > id) {
                        id = Integer.parseInt(split.get(0));
                    }
                } else {
                    return;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //восстановление данных менеджера из файла
    public static FileBackedTasksManager loadFromFile(File fileCSV) {
        FileBackedTasksManager fb = new FileBackedTasksManager(new File(String.valueOf(fileCSV)));
        fb.fromString("csvSave.csv");
        fb.fromStringHistory("csvSave.csv");
        return fb;
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

    public static void main(String[] args) {
        FileBackedTasksManager fb = new FileBackedTasksManager(new File("csvSave.csv"));
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
        fb.getHistory();//смотрим историю
        FileBackedTasksManager fileBackedTasksManager = loadFromFile(new File("csvSave.csv"));
    }
}



