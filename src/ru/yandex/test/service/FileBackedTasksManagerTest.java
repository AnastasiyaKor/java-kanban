package service;

import model.Epic;
import model.SubTask;
import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static service.FileBackedTasksManager.loadFromFile;

class FileBackedTasksManagerTest extends TaskManagerTest<FileBackedTasksManager> {
    FileBackedTasksManager fileBackedTasksManager;
    File file = new File("csvSave.csv");


    @Override
    @BeforeEach
    void beforeEach() {
        fileBackedTasksManager = new FileBackedTasksManager(file);
    }

    //1.1проверка восстановления данных пустого из файла
    @Test
    void Test1_1_CheckingDataRecoveryFromAnEmptyFile() {
        assertTrue(fileBackedTasksManager.epics.isEmpty());
    }

    //2.1проверка восстановления данных эпика из файла
    @Test
    void Test2_1_CheckingTheRecoveryOfAnEpicFromAFile() {
        Epic epic1 = new Epic("епик1", "описание эпика 1");
        fileBackedTasksManager.createEpics(epic1);
        SubTask subTask1 = new SubTask("подзадача1", "описание подзадачи1",
                30, "2023-10-15T21:00:00", epic1.getId());
        fileBackedTasksManager.createSubTasks(subTask1);
        assertEquals(1, fileBackedTasksManager.epics.size(), "данные эпика не восстановлены");
    }

    //3.1.проверка идентичности данных эпиков из с файла и памяти
    @Test
    void Test3_1__IdentityOfEpicDataFromFileAndMemory() {
        Epic epic1 = new Epic("епик1", "описание эпика 1");
        Epic epic2 = new Epic("епик2", "описание эпика 2");
        SubTask subTask1 = new SubTask("подзадача1 эпика 1", "описание подзадачи1",
                30, "2022-08-09T21:00:00", epic1.getId());
        SubTask subTask2 = new SubTask("подзадача1 эпика2", "описание подзадачи1",
                30, "2022-08-09T21:00:00", epic2.getId());
        fileBackedTasksManager.createSubTasks(subTask1);
        fileBackedTasksManager.createEpics(epic1);
        fileBackedTasksManager.createSubTasks(subTask2);
        fileBackedTasksManager.createEpics(epic2);
        assertEquals(fileBackedTasksManager.getAllEpics(), loadFromFile(file).getAllEpics(),
                "Список эпиков после выгрузки не совпададает");
    }

    //4.1.проверка идентичности данных задач из с файла и памяти
    @Test
    void Test4_1_IdentityOfTaskDataFromFileAndMemory() {
        Task task = new Task("задача1", "описание задачи 1", 30,
                "2022-08-10T21:00:00");
        Task task2 = new Task("задача2", "описание задачи 2", 30,
                "2022-08-11T21:00:00");
        fileBackedTasksManager.createTasks(task);
        fileBackedTasksManager.createTasks(task2);
        assertEquals(fileBackedTasksManager.getAllTasks(), loadFromFile(file).getAllTasks(),
                "Список задач после выгрузки не совпададает");
    }

    //5.1.проверка идентичности данных подзадач из с файла и памяти
    @Test
    void Test5_1_IdentityOfSubTaskDataFromFileAndMemory() {
        Epic epic1 = new Epic("епик1", "описание эпика 1");
        fileBackedTasksManager.createEpics(epic1);
        SubTask subTask1 = new SubTask("подзадача1", "описание подзадачи1",
                30, "2022-08-13T21:00:00", epic1.getId());
        SubTask subTask2 = new SubTask("подзадача2", "описание подзадачи2",
                30, "2022-08-14T21:00:00", epic1.getId());
        SubTask subTask3 = new SubTask("подзадача3", "описание подзадачи3",
                30, "2022-08-15T21:00:00", epic1.getId());
        fileBackedTasksManager.createSubTasks(subTask1);
        fileBackedTasksManager.createSubTasks(subTask2);
        fileBackedTasksManager.createSubTasks(subTask3);
        assertEquals(fileBackedTasksManager.getAllSubTasks(), loadFromFile(file).getAllSubTasks(),
                "Список подзадач после выгрузки не совпададает");
    }

    //6.1.проверка восстановления пустой истории из файла
    @Test
    void Test6_1_CheckingTheRecoveryOfAnEmptyHistoryFromAFile() {
        assertEquals(0, loadFromFile(file).getHistory().size());
    }

    //7.1.проверка восстановления истории из файла
    @Test
    void Test7_1_CheckingTheHistoryRecoveryFromAFilledFile() {
        Task task = new Task("задача1", "описание задачи 1", 30,
                "2022-10-15T21:00:00");
        Epic epic1 = new Epic("епик1", "описание эпика 1");
        SubTask subTask1 = new SubTask("подзадача1", "описание подзадачи1",
                30, "2022-08-16T21:00:00", 1);
        fileBackedTasksManager.createTasks(task);
        fileBackedTasksManager.createEpics(epic1);
        fileBackedTasksManager.createSubTasks(subTask1);
        fileBackedTasksManager.getEpicById(1);
        fileBackedTasksManager.getSubTaskById(2);
        fileBackedTasksManager.getTaskById(0);
        assertEquals(fileBackedTasksManager.getHistory(), loadFromFile(file).getHistory());
    }
}