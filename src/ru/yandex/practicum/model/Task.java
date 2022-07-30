package model;

import service.Status;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

public class Task implements Comparable<Task> {
    private String name;
    private String description;
    private Status status;
    private int id;
    private LocalDateTime startTime;
    private long duration;

    public Task(String name, String description, long duration, int year,
                int month, int day, int hours, int minutes) {
        this.name = name;
        this.description = description;
        this.duration = duration;
        this.status = Status.NEW;
        this.startTime = LocalDateTime.of(year,month, day, hours, minutes);
    }

    public Task(String name, String description, String status) {
        this.name = name;
        this.description = description;
        this.status = Status.valueOf(status);
        this.startTime = LocalDateTime.MAX;
        this.duration = 0;
    }

    public Task(String name, String description, String status, int id) {
        this.name = name;
        this.description = description;
        this.status = Status.valueOf(status);
        this.id = id;
    }

    public Task(String name, String description, String status,
                int id, String startTime, long duration) {
        this.name = name;
        this.description = description;
        this.status = Status.valueOf(status);
        this.id = id;
        this.startTime = LocalDateTime.parse(startTime);
        this.duration = duration;
    }

    @Override
    public int compareTo(Task other) {
        return this.startTime.compareTo(other.startTime);
    }


    public LocalDateTime getEndTime(){
        Duration durationTask = Duration.ofMinutes(duration);
        LocalDateTime endTime = startTime.plus(durationTask);
        return endTime;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return Objects.equals(name, task.name) && Objects.equals(description, task.description) &&
                Objects.equals(status, task.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, status);
    }

    @Override
    public String toString() {
        return "Task{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", id=" + id +
                ", startTime=" + startTime +
                ", duration=" + duration +
                '}';
    }

}
