package service;

import server.HTTPTaskManager;

public class Managers {

    public static TaskManager getDefault() {
        return new HTTPTaskManager("http://localhost:8078/", false);
    }

    public static FileBackedTasksManager getDefaultFileBacked() {
        return new HTTPTaskManager("http://localhost:8078/load/tasks", false);
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

}

