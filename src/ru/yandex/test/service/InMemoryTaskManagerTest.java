package service;

import org.junit.jupiter.api.BeforeEach;

class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {
    InMemoryTaskManager inMemoryTaskManager;

    @Override
    @BeforeEach
    void beforeEach() {
        inMemoryTaskManager = new InMemoryTaskManager();
    }
}