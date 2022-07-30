package service;

import model.Epic;
import model.SubTask;
import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

class FileBackedTasksManagerTest extends TaskManagerTest<FileBackedTasksManager> {
    FileBackedTasksManager fileBackedTasksManager;
    File file = new File("csvSave");

    @BeforeEach
    protected void beforeEach() {
        inMemoryTaskManager = new InMemoryTaskManager();
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
        SubTask subTask1 = new SubTask("подзадача1", "описание подзадачи1", 40, 2022,
                04, 12, 12, 00, epic1.getId());
        fileBackedTasksManager.createSubTasks(subTask1);
        assertEquals(1, fileBackedTasksManager.epics.size(), "данные эпика не восстановлены");
    }

    //3.1.проверка идентичности данных эпиков из с файла и памяти
    @Test
    void Test3_1__IdentityOfEpicDataFromFileAndMemory() {
        InMemoryTaskManager taskManager = new InMemoryTaskManager();
        Epic epic1 = new Epic("епик1", "описание эпика 1");
        fileBackedTasksManager.createEpics(epic1);
        taskManager.createEpics(epic1);
        Epic epic2 = new Epic("епик2", "описание эпика 2");
        fileBackedTasksManager.createEpics(epic2);
        taskManager.createEpics(epic2);
        SubTask subTask1 = new SubTask("подзадача1", "описание подзадачи1", 40, 2022,
                04, 12, 12, 00, epic1.getId());
        fileBackedTasksManager.createSubTasks(subTask1);
        taskManager.createSubTasks(subTask1);
        SubTask subTask2 = new SubTask("подзадача1", "описание подзадачи1", 60, 2022,
                04, 28, 12, 00, epic2.getId());
        fileBackedTasksManager.createSubTasks(subTask1);
        taskManager.createSubTasks(subTask1);
        fileBackedTasksManager.createSubTasks(subTask2);
        taskManager.createSubTasks(subTask2);
        assertEquals(taskManager.getAllEpics(), fileBackedTasksManager.getAllEpics(),
                "Список эпиков после выгрузки не совпададает");
    }

    //4.1.проверка идентичности данных задач из с файла и памяти
    @Test
    void Test4_1_IdentityOfTaskDataFromFileAndMemory() {
        InMemoryTaskManager taskManager = new InMemoryTaskManager();
        Task task = new Task("задача1", "описание задачи 1",
                40, 2022, 12, 12, 20, 00);
        Task task2 = new Task("задача2", "описание задачи 2",
                80, 2022, 12, 18, 20, 00);
        fileBackedTasksManager.createTasks(task);
        taskManager.createTasks(task);
        fileBackedTasksManager.createTasks(task2);
        taskManager.createTasks(task2);
        assertEquals(taskManager.getAllTasks(), fileBackedTasksManager.getAllTasks(),
                "Список задач после выгрузки не совпададает");
    }

    //5.1.проверка идентичности данных подзадач из с файла и памяти
    @Test
    void Test5_1_IdentityOfSubTaskDataFromFileAndMemory() {
        InMemoryTaskManager taskManager = new InMemoryTaskManager();
        Epic epic1 = new Epic("епик1", "описание эпика 1");
        fileBackedTasksManager.createEpics(epic1);
        taskManager.createEpics(epic1);
        SubTask subTask1 = new SubTask("подзадача1", "описание подзадачи1", 40, 2022,
                04, 12, 12, 00, epic1.getId());
        SubTask subTask2 = new SubTask("подзадача2", "описание подзадачи2", 70, 2022,
                04, 19, 12, 00, epic1.getId());
        SubTask subTask3 = new SubTask("подзадача3", "описание подзадачи3", 100, 2022,
                04, 22, 12, 00, epic1.getId());
        fileBackedTasksManager.createSubTasks(subTask1);
        taskManager.createSubTasks(subTask1);
        fileBackedTasksManager.createSubTasks(subTask2);
        taskManager.createSubTasks(subTask2);
        fileBackedTasksManager.createSubTasks(subTask3);
        taskManager.createSubTasks(subTask3);
        assertEquals(taskManager.getAllSubTasks(), fileBackedTasksManager.getAllSubTasks(),
                "Список подзадач после выгрузки не совпададает");
    }

    //6.1.проверка восстановления пустой истории из файла
    @Test
    void Test6_1_CheckingTheRecoveryOfAnEmptyHistoryFromAFile() {
        assertEquals(0, fileBackedTasksManager.getHistory().size());
    }

    //7.1.проверка восстановления истории из файла
    @Test
    void Test7_1_CheckingTheHistoryRecoveryFromAFilledFile() {
        Task task = new Task("задача1", "описание задачи 1",
                40, 2022, 12, 12, 20, 00);
        fileBackedTasksManager.createTasks(task);
        Epic epic1 = new Epic("епик1", "описание эпика 1");
        fileBackedTasksManager.createEpics(epic1);
        SubTask subTask1 = new SubTask("подзадача1", "описание подзадачи1", 40, 2022,
                04, 12, 12, 00, epic1.getId());
        fileBackedTasksManager.createSubTasks(subTask1);
        SubTask subTask2 = new SubTask("подзадача2", "описание подзадачи", 30, 2022,
                07, 12, 13, 00, epic1.getId());
        fileBackedTasksManager.createSubTasks(subTask2);
        fileBackedTasksManager.getSubTaskById(3);
        fileBackedTasksManager.getSubTaskById(2);
        fileBackedTasksManager.getEpicById(1);
        fileBackedTasksManager.getTaskById(0);
        assertEquals(4, fileBackedTasksManager.getHistory().size());
    }
}