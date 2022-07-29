package service;

import model.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static service.Status.*;

class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {
    InMemoryTaskManager inMemoryTaskManager;

    @BeforeEach
    void beforeEach() {
        inMemoryTaskManager = new InMemoryTaskManager();
    }

    //1.расчет статуса эпика с пустым списком подзадач
    @Test
    void Test1_CalculationOfTheEpicStatusWithAnEmptyListOfSubtasks() {
        Epic epic1 = new Epic("Эпик 1", "описание эпика 1");
        inMemoryTaskManager.createEpics(epic1);
        assertEquals(NEW, epic1.getStatus());
    }

    //2.расчет статуса эпика. Все задачи со статусом NEW
    @Test
    void Test2_CalculationOfTheEpicStatusAllTasksWithTheStatusNew() {
        Epic epic1 = new Epic("Эпик 1", "описание эпика 1");
        inMemoryTaskManager.createEpics(epic1);
        inMemoryTaskManager.createSubTasks(new SubTask("подзадача1",
                "описание подзадачи1", 40, 2022,
                04, 12, 12, 00, epic1.getId()));
        inMemoryTaskManager.createSubTasks(new SubTask("подзадача2",
                "описание подзадачи", 30, 2022,
                07, 12, 13, 00, epic1.getId()));
        assertEquals(NEW, epic1.getStatus());
    }

    //3.расчет статуса эпика. Все задачи со статусом DONE
    @Test
    void Test3_CalculationOfTheEpicStatusAllTasksWithTheStatusDone() {
        Epic epic1 = new Epic("Эпик 1", "описание эпика 1");
        inMemoryTaskManager.createEpics(epic1);
        SubTask subTask1 = new SubTask("подзадача1", "описание подзадачи1", 40, 2022,
                04, 12, 12, 00, epic1.getId());
        subTask1.setStatus(DONE);
        inMemoryTaskManager.refreshSubTasks(subTask1);
        SubTask subTask2 = new SubTask("подзадача2", "описание подзадачи", 30, 2022,
                07, 12, 13, 00, epic1.getId());
        inMemoryTaskManager.createSubTasks(subTask2);
        subTask2.setStatus(DONE);
        inMemoryTaskManager.refreshSubTasks(subTask2);
        inMemoryTaskManager.refreshStatusEpic(epic1.getId());
        assertEquals(DONE, epic1.getStatus());
    }

    //4.расчет статуса эпика. Подзадачи со статусами NEW и DONE
    @Test
    void Test4_CalculationOfTheEpicStatusSubtasksWithNewAndDoneStatuses() {
        Epic epic1 = new Epic("Эпик 1", "описание эпика 1");
        inMemoryTaskManager.createEpics(epic1);
        SubTask subTask1 = new SubTask("подзадача1", "описание подзадачи1", 40, 2022,
                04, 12, 12, 00, epic1.getId());
        inMemoryTaskManager.createSubTasks(subTask1);
        subTask1.setStatus(NEW);
        inMemoryTaskManager.refreshSubTasks(subTask1);
        SubTask subTask2 = new SubTask("подзадача2", "описание подзадачи", 30, 2022,
                07, 12, 13, 00, epic1.getId());
        inMemoryTaskManager.createSubTasks(subTask2);
        subTask2.setStatus(DONE);
        inMemoryTaskManager.refreshSubTasks(subTask2);
        inMemoryTaskManager.refreshStatusEpic(epic1.getId());
        assertEquals(IN_PROGRESS, epic1.getStatus());
    }

    //5.расчет статуса эпика. Подзадачи со статусами IN_PROGRESS
    @Test
    void Test5_CalculationOfTheEpicStatusSubtasksWithIn_ProgressStatuses() {
        Epic epic1 = new Epic("Эпик 1", "описание эпика 1");
        inMemoryTaskManager.createEpics(epic1);
        SubTask subTask1 = new SubTask("подзадача1", "описание подзадачи1", 40, 2022,
                04, 12, 12, 00, epic1.getId());
        inMemoryTaskManager.createSubTasks(subTask1);
        subTask1.setStatus(IN_PROGRESS);
        inMemoryTaskManager.refreshSubTasks(subTask1);
        SubTask subTask2 = new SubTask("подзадача2", "описание подзадачи", 30, 2022,
                07, 12, 13, 00, epic1.getId());
        inMemoryTaskManager.createSubTasks(subTask2);
        subTask2.setStatus(IN_PROGRESS);
        inMemoryTaskManager.refreshSubTasks(subTask2);
        inMemoryTaskManager.refreshStatusEpic(epic1.getId());
        assertEquals(IN_PROGRESS, epic1.getStatus());
    }

