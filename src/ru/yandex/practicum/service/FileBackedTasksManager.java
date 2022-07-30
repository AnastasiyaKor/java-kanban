package service;

import model.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static service.TaskType.*;

public class FileBackedTasksManager extends InMemoryTaskManager {

    private final File scvSave;
    private static final String FILE_HEADER = "id,type,name,status,description,startTime,duration,epic";

    public FileBackedTasksManager(File scvSave) {
        this.scvSave = scvSave;
    }

    // сохранение в файл
    private void save() {
        try (FileWriter fileWriter = new FileWriter(scvSave)) {
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
            fileWriter.write("\n");
            fileWriter.write(toStringHistory(historyManager));
        } catch (IOException e) {
            e.printStackTrace();
            throw new ManagerSaveException("Произошла проблема с сохранением в файл FileBackedTasksManager");
        }
    }

    private String toStringTask(Task task) {
        return task.getId() + "," + TASK + "," + task.getName() + "," + task.getStatus() + ","
                + task.getDescription() + "," + task.getStartTime() + ","
                + task.getDuration();
    }

    private String toStringEpic(Epic epic) {
        return epic.getId() + "," + EPIC + "," + epic.getName() + "," + epic.getStatus() + ","
                + epic.getDescription() + "," + epic.getStartTime() + ","
                + epic.getDuration();
    }

    private String toStringSubtask(SubTask subTask) {
        return subTask.getId() + "," + SUB_TASK + "," + subTask.getName() + ","
                + subTask.getStatus() + "," + subTask.getDescription() + "," +
                subTask.getStartTime() + "," + subTask.getDuration() + ","
                 + subTask.getEpicId();
    }

    //сохранение истории в файл
    private String toStringHistory(HistoryManager manager) {
        StringBuilder sb = new StringBuilder();
        for (Task task : manager.getHistory()) {
            sb.append(task.getId());
            sb.append(" ");
        }
        String str = sb.toString();
        String[] split = str.split(" ");
        return String.join(",", split);
    }

    //метод создания истории из строки
    private List<Integer> fromStringHistory(String value) {
        List<Integer> historyId = new ArrayList<>();
        if (value != null) {
            String[] split = value.split(",");
            for (String str : split) {
                historyId.add(Integer.parseInt(str));
            }
        }
        return historyId;
    }

    //метод создания задачи из строки
    private void fromString(File file) {
        Task task;
        if (file != null) {
            try (FileReader reader = new FileReader(file); BufferedReader br = new BufferedReader(reader)) {
                br.readLine();
                String line;
                List<String> split;
                while (br.ready()) {
                    line = br.readLine();
                    if (!(line.isBlank())) {
                        split = List.of(line.split(","));
                        int idTask = Integer.parseInt(split.get(0));
                        if (idTask > id) {
                            id = idTask;
                        }
                        switch (TaskType.valueOf(split.get(1))) {
                            case TASK:
                                task = new Task(split.get(2), split.get(4), split.get(3), idTask,
                                        String.valueOf(split.get(5)), Long.parseLong(split.get(6)));
                                tasks.put(idTask, task);
                                priorityTasks.put(task.getStartTime(),task);
                                break;
                            case EPIC:
                                task = new Epic(split.get(2), split.get(4), split.get(3), idTask,
                                        String.valueOf(split.get(5)), Long.parseLong(split.get(6)));
                                epics.put(idTask, (Epic) task);
                                priorityTasks.put(task.getStartTime(),task);
                                break;
                            case SUB_TASK:
                                task = new SubTask(split.get(2), split.get(4), split.get(3), idTask,
                                        String.valueOf(split.get(5)), Long.parseLong(split.get(6)),
                                        Integer.parseInt(split.get(7)));
                                subTasks.put(idTask, (SubTask) task);
                                priorityTasks.put(task.getStartTime(),task);
                                getSubtasksByEpicId(Integer.parseInt(split.get(7))).add(idTask);
                                break;
                        }
                    } else {
                        line = br.readLine();
                        List<Integer> count = fromStringHistory(line);
                        for (Integer i : count) {
                            if (tasks.containsKey(i)) {
                                historyManager.add(tasks.get(i));
                            } else if (epics.containsKey(i)) {
                                historyManager.add(epics.get(i));
                            } else {
                                historyManager.add(subTasks.get(i));
                            }
                        }
                        System.out.println(historyManager.getHistory()); //для наглядности
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //восстановление данных менеджера из файла
    public static FileBackedTasksManager loadFromFile(File file) {
        FileBackedTasksManager fb = new FileBackedTasksManager(file);
        fb.fromString(file);
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
        Task task = new Task("task1", "description1", 30, 2022, 05,
                14, 22, 22);
        fb.createTasks(task);
        System.out.println("вызываем задачу:");
        System.out.println(fb.getAllTasks());
        //создаем эпик с 3 подзадачами
        Epic epic1 = new Epic("epic1", "описание эпик 1");
        fb.createEpics(epic1);
        SubTask subTask1 = new SubTask("подзадача1", "описание подзадачи1", 40,
                2022, 07, 12, 12, 00, epic1.getId());
        fb.createSubTasks(subTask1);
        SubTask subTask2 = new SubTask("подзадача2", "описание подзадачи", 30,
                2022, 07, 12, 11, 00, epic1.getId());
        fb.createSubTasks(subTask2);
        SubTask subTask3 = new SubTask("подзадача3", "описание подзадачи3", 30,
                2022, 07, 12, 14, 00, epic1.getId());
        fb.createSubTasks(subTask3);
        System.out.println("вызываем эпик:");
        System.out.println(fb.getAllEpics());
        System.out.println("вызываем подзадачи:");
        System.out.println(fb.getAllSubTasks());
        //вызываем задачи
        System.out.println(fb.getEpicById(1));
        System.out.println(fb.getTaskById(0));
        System.out.println(fb.getEpicById(1));
        System.out.println(fb.getTaskById(0));
        System.out.println(fb.getSubTaskById(3));
        System.out.println(fb.getSubTaskById(2));
        System.out.println(fb.getSubTaskById(4));
        System.out.println("Смотрим историю:");
        System.out.println(fb.getHistory());//смотрим историю
        System.out.println("Восстановление из файла:");
        FileBackedTasksManager fileBackedTasksManager = loadFromFile(new File("csvSave.csv"));
        System.out.println(fileBackedTasksManager);
        System.out.println(fb.getPrioritizedTasks());
    }
}



