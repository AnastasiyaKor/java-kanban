package ru.yandex.practicum.model;

import ru.yandex.practicum.service.*;

import java.util.ArrayList;
import java.util.Objects;

public class Epic extends Task {
    private ArrayList<Integer> subTasksId;

    public Epic(String name, String description) {
        super(name, description, String.valueOf(Status.NEW));
        this.subTasksId = new ArrayList<>();
    }

    public Epic(String name, String description, String status, int id) {

        super(name, description, status, id);
        this.subTasksId = new ArrayList<>();
    }

    public ArrayList<Integer> getSubTasksId() {
        return subTasksId;
    }

    public void setSubTasksId(ArrayList<Integer> subTasksId) {

        this.subTasksId = subTasksId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Epic epic = (Epic) o;
        return Objects.equals(subTasksId, epic.subTasksId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), subTasksId);
    }

    @Override
    public String toString() {
        return "Epic{" +
                "} " + super.toString();
    }
}