    //6. Проверка наличия эпика для подзадачи
    @Test
    void Test6_CheckingForThePresenceOfAnEpicInASubtask() {
        Epic epic1 = new Epic("Эпик 1", "описание эпика 1");
        inMemoryTaskManager.createEpics(epic1);
        SubTask subTask1 = new SubTask("подзадача1", "описание подзадачи1", 40, 2022,
                04, 12, 12, 00, epic1.getId());
        inMemoryTaskManager.createSubTasks(subTask1);
        SubTask subTask2 = new SubTask("подзадача2", "описание подзадачи", 30, 2022,
                07, 12, 13, 00, epic1.getId());
        inMemoryTaskManager.createSubTasks(subTask2);
        assertEquals(epic1.getId(), subTask1.getEpicId());
    }

    //7. Проверка записи задачи в мапу
    @Test
    void Test7_CreatingAndWritingATaskToTheMap() {
        inMemoryTaskManager.createTasks(new Task("Задача 1", "описание задачи 1",
                10, 2022, 07, 07, 20, 20));
        assertEquals(1, inMemoryTaskManager.tasks.size());
    }

    //8. Проверка записи эпика в мапу
    @Test
    void Test8_CreatingAndWritingAEpicToTheMap() {
        inMemoryTaskManager.createEpics(new Epic("Эпик 1", "описание эпика 1"));
        assertEquals(1, inMemoryTaskManager.epics.size());
    }

    //9. Проверка записи подзадачи в мапу
    @Test
    void Test9_CreatingAndWritingASubTaskToTheMap() {
        Epic epic1 = new Epic("Эпик 1", "описание эпика 1");
        inMemoryTaskManager.createEpics(epic1);
        inMemoryTaskManager.createSubTasks(new SubTask("подзадача1",
                "описание подзадачи1", 40, 2022,
                04, 12, 12, 00, epic1.getId()));
        assertEquals(1, inMemoryTaskManager.subTasks.size());
    }

    //10. Проверка получения задачи по идентификатору
    @Test
    void Test10_GettingATaskById() {
        Task task1 = new Task("Задача 1", "описание задачи 1",
                10, 2022, 07, 15, 20, 21);
        inMemoryTaskManager.createTasks(task1);
        assertEquals(0, task1.getId());
    }

    //10.1 Проверка получения задачи по неверному идентификатору
    @Test
    void Test10_1_GettingATaskByAnErroneousId() {
        Task task1 = new Task("Задача 1", "описание задачи 1",
                10, 2022, 07, 01, 20, 21);
        inMemoryTaskManager.createTasks(task1);
        Task taskId = inMemoryTaskManager.tasks.get(1);
        Assertions.assertNull(taskId);
    }

    //11. Проверка получения эпика по идентификатору
    @Test
    void Test11_GettingAEpicById() {
        Epic epic1 = new Epic("Эпик 1", "описание эпика 1");
        inMemoryTaskManager.createEpics(epic1);
        assertEquals(0, epic1.getId());
    }

    //11.1 Проверка получения эпика по неверному идентификатору
    @Test
    void Test11_1_GettingAEpicByAnErroneousId() {
        Epic epic1 = new Epic("Эпик 1", "описание эпика 1");
        inMemoryTaskManager.createEpics(epic1);
        Epic epicId = inMemoryTaskManager.epics.get(5);
        Assertions.assertNull(epicId);
    }

    //12. Проверка получения подзадачи по идентификатору
    @Test
    void Test12_GettingASubTaskById() {
        Epic epic1 = new Epic("Эпик 1", "описание эпика 1");
        inMemoryTaskManager.createEpics(epic1);
        SubTask subTask1 = new SubTask("подзадача1", "описание подзадачи1", 40, 2022,
                04, 12, 12, 00, epic1.getId());
        inMemoryTaskManager.createSubTasks(subTask1);
        assertEquals(1, subTask1.getId());
    }

    //12.1 Проверка получения подзадачи по неверному идентификатору
    @Test
    void Test12_1_GettingASubTaskByAnErroneousId() {
        Epic epic1 = new Epic("Эпик 1", "описание эпика 1");
        inMemoryTaskManager.createEpics(epic1);
        SubTask subTask1 = new SubTask("подзадача1", "описание подзадачи1", 40, 2022,
                04, 12, 12, 00, epic1.getId());
        inMemoryTaskManager.createSubTasks(subTask1);
        SubTask subTaskId = inMemoryTaskManager.subTasks.get(5);
        Assertions.assertNull(subTaskId);
    }

    //13 Проверка изменения статуса задачи
    @Test
    void Test13_CheckingTaskStatusChanges() {
        Task task1 = new Task("Задача 1", "описание задачи 1",
                10, 2022, 04, 04, 10, 00);
        inMemoryTaskManager.createTasks(task1);
        task1.setStatus(DONE);
        inMemoryTaskManager.refreshTasks(task1);
        assertEquals(DONE, task1.getStatus());
    }

    //14 Проверка изменения статуса подзадачи
    @Test
    void Test14_CheckingSubTaskStatusChanges() {
        Epic epic1 = new Epic("Эпик 1", "описание эпика 1");
        inMemoryTaskManager.createEpics(epic1);
        SubTask subTask1 = new SubTask("подзадача1", "описание подзадачи1", 40, 2022,
                04, 12, 12, 00, epic1.getId());
        inMemoryTaskManager.createSubTasks(subTask1);
        subTask1.setStatus(IN_PROGRESS);
        inMemoryTaskManager.refreshSubTasks(subTask1);
        assertEquals(IN_PROGRESS, subTask1.getStatus());
    }

