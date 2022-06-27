package ru.yandex.practicum.service;

import java.io.File;


public class Managers {


    public static TaskManager getDefault() {
        return new FileBackedTasksManager(new File("csvSave.csv"));
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

}

