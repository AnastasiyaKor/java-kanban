package ru.yandex.practicum.service;

import ru.yandex.practicum.model.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

class InMemoryHistoryManager implements HistoryManager {
    private List<Task> viewedTasks = new ArrayList<>();
    private int MAXI_ITEMS_IN_THE_LIST = 10;

    // просмотр истории
    @Override
    public List<Task> getHistory() {
        return viewedTasks;
    }

    //обновление истории
    @Override
    public void add(Task task) {
        if (viewedTasks.size() == MAXI_ITEMS_IN_THE_LIST) {
            viewedTasks.remove(0);
        }
        viewedTasks.add(task);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InMemoryHistoryManager that = (InMemoryHistoryManager) o;
        return MAXI_ITEMS_IN_THE_LIST == that.MAXI_ITEMS_IN_THE_LIST && viewedTasks.equals(that.viewedTasks);
    }

    @Override
    public int hashCode() {
        return Objects.hash(MAXI_ITEMS_IN_THE_LIST, viewedTasks);
    }

    @Override
    public String toString() {
        return "ru.yandex.practicum.service.InMemoryHistoryManager{" +
                "viewedTasks=" + viewedTasks +
                '}';
    }
}
