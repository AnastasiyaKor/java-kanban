package model;

import service.Status;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Objects;

public class Epic extends Task {

    private ArrayList<Integer> subTasksId;
    private LocalDateTime endTime;


    public Epic(String name, String description, Status status, int id, String startTime,
                long duration) {
        super(name, description, String.valueOf(status), id, startTime, duration);
        this.subTasksId = new ArrayList<>();
    }

    public Epic(String name, String description, String status, int id, String startTime,
                long duration, ArrayList<Integer> subTasksId) {
        super(name, description, status, id, startTime, duration);
        this.subTasksId = new ArrayList<>();
    }

    public Epic(String name, String description, long duration, String startTime) {
        super(name, description, duration, startTime);
        this.subTasksId = new ArrayList<>();
    }

    public Epic(String name, String description, Status status, int id, long duration, String startTime) {
        super(name, description, String.valueOf(Status.NEW));
        this.subTasksId = new ArrayList<>();
    }


    @Override
    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public Epic(String name, String description) {
        super(name, description, String.valueOf(Status.NEW));
        this.subTasksId = new ArrayList<>();
    }

    public Epic(String name, String description, String status, int id, String startTime,
                long duration) {
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
        return Objects.equals(subTasksId, epic.subTasksId) && Objects.equals(endTime, epic.endTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), subTasksId, endTime);
    }

    @Override
    public String toString() {
        return "Epic{" +
                "subTasksId=" + subTasksId +
                "} " + super.toString();
    }
}


