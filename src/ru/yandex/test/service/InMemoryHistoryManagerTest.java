package service;

import model.Epic;
import model.Task;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {

    //обновление пустой истории
    @Test
    void UpdatingAnEmptyTaskHistory() {
        InMemoryHistoryManager inMemoryHistoryManager = new InMemoryHistoryManager();
        InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();
        Task task1 = new Task("task1", "описание задачи",
                20, 2022, 12, 12, 16, 00);
        inMemoryTaskManager.createTasks(task1);
        inMemoryHistoryManager.add(task1);
        inMemoryHistoryManager.remove(0);
        assertEquals(0, inMemoryHistoryManager.getHistory().size());
    }

    //обновление  истории с дублем
    @Test
    void UpdatingTheHistoryOfTasksWithDuplicates() {
        InMemoryHistoryManager inMemoryHistoryManager = new InMemoryHistoryManager();
        InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();
        Task task1 = new Task("task1", "описание задачи",
                50, 2022, 12, 12, 16, 00);
        inMemoryTaskManager.createTasks(task1);
        inMemoryHistoryManager.add(task1);
        inMemoryHistoryManager.add(task1);
        assertEquals(1, inMemoryHistoryManager.getHistory().size());
    }

    //обновление истории с 2 задачами
    @Test
    void UpdatingTheHistoryWithTwoTasks() {
        InMemoryHistoryManager inMemoryHistoryManager = new InMemoryHistoryManager();
        InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();
        Task task1 = new Task("task1", "описание задачи",
                20, 2022, 12, 12, 17, 00);
        inMemoryTaskManager.createTasks(task1);
        Epic epic1 = new Epic("epic", "описание эпика");
        inMemoryTaskManager.createEpics(epic1);
        inMemoryHistoryManager.add(task1);
        inMemoryHistoryManager.add(epic1);
        assertEquals(2, inMemoryHistoryManager.getHistory().size());
    }

}