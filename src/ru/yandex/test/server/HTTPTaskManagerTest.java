package server;

import model.Epic;
import model.SubTask;
import model.Task;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class HTTPTaskManagerTest {
    HTTPTaskManager httpTaskManager;
    String url = "http://localhost:8078/load/tasks";
    KVServer kvServer = new KVServer();
    KVTaskClient kvTaskClient;

    HTTPTaskManagerTest() throws IOException {
    }

    @BeforeEach
    public void startServers() {
        kvServer.start();
        System.out.println("Сервер запущен");
        kvTaskClient = new KVTaskClient("http://localhost:8078/");
        httpTaskManager = new HTTPTaskManager("http://localhost:8078/", false);
    }

    @AfterEach
    public void stopServers() {
        kvServer.stop();
    }


    //1проверка восстановления данных эпика из файла
    @Test
    void Test1_CheckingTheRecoveryOfAnEpicFromAFile() {
        Epic epic1 = new Epic("епик1", "описание эпика 1");
        httpTaskManager.createEpics(epic1);
        SubTask subTask1 = new SubTask("подзадача1", "описание подзадачи1", 30,
                "2023-10-15T21:00:00", epic1.getId());
        httpTaskManager.createSubTasks(subTask1);
        HTTPTaskManager taskManager = new HTTPTaskManager("http://localhost:8078/", true);
        assertEquals(1, taskManager.getAllEpics().size(), "данные эпика не восстановлены");
        kvServer.stop();
    }

    //2.проверка идентичности данных эпиков из с файла и памяти
    @Test
    void Test2_IdentityOfEpicDataFromFileAndMemory() {
        Epic epic1 = new Epic("епик1", "описание эпика 1");
        Epic epic2 = new Epic("епик2", "описание эпика 2");
        SubTask subTask1 = new SubTask("подзадача1 эпика 1", "описание подзадачи1", 30,
                "2022-08-10T21:00:00", 0);
        SubTask subTask2 = new SubTask("подзадача1 эпика2", "описание подзадачи1", 30,
                "2022-08-09T21:00:00", 1);
        httpTaskManager.createEpics(epic1);
        httpTaskManager.createEpics(epic2);
        httpTaskManager.createSubTasks(subTask1);
        httpTaskManager.createSubTasks(subTask2);
        HTTPTaskManager taskManager = new HTTPTaskManager("http://localhost:8078/", true);
        assertEquals(httpTaskManager.getAllEpics(), taskManager.getAllEpics(),
                "Список эпиков после выгрузки не совпададает");
    }

    //3.проверка идентичности данных задач из с файла и памяти
    @Test
    void Test3_IdentityOfTaskDataFromFileAndMemory() {
        Task task = new Task("задача1", "описание задачи 1", 30,
                "2022-08-10T21:00:00");
        Task task2 = new Task("задача2", "описание задачи 2", 30,
                "2022-08-11T21:00:00");
        httpTaskManager.createTasks(task);
        httpTaskManager.createTasks(task2);
        HTTPTaskManager taskManager = new HTTPTaskManager("http://localhost:8078/", true);
        assertEquals(httpTaskManager.getAllTasks(), taskManager.getAllTasks(),
                "Список задач после выгрузки не совпададает");
    }

    //4.проверка идентичности данных подзадач из с файла и памяти
    @Test
    void Test4_IdentityOfSubTaskDataFromFileAndMemory() {
        Epic epic1 = new Epic("епик1", "описание эпика 1");
        httpTaskManager.createEpics(epic1);
        SubTask subTask1 = new SubTask("подзадача1", "описание подзадачи1",
                30, "2022-08-13T21:00:00", epic1.getId());
        SubTask subTask2 = new SubTask("подзадача2", "описание подзадачи2",
                30, "2022-08-14T21:00:00", epic1.getId());
        SubTask subTask3 = new SubTask("подзадача3", "описание подзадачи3",
                30, "2022-08-15T21:00:00", epic1.getId());
        httpTaskManager.createSubTasks(subTask1);
        httpTaskManager.createSubTasks(subTask2);
        httpTaskManager.createSubTasks(subTask3);
        HTTPTaskManager taskManager = new HTTPTaskManager("http://localhost:8078/", true);
        assertEquals(httpTaskManager.getAllSubTasks(), taskManager.getAllSubTasks(),
                "Список подзадач после выгрузки не совпададает");
    }

    //5.проверка восстановления пустой истории из файла
    @Test
    void Test5_CheckingTheRecoveryOfAnEmptyHistoryFromAFile() {
        HTTPTaskManager taskManager = new HTTPTaskManager("http://localhost:8078/", true);
        assertEquals(0, taskManager.getHistory().size());
    }

    //6.проверка восстановления истории из файла
    @Test
    void Test6_CheckingTheHistoryRecoveryFromAFilledFile() {
        Task task = new Task("задача1", "описание задачи 1", 30,
                "2022-10-15T21:00:00");
        Epic epic1 = new Epic("епик1", "описание эпика 1");
        SubTask subTask1 = new SubTask("подзадача1", "описание подзадачи1", 30,
                "2022-08-16T21:00:00", 1);
        httpTaskManager.createTasks(task);
        httpTaskManager.createEpics(epic1);
        httpTaskManager.createSubTasks(subTask1);
        httpTaskManager.getEpicById(1);
        httpTaskManager.getSubTaskById(2);
        httpTaskManager.getTaskById(0);
        HTTPTaskManager taskManager = new HTTPTaskManager("http://localhost:8078/", true);
        assertEquals(httpTaskManager.getHistory(), taskManager.getHistory());
    }

    //7.проверка восстановления списка приоритета
    @Test
    void Test7_CheckingThePrioritizedRecoveryFromAFilledFile() {
        Task task = new Task("задача1", "описание задачи 1", 30,
                "2022-10-15T21:00:00");
        Epic epic1 = new Epic("епик1", "описание эпика 1");
        SubTask subTask1 = new SubTask("подзадача1", "описание подзадачи1", 30,
                "2022-08-16T21:00:00", 1);
        httpTaskManager.createTasks(task);
        httpTaskManager.createEpics(epic1);
        httpTaskManager.createSubTasks(subTask1);
        HTTPTaskManager taskManager = new HTTPTaskManager("http://localhost:8078/", true);
        assertEquals(httpTaskManager.getPrioritizedTasks(), taskManager.getPrioritizedTasks());
    }
}