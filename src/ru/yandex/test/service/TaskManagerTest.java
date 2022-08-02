package service;

import model.Epic;
import model.SubTask;
import model.Task;
import org.junit.jupiter.api.*;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static service.Status.*;
import static service.Status.IN_PROGRESS;

abstract class TaskManagerTest<T extends TaskManager> {

    TaskManager inMemoryTaskManager = new InMemoryTaskManager();

    @BeforeEach
    abstract void beforeEach();

    //1.расчет статуса эпика с пустым списком подзадач
    @Test
    void Test1_CalculationOfTheEpicStatusWithAnEmptyListOfSubtasks() {
        Epic epic1 = new Epic("Эпик 1", "описание эпика 1");
        inMemoryTaskManager.createEpics(epic1);
        assertEquals(NEW, epic1.getStatus(), "ожидается статус NEW");
    }

    //2.расчет статуса эпика. Все задачи со статусом NEW
    @Test
    void Test2_CalculationOfTheEpicStatusAllTasksWithTheStatusNew() throws IOException {
        Epic epic1 = new Epic("Эпик 1", "описание эпика 1");
        inMemoryTaskManager.createEpics(epic1);
        inMemoryTaskManager.createSubTasks(new SubTask("подзадача1",
                "описание подзадачи1", 40, 2022,
                04, 12, 12, 00, epic1.getId()));
        inMemoryTaskManager.createSubTasks(new SubTask("подзадача2",
                "описание подзадачи", 30, 2022,
                07, 12, 13, 00, epic1.getId()));
        assertEquals(NEW, epic1.getStatus(), "ожидается статус NEW");
    }

    //3.расчет статуса эпика. Все задачи со статусом DONE
    @Test
    void Test3_CalculationOfTheEpicStatusAllTasksWithTheStatusDone() throws IOException {
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
        assertEquals(DONE, epic1.getStatus(), "ожидается статус DONE");
    }

    //4.расчет статуса эпика. Подзадачи со статусами NEW и DONE
    @Test
    void Test4_CalculationOfTheEpicStatusSubtasksWithNewAndDoneStatuses() throws IOException {
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
        assertEquals(IN_PROGRESS, epic1.getStatus(), "ожидается статус IN_PROGRESS");
    }

    //5.расчет статуса эпика. Подзадачи со статусами IN_PROGRESS
    @Test
    void Test5_CalculationOfTheEpicStatusSubtasksWithIn_ProgressStatuses()  throws IOException{
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
        assertEquals(IN_PROGRESS, epic1.getStatus(), "ожидается статус IN_PROGRESS");
    }

