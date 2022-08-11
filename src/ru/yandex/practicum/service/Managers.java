package service;

import server.HTTPTaskManager;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

public class Managers {

    public static TaskManager getDefault() throws IOException, URISyntaxException {
        return new HTTPTaskManager(new URL("http://localhost:8078/load/tasks"));
    }

    public static FileBackedTasksManager getDefaultFileBacked() throws IOException, URISyntaxException {
        return new HTTPTaskManager(new URL("http://localhost:8078/load/tasks"));
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

}