    //15 Проверка удаления задачи
    @Test
    void Test15_CheckingTaskDeletion() {
        Task task1 = new Task("Задача 1", "описание задачи 1",
                10, 2022, 05, 05, 12, 00);
        Task task2 = new Task("Задача 2", "описание задачи 2",
                10, 2022, 06, 06, 13, 30);
        inMemoryTaskManager.createTasks(task1);
        inMemoryTaskManager.createTasks(task2);
        inMemoryTaskManager.clearTasks();
        assertTrue(inMemoryTaskManager.getAllTasks().isEmpty());
    }

    //16 Проверка удаления эпика
    @Test
    void Test16_CheckingEpicDeletion() {
        Epic epic1 = new Epic("Эпик 1", "описание эпика 1");
        Epic epic2 = new Epic("Эпик 2", "описание эпика 2");
        inMemoryTaskManager.createEpics(epic1);
        inMemoryTaskManager.createEpics(epic2);
        inMemoryTaskManager.clearEpics();
        assertTrue(inMemoryTaskManager.getAllEpics().isEmpty());
    }

    //17 Проверка удаления подзадачи
    @Test
    void Test17_CheckingSubTaskDeletion() {
        Epic epic1 = new Epic("Эпик 1", "описание эпика 1");
        inMemoryTaskManager.createEpics(epic1);
        SubTask subTask1 = new SubTask("подзадача1", "описание подзадачи1", 40, 2022,
                04, 12, 12, 00, epic1.getId());
        SubTask subTask2 = new SubTask("подзадача2", "описание подзадачи", 30, 2022,
                07, 12, 13, 00, epic1.getId());
        inMemoryTaskManager.createSubTasks(subTask1);
        inMemoryTaskManager.createSubTasks(subTask2);
        inMemoryTaskManager.clearSubTasks();
        assertTrue(inMemoryTaskManager.getAllSubTasks().isEmpty());
    }

    //18 Проверка удаления задачи по идентификатору
    @Test
    void Test18_DeletingATaskById() {
        Task task1 = new Task("Задача 1", "описание задачи 1",
                10, 2022, 03, 05, 14, 00);
        inMemoryTaskManager.createTasks(task1);
        inMemoryTaskManager.removeTaskById(0);
        assertFalse(inMemoryTaskManager.tasks.containsKey(0));
    }

    //18.1 Проверка удаления задачи по неверному идентификатору
    @Test
    void Test18_1_DeletingATaskByAnErroneousId() {
        Task task1 = new Task("Задача 1", "описание задачи 1",
                10, 2022, 03, 03, 15, 00);
        inMemoryTaskManager.createTasks(task1);
        inMemoryTaskManager.removeTaskById(3);
        assertTrue(inMemoryTaskManager.tasks.containsKey(0));
    }

    //19 Проверка удаления эпика по идентификатору
    @Test
    void Test19_DeletingAEpicById() {
        Epic epic1 = new Epic("Эпик 1", "описание эпика 1");
        inMemoryTaskManager.createEpics(epic1);
        inMemoryTaskManager.removeEpicById(0);
        assertFalse(inMemoryTaskManager.epics.containsKey(0));
    }

    //19.1 Проверка удаления эпика по неверному идентификатору
    @Test
    void Test19_1_DeletingAEpicByAnErroneousId() {
        Epic epic1 = new Epic("Эпик 1", "описание эпика 1");
        inMemoryTaskManager.createEpics(epic1);
        inMemoryTaskManager.removeEpicById(4);
        assertTrue(inMemoryTaskManager.epics.containsKey(0));
    }

    //20 Проверка удаления подзадачи по идентификатору
    @Test
    void Test20_DeletingASubTaskById() {
        Epic epic1 = new Epic("Эпик 1", "описание эпика 1");
        inMemoryTaskManager.createEpics(epic1);
        SubTask subTask1 = new SubTask("подзадача1", "описание подзадачи1", 40, 2022,
                04, 12, 12, 00, epic1.getId());
        inMemoryTaskManager.createSubTasks(subTask1);
        inMemoryTaskManager.removeSubTaskById(1);
        assertFalse(inMemoryTaskManager.subTasks.containsKey(1));
    }

    //20.1 Проверка удаления подзадачи по неверному идентификатору
    @Test
    void Test20_1_DeletingASubTaskByAnErroneousId() {
        Epic epic1 = new Epic("Эпик 1", "описание эпика 1");
        inMemoryTaskManager.createEpics(epic1);
        SubTask subTask1 = new SubTask("подзадача1", "описание подзадачи1", 40, 2022,
                04, 12, 12, 00, epic1.getId());
        inMemoryTaskManager.createSubTasks(subTask1);
        inMemoryTaskManager.removeSubTaskById(6);
        assertTrue(inMemoryTaskManager.subTasks.containsKey(1));
    }
}