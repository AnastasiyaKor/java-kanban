package service;

import model.Epic;
import model.Task;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {

    //1.обновление пустой истории
    @Test
    void Test1_UpdatingAnEmptyTaskHistory() {
        InMemoryHistoryManager inMemoryHistoryManager = new InMemoryHistoryManager();
        InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();
        Task task1 = new Task("task1", "описание задачи", 30,
                "2022-10-15T16:00:00");
        inMemoryTaskManager.createTasks(task1);
        inMemoryHistoryManager.add(task1);
        inMemoryHistoryManager.remove(0);
        assertEquals(0, inMemoryHistoryManager.getHistory().size());
    }

    //2.обновление  истории с дублем
    @Test
    void Test2_UpdatingTheHistoryOfTasksWithDuplicates() {
        InMemoryHistoryManager inMemoryHistoryManager = new InMemoryHistoryManager();
        InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();
        Task task1 = new Task("task1", "описание задачи", 30,
                "2022-10-15T23:00:00");
        inMemoryTaskManager.createTasks(task1);
        inMemoryHistoryManager.add(task1);
        inMemoryHistoryManager.add(task1);
        assertEquals(1, inMemoryHistoryManager.getHistory().size());
    }

    //3.обновление истории с 2 задачами
    @Test
    void Test3_UpdatingTheHistoryWithTwoTasks() {
        InMemoryHistoryManager inMemoryHistoryManager = new InMemoryHistoryManager();
        InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();
        Task task1 = new Task("task1", "описание задачи", 30,
                "2022-10-15T22:00:00");
        inMemoryTaskManager.createTasks(task1);
        Epic epic1 = new Epic("epic", "описание эпика");
        inMemoryTaskManager.createEpics(epic1);
        inMemoryHistoryManager.add(task1);
        inMemoryHistoryManager.add(epic1);
        assertEquals(2, inMemoryHistoryManager.getHistory().size());
    }
}