    //6. Проверка наличия эпика для подзадачи
    @Test
    void Test6_CheckingForThePresenceOfAnEpicInASubtask()  throws IOException{
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

    //7. Проверка записи задачи в мапу (метод createTasks)
    @Test
    void Test7_CreatingAndWritingATaskToTheMap() {
        inMemoryTaskManager.createTasks(new Task("Задача 1", "описание задачи 1",
                10, 2022, 07, 07, 20, 20));
        assertEquals(1, inMemoryTaskManager.getAllTasks().size());
        //assertEquals(1, inMemoryTaskManager.tasks.size());
    }

    //8. Проверка записи эпика в мапу (метод  createEpics)
    @Test
    void Test8_CreatingAndWritingAEpicToTheMap() {
        inMemoryTaskManager.createEpics(new Epic("Эпик 1", "описание эпика 1"));
        assertEquals(1, inMemoryTaskManager.getAllEpics().size());
       // assertEquals(1, inMemoryTaskManager.epics.size());
    }

    //9. Проверка записи подзадачи в мапу (метод createSubTasks)
    @Test
    void Test9_CreatingAndWritingASubTaskToTheMap() throws IOException{
        Epic epic1 = new Epic("Эпик 1", "описание эпика 1");
        inMemoryTaskManager.createEpics(epic1);
        inMemoryTaskManager.createSubTasks(new SubTask("подзадача1",
                "описание подзадачи1", 40, 2022,
                04, 12, 12, 00, epic1.getId()));
        assertEquals(1, inMemoryTaskManager.getAllSubTasks().size());
       // assertEquals(1, inMemoryTaskManager.subTasks.size());
    }

    //10.получение пустого списка сортировки (метод getPrioritizedTasks)
    @Test
    void Test10_checkingToGetAnEmptyPrioritySortingList() {
        inMemoryTaskManager.getPrioritizedTasks();
        assertEquals(0,inMemoryTaskManager.getPrioritizedTasks().size());
        //assertEquals(0,inMemoryTaskManager.priorityTasks.size());
    }

    //11.получение непустого списка сортировки (метод getPrioritizedTasks)
    @Test
    void Test11_checkingForGettingAPrioritySortingList() throws IOException{
        Task task1 = new Task("Задача 1", "описание задачи 1",
                10, 2022, 07, 15, 20, 21);
        inMemoryTaskManager.createTasks(task1);
        Epic epic1 = new Epic("Эпик 1", "описание эпика 1");
        inMemoryTaskManager.createEpics(epic1);
        SubTask subTask1 = new SubTask("подзадача1", "описание подзадачи1", 40, 2022,
                04, 12, 12, 00, epic1.getId());
        inMemoryTaskManager.createSubTasks(subTask1);
        inMemoryTaskManager.getPrioritizedTasks();
        assertEquals(2,inMemoryTaskManager.getPrioritizedTasks().size());
        //assertEquals(2,inMemoryTaskManager.priorityTasks.size());
    }

    //12. Проверка получения задачи по идентификатору (метод getTaskById)
    @Test
    void Test12_GettingATaskById() {
        Task task1 = new Task("Задача 1", "описание задачи 1",
                10, 2022, 07, 15, 20, 21);
        inMemoryTaskManager.createTasks(task1);
        assertEquals(0, task1.getId());
    }

    //12.1 Проверка получения задачи по неверному идентификатору (метод getTaskById)
    @Test
    void Test12_1_GettingATaskByAnErroneousId() {
        Task task1 = new Task("Задача 1", "описание задачи 1",
                10, 2022, 07, 01, 20, 21);
        inMemoryTaskManager.createTasks(task1);
        Task taskId = inMemoryTaskManager.getTaskById(1);
        //Task taskId = inMemoryTaskManager.tasks.get(1);
        Assertions.assertNull(taskId);
    }

    //13. Проверка получения эпика по идентификатору (метод getEpicById)
    @Test
    void Test13_GettingAEpicById() {
        Epic epic1 = new Epic("Эпик 1", "описание эпика 1");
        inMemoryTaskManager.createEpics(epic1);
        assertEquals(0, epic1.getId());
    }

    //13.1 Проверка получения эпика по неверному идентификатору (метод getEpicById)
    @Test
    void Test13_1_GettingAEpicByAnErroneousId() {
        Epic epic1 = new Epic("Эпик 1", "описание эпика 1");
        inMemoryTaskManager.createEpics(epic1);
        Epic epicId = inMemoryTaskManager.getEpicById(5);
        Assertions.assertNull(epicId);
    }

    //14. Проверка получения подзадачи по идентификатору (метод getSubTaskById)
    @Test
    void Test14_GettingASubTaskById() throws IOException{
        Epic epic1 = new Epic("Эпик 1", "описание эпика 1");
        inMemoryTaskManager.createEpics(epic1);
        SubTask subTask1 = new SubTask("подзадача1", "описание подзадачи1", 40, 2022,
                04, 12, 12, 00, epic1.getId());
        inMemoryTaskManager.createSubTasks(subTask1);
        assertEquals(1, subTask1.getId());
    }

    //14.1 Проверка получения подзадачи по неверному идентификатору (метод getSubTaskById)
    @Test
    void Test14_1_GettingASubTaskByAnErroneousId() throws IOException{
        Epic epic1 = new Epic("Эпик 1", "описание эпика 1");
        inMemoryTaskManager.createEpics(epic1);
        SubTask subTask1 = new SubTask("подзадача1", "описание подзадачи1", 40, 2022,
                04, 12, 12, 00, epic1.getId());
        inMemoryTaskManager.createSubTasks(subTask1);
        SubTask subTaskId = inMemoryTaskManager.getSubTaskById(5);
        Assertions.assertNull(subTaskId);
    }

    //15 Проверка изменения статуса задачи (метод refreshTasks)
    @Test
    void Test15_CheckingTaskStatusChanges() {
        Task task1 = new Task("Задача 1", "описание задачи 1",
                10, 2022, 04, 04, 10, 00);
        inMemoryTaskManager.createTasks(task1);
        task1.setStatus(DONE);
        inMemoryTaskManager.refreshTasks(task1);
        assertEquals(DONE, task1.getStatus());
    }

    //16 Проверка изменения статуса подзадачи (метод refreshSubTasks)
    @Test
    void Test16_CheckingSubTaskStatusChanges() throws IOException{
        Epic epic1 = new Epic("Эпик 1", "описание эпика 1");
        inMemoryTaskManager.createEpics(epic1);
        SubTask subTask1 = new SubTask("подзадача1", "описание подзадачи1", 40, 2022,
                04, 12, 12, 00, epic1.getId());
        inMemoryTaskManager.createSubTasks(subTask1);
        subTask1.setStatus(IN_PROGRESS);
        inMemoryTaskManager.refreshSubTasks(subTask1);
        assertEquals(IN_PROGRESS, subTask1.getStatus());
    }

    //17 Проверка удаления задач (метод clearTasks)
    @Test
    void Test17_CheckingTaskDeletion() {
        Task task1 = new Task("Задача 1", "описание задачи 1",
                10, 2022, 05, 05, 12, 00);
        Task task2 = new Task("Задача 2", "описание задачи 2",
                10, 2022, 06, 06, 13, 30);
        inMemoryTaskManager.createTasks(task1);
        inMemoryTaskManager.createTasks(task2);
        inMemoryTaskManager.clearTasks();
        assertTrue(inMemoryTaskManager.getAllTasks().isEmpty());
    }

    //18 Проверка удаления эпиков (метод clearEpic)
    @Test
    void Test18_CheckingEpicDeletion() {
        Epic epic1 = new Epic("Эпик 1", "описание эпика 1");
        Epic epic2 = new Epic("Эпик 2", "описание эпика 2");
        inMemoryTaskManager.createEpics(epic1);
        inMemoryTaskManager.createEpics(epic2);
        inMemoryTaskManager.clearEpics();
        assertTrue(inMemoryTaskManager.getAllEpics().isEmpty());
    }

    //19 Проверка удаления подзадач (метод clearSubTask)
    @Test
    void Test19_CheckingSubTaskDeletion() throws IOException{
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

    //20 Проверка удаления задачи по идентификатору (removeTaskById)
    @Test
    void Test20_DeletingATaskById() {
        Task task1 = new Task("Задача 1", "описание задачи 1",
                10, 2022, 03, 05, 14, 00);
        inMemoryTaskManager.createTasks(task1);
        inMemoryTaskManager.removeTaskById(0);
        assertFalse(inMemoryTaskManager.getAllTasks().containsKey(0));
    }

    //20.1 Проверка удаления задачи по неверному идентификатору (removeEpicById)
    @Test
    void Test20_1_DeletingATaskByAnErroneousId() {
        Task task1 = new Task("Задача 1", "описание задачи 1",
                10, 2022, 03, 03, 15, 00);
        inMemoryTaskManager.createTasks(task1);
        inMemoryTaskManager.removeTaskById(3);
        assertTrue(inMemoryTaskManager.getAllTasks().containsKey(0));
    }

    //21 Проверка удаления эпика по идентификатору (removeEpicById)
    @Test
    void Test21_DeletingAEpicById() {
        Epic epic1 = new Epic("Эпик 1", "описание эпика 1");
        inMemoryTaskManager.createEpics(epic1);
        inMemoryTaskManager.removeEpicById(0);
        assertFalse(inMemoryTaskManager.getAllEpics().containsKey(0));
    }

    //21.1 Проверка удаления эпика по неверному идентификатору (removeEpicById)
    @Test
    void Test21_1_DeletingAEpicByAnErroneousId() {
        Epic epic1 = new Epic("Эпик 1", "описание эпика 1");
        inMemoryTaskManager.createEpics(epic1);
        inMemoryTaskManager.removeEpicById(4);
        assertTrue(inMemoryTaskManager.getAllEpics().containsKey(0));
    }

    //22 Проверка удаления подзадачи по идентификатору (removeSubTaskById)
    @Test
    void Test22_DeletingASubTaskById() throws IOException{
        Epic epic1 = new Epic("Эпик 1", "описание эпика 1");
        inMemoryTaskManager.createEpics(epic1);
        SubTask subTask1 = new SubTask("подзадача1", "описание подзадачи1", 40, 2022,
                04, 12, 12, 00, epic1.getId());
        inMemoryTaskManager.createSubTasks(subTask1);
        inMemoryTaskManager.removeSubTaskById(1);
        assertFalse(inMemoryTaskManager.getAllSubTasks().containsKey(1));
    }

    //22.1 Проверка удаления подзадачи по неверному идентификатору (метод removeSubTaskById)
    @Test
    void Test22_1_DeletingASubTaskByAnErroneousId() throws IOException{
        Epic epic1 = new Epic("Эпик 1", "описание эпика 1");
        inMemoryTaskManager.createEpics(epic1);
        SubTask subTask1 = new SubTask("подзадача1", "описание подзадачи1", 40, 2022,
                04, 12, 12, 00, epic1.getId());
        inMemoryTaskManager.createSubTasks(subTask1);
        inMemoryTaskManager.removeSubTaskById(6);
        assertTrue(inMemoryTaskManager.getAllSubTasks().containsKey(1));
    }

    //23.проверка исключения при валидации
    @Test
    void Test23_CheckingTheRecoveryOfAnEpicFromAFile() throws RuntimeException {
        TimeTaskException thrown = assertThrows(TimeTaskException.class, () -> {
            Task task1 = new Task("Задача 1", "описание задачи 1",
                    60, 2022, 05, 05, 12, 00);
            inMemoryTaskManager.createTasks(task1);
            Task task2 = new Task("Задача 2", "описание задачи 2",
                    10, 2022, 05, 05, 12, 30);
            inMemoryTaskManager.createTasks(task2);
        }, "ожидалась ошибка TimeTaskException");
        assertNotNull(thrown.getMessage());
    }
}