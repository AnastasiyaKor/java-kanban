package model;

import service.Status;

import java.util.Objects;

public class SubTask extends Task {
    private int epicId;

    public SubTask(String name, String description, String startTime, long duration) {
        super(name, description, duration, startTime);
    }

    public SubTask(String name, String description, long duration, String startTime, int epicId) {
        super(name, description, duration, startTime);
        this.epicId = epicId;
    }

    public SubTask(String name, String description, String status, int id, String startTime,
                   long duration, int epicId) {
        super(name, description, status, id, startTime, duration);
        this.epicId = epicId;
    }

    public SubTask(String name, String description, int epicId) {
        super(name, description, String.valueOf(Status.NEW));
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        SubTask subTask = (SubTask) o;
        return epicId == subTask.epicId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), epicId);
    }

    @Override
    public String toString() {
        return "SubTask{" +
                "epicId=" + epicId +
                "} " + super.toString();
    }
}
