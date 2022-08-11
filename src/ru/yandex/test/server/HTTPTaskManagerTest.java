package server;

import model.Epic;
import model.SubTask;
import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.http.HttpClient;

import static org.junit.jupiter.api.Assertions.*;
import static server.HTTPTaskManager.load;

class HTTPTaskManagerTest {
    HTTPTaskManager httpTaskManager;
    URL url = new URL("http://localhost:8078/load/tasks");
    KVServer kvServer = new KVServer();
    KVTaskClient kvTaskClient;

    HTTPTaskManagerTest() throws IOException {
    }

    @BeforeEach
    public void startServers() throws IOException, URISyntaxException, InterruptedException {
        kvTaskClient = new KVTaskClient(new URL("http://localhost:8078/"));
        kvServer.start();
        httpTaskManager = new HTTPTaskManager(url);
    }

    //2.1проверка восстановления данных эпика из файла
    @Test
    void Test2_1_CheckingTheRecoveryOfAnEpicFromAFile() {
        Epic epic1 = new Epic("епик1", "описание эпика 1");
        httpTaskManager.createEpics(epic1);
        SubTask subTask1 = new SubTask("подзадача1", "описание подзадачи1", 30,
                "2023-10-15T21:00:00", epic1.getId());
        httpTaskManager.createSubTasks(subTask1);
        assertEquals(1, httpTaskManager.getAllEpics().size(), "данные эпика не восстановлены");
    }

    //3.1.проверка идентичности данных эпиков из с файла и памяти
    @Test
    void Test3_1__IdentityOfEpicDataFromFileAndMemory() throws IOException,
            URISyntaxException, InterruptedException {
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
        assertEquals(load(url).getAllEpics(), httpTaskManager.getAllEpics(),
                "Список эпиков после выгрузки не совпададает");
    }

    //4.1.проверка идентичности данных задач из с файла и памяти
    @Test
    void Test4_1_IdentityOfTaskDataFromFileAndMemory() throws IOException,
            URISyntaxException, InterruptedException {
        Task task = new Task("задача1", "описание задачи 1", 30,
                "2022-08-10T21:00:00");
        Task task2 = new Task("задача2", "описание задачи 2", 30,
                "2022-08-11T21:00:00");
        httpTaskManager.createTasks(task);
        httpTaskManager.createTasks(task2);
        assertEquals(httpTaskManager.getAllTasks(), load(url).getAllTasks(),
                "Список задач после выгрузки не совпададает");
    }

    //5.1.проверка идентичности данных подзадач из с файла и памяти
    @Test
    void Test5_1_IdentityOfSubTaskDataFromFileAndMemory() throws IOException,
            URISyntaxException, InterruptedException {
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
        assertEquals(httpTaskManager.getAllSubTasks(), load(url).getAllSubTasks(),
                "Список подзадач после выгрузки не совпададает");
    }

    //6.1.проверка восстановления пустой истории из файла
    @Test
    void Test6_1_CheckingTheRecoveryOfAnEmptyHistoryFromAFile() throws IOException,
            URISyntaxException, InterruptedException {
        assertEquals(0, load(url).getHistory().size());
    }

    //7.1.проверка восстановления истории из файла
    @Test
    void Test7_1_CheckingTheHistoryRecoveryFromAFilledFile() throws IOException,
            URISyntaxException, InterruptedException {
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
        assertEquals(httpTaskManager.getHistory(), load(url).getHistory());
    }

}