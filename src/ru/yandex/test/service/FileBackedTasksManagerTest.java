package service;

import model.Epic;
import model.SubTask;
import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

class FileBackedTasksManagerTest extends TaskManagerTest<FileBackedTasksManager> {
    File file;

    @BeforeEach
    void beforeEach() {
        file = new File("csvSave");
    }

    //1.проверка восстановления данных пустого из файла
    @Test
    void Test1_CheckingDataRecoveryFromAnEmptyFile() {
        FileBackedTasksManager fileBacked = new FileBackedTasksManager(file);
        assertTrue(fileBacked.epics.isEmpty());
    }

    //2.проверка восстановления данных эпика из файла
    @Test
    void Test2_CheckingTheRecoveryOfAnEpicFromAFile() {
        FileBackedTasksManager fileBacked = new FileBackedTasksManager(file);
        Epic epic1 = new Epic("епик1", "описание эпика 1");
        fileBacked.createEpics(epic1);
        SubTask subTask1 = new SubTask("подзадача1", "описание подзадачи1", 40, 2022,
                04, 12, 12, 00, epic1.getId());
        fileBacked.createSubTasks(subTask1);
        assertEquals(1, fileBacked.epics.size());
    }

    //3.проверка восстановления пустой истории из файла
    @Test
    void Test3_CheckingTheRecoveryOfAnEmptyHistoryFromAFile() {
        FileBackedTasksManager fileBacked = new FileBackedTasksManager(file);
        assertEquals(0, fileBacked.getHistory().size());
    }

    //4.проверка восстановления истории из файла
    @Test
    void Test4_CheckingTheHistoryRecoveryFromAFilledFile() {
        FileBackedTasksManager fileBacked = new FileBackedTasksManager(file);
        Task task = new Task("задача1", "описание задачи 1",
                40, 2022, 12, 12, 20, 00);
        fileBacked.createTasks(task);
        Epic epic1 = new Epic("епик1", "описание эпика 1");
        fileBacked.createEpics(epic1);
        SubTask subTask1 = new SubTask("подзадача1", "описание подзадачи1", 40, 2022,
                04, 12, 12, 00, epic1.getId());
        fileBacked.createSubTasks(subTask1);
        SubTask subTask2 = new SubTask("подзадача2", "описание подзадачи", 30, 2022,
                07, 12, 13, 00, epic1.getId());
        fileBacked.createSubTasks(subTask2);
        fileBacked.getSubTaskById(3);
        fileBacked.getSubTaskById(2);
        fileBacked.getEpicById(1);
        fileBacked.getTaskById(0);
        assertEquals(4, fileBacked.getHistory().size());
    }
}