package service;

import model.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {

    protected final HistoryManager historyManager = Managers.getDefaultHistory();
    protected final HashMap<Integer, Task> tasks;
    protected final HashMap<Integer, Epic> epics;
    protected final HashMap<Integer, SubTask> subTasks;
    protected final Map<LocalDateTime, Task> priorityTasks;
    protected int id = 0;

    public InMemoryTaskManager() {
        this.tasks = new HashMap<>();
        this.epics = new HashMap<>();
        this.subTasks = new HashMap<>();
        this.priorityTasks = new TreeMap<>();
    }

    //вызов сортировки
    @Override
    public Map<LocalDateTime, Task> getPrioritizedTasks() {
        for (Integer i : tasks.keySet()) {
            if (!(tasks.get(i).getStartTime() == null)) {
                priorityTasks.put(tasks.get(i).getStartTime(), tasks.get(i));
            }
        }
        for (Integer i : subTasks.keySet()) {
            if (!(subTasks.get(i).getStartTime() == null)) {
                priorityTasks.put(subTasks.get(i).getStartTime(), subTasks.get(i));
            }
        }
        return priorityTasks;
    }

    //получение истории
    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    //проверка пересечений
    public void checkingForIntersectionsOfTasks(Task task) {
        LocalDateTime addStartTime = task.getStartTime();
        LocalDateTime addEndTime = task.getEndTime();
        for (Task existTask : priorityTasks.values()) {
            LocalDateTime endTimeExistTask = existTask.getEndTime();
            LocalDateTime startTimeExistTask = existTask.getStartTime();
            if (addEndTime.isBefore(startTimeExistTask) || addEndTime.isEqual(startTimeExistTask)) {
                continue;
            }
            if (addStartTime.isAfter(endTimeExistTask) || addStartTime.isEqual(endTimeExistTask)) {
                continue;
            }
            if (task.getId() == existTask.getId()) {
                continue;
            }
            throw new TimeTaskException(task + " пересекается с началом времени задачи: " + existTask);
        }
    }

    //    создание задач, эпиков, подзадач
    @Override
    public void createTasks(Task task) {
        task.setId(id);
        id++;
        getPrioritizedTasks();
        checkingForIntersectionsOfTasks(task);
        tasks.put(task.getId(), task);
    }

    @Override
    public void createEpics(Epic epic) {
        epic.setId(id);
        id++;
        epics.put(epic.getId(), epic);
    }

    @Override
    public void createSubTasks(SubTask subTask) {
        subTask.setId(id);
        id++;
        getPrioritizedTasks();
        checkingForIntersectionsOfTasks(subTask);
        if (epics.containsKey(subTask.getEpicId())) {
            subTasks.put(subTask.getId(), subTask);
            getSubtasksByEpicId(subTask.getEpicId()).add(subTask.getId());
            durationEpic(subTask.getEpicId());
            startTimeEpic(subTask.getEpicId());
            endTimeEpic(subTask.getEpicId());
            refreshStatusEpic(subTask.getEpicId());
        }
    }

    //    получение списка задач, эпиков, подзадач
    @Override
    public HashMap<Integer, Task> getAllTasks() {
        for (Task t : tasks.values()) {
            System.out.println(t);
        }
        return new HashMap<>(tasks);
    }

    @Override
    public HashMap<Integer, Epic> getAllEpics() {
        for (Epic e : epics.values()) {
            System.out.println(e);
        }
        return new HashMap<>(epics);
    }

    @Override
    public HashMap<Integer, SubTask> getAllSubTasks() {
        for (SubTask t : subTasks.values()) {
            System.out.println(t);
        }
        return new HashMap<>(subTasks);
    }

    //    получение задач, эпиков, подзадач по идентификатору
    @Override
    public Task getTaskById(int id) {
        if (tasks.get(id) != null) {
            historyManager.add(tasks.get(id));
        }
        return tasks.get(id);
    }

    @Override
    public Epic getEpicById(int id) {
        if (epics.get(id) != null) {
            historyManager.add(epics.get(id));
        }
        return epics.get(id);
    }

    @Override
    public SubTask getSubTaskById(int id) {
        if (subTasks.get(id) != null) {
            historyManager.add(subTasks.get(id));
        }
        return subTasks.get(id);
    }

    public ArrayList<Integer> getSubtasksByEpicId(int epicId) {
        if (epics.get(epicId) != null) {
            return epics.get(epicId).getSubTasksId();
        } else {
            return null;
        }
    }

    //    обновление задач, эпиков, подзадач
    @Override
    public void refreshTasks(Task task) {
        if (tasks.containsKey(task.getId())) {
            priorityTasks.remove(task.getStartTime());
            tasks.put(task.getId(), task);
            checkingForIntersectionsOfTasks(task);
        }
        checkingForIntersectionsOfTasks(task);
    }

    @Override
    public void refreshEpics(Epic epic) {
        if (epics.containsKey(epic.getId())) {
            epics.put(epic.getId(), epic);
        }
    }

    @Override
    public void refreshSubTasks(SubTask subTask) {
        if (subTasks.containsKey(subTask.getId())) {
            priorityTasks.remove(subTask.getStartTime());
            subTasks.put(subTask.getId(), subTask);
            refreshStatusEpic(subTask.getEpicId());
            refreshStartTimeEpic(subTask.getEpicId());
            durationEpic(subTask.getEpicId());
            endTimeEpic(subTask.getEpicId());
            checkingForIntersectionsOfTasks(subTask);
        }
    }

    //    обновление статуса эпика
    public void refreshStatusEpic(int epicId) {
        Epic epic = epics.get(epicId);
        int newStatus = 0;
        int done = 0;
        if (getSubtasksByEpicId(epicId) != null) {
            for (Integer i : getSubtasksByEpicId(epicId)) {
                if (subTasks.get(i).getStatus().equals(Status.NEW)) {
                    newStatus++;
                } else if (subTasks.get(i).getStatus().equals(Status.DONE)) {
                    done++;
                } else {
                    epic.setStatus(Status.IN_PROGRESS);
                    break;
                }
            }
            if (getSubtasksByEpicId(epicId).size() == newStatus) {
                epic.setStatus(Status.NEW);
            } else if (getSubtasksByEpicId(epicId).size() == done) {
                epic.setStatus(Status.DONE);
            } else {
                epic.setStatus(Status.IN_PROGRESS);
            }
        }
    }

    // продолжительность эпика
    public void durationEpic(int epicId) {
        Epic epic = epics.get(epicId);
        long duration1;
        long sum = 0;
        if (getSubtasksByEpicId(epicId) != null) {
            for (Integer i : getSubtasksByEpicId(epicId)) {
                duration1 = subTasks.get(i).getDuration();
                sum = sum + duration1;
            }
            epic.setDuration(sum);
        }
    }

    //РЕФРЕШ даты начала эпика
    public void refreshStartTimeEpic(int epicId) {
        Epic epic = epics.get(epicId);
        LocalDateTime startEpic = LocalDateTime.MAX;
        if (getSubtasksByEpicId(epicId) != null) {
            for (Integer i : getSubtasksByEpicId(epicId)) {
                if (subTasks.get(i).getStartTime().isBefore(startEpic)) {
                    startEpic = subTasks.get(i).getStartTime();
                }
            }
            epic.setStartTime(startEpic);
        }
    }

    // дата начала эпика
    public void startTimeEpic(int epicId) {
        Epic epic = epics.get(epicId);
        LocalDateTime startEpic = epic.getStartTime();
        if (getSubtasksByEpicId(epicId) != null) {
            for (Integer i : getSubtasksByEpicId(epicId)) {
                if (subTasks.get(i).getStartTime().isBefore(startEpic)) {
                    startEpic = subTasks.get(i).getStartTime();
                }
            }
            epic.setStartTime(startEpic);
        }
    }

    //дата окончания эпика
    public void endTimeEpic(int epicId) {
        Epic epic = epics.get(epicId);
        LocalDateTime startEpic = epic.getStartTime();
        long durationSubTask = 0;
        if (getSubtasksByEpicId(epicId) != null) {
            for (Integer i : getSubtasksByEpicId(epicId)) {
                if (subTasks.get(i).getStartTime().isAfter(startEpic)) {
                    startEpic = subTasks.get(i).getStartTime();
                    durationSubTask = subTasks.get(i).getDuration();
                }
            }
            LocalDateTime maxSubTaskTime = startEpic;
            long maxSubTask = durationSubTask;
            Duration maxDurationSubTask = Duration.ofMinutes(maxSubTask);
            epic.setEndTime(maxSubTaskTime.plus(maxDurationSubTask));
        }
    }

    //    удаление задач, эпиков, подзадач
    @Override
    public void clearTasks() {
        for (Integer id : tasks.keySet()) {
            historyManager.remove(id);
            priorityTasks.remove(tasks.get(id).getStartTime());
        }
        tasks.clear();
    }

    @Override
    public void clearEpics() {
        for (Integer id : epics.keySet()) {
            historyManager.remove(id);
        }
        for (Integer id : subTasks.keySet()) {
            historyManager.remove(id);
        }
        epics.clear();
        subTasks.clear();
    }


    @Override
    public void clearSubTasks() {
        for (Integer id : subTasks.keySet()) {
            historyManager.remove(id);
            priorityTasks.remove(subTasks.get(id).getStartTime());
        }
        subTasks.clear();
        for (Epic epic : epics.values()) {
            epic.setStatus(Status.NEW);
            epic.getSubTasksId().clear();
        }
    }

    //    удаление задач, эпиков, подзадач по  идентификатору
    @Override
    public void removeTaskById(int id) {
        if (tasks.get(id) != null) {
            priorityTasks.remove(tasks.get(id).getStartTime());
            tasks.remove(id);
            historyManager.remove(id);
        }
    }

    @Override
    public void removeEpicById(int id) {
        if (getSubtasksByEpicId(id) != null) {
            for (Integer del : getSubtasksByEpicId(id)) {
                subTasks.remove(del);
                historyManager.remove(del);
            }
            epics.remove(id);
            historyManager.remove(id);
        }
    }


    @Override
    public void removeSubTaskById(int id) {
        if (subTasks.get(id) != null) {
            if (getSubtasksByEpicId(subTasks.get(id).getEpicId()) != null) {
                getSubtasksByEpicId(subTasks.get(id).getEpicId()).remove((Integer) id);
                refreshStartTimeEpic(subTasks.get(id).getEpicId());
                durationEpic(subTasks.get(id).getEpicId());
                endTimeEpic(subTasks.get(id).getEpicId());
                refreshStatusEpic(subTasks.get(id).getEpicId());
                priorityTasks.remove(subTasks.get(id).getStartTime());
                subTasks.remove(id);

                historyManager.remove(id);
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InMemoryTaskManager that = (InMemoryTaskManager) o;
        return id == that.id && Objects.equals(historyManager, that.historyManager)
                && Objects.equals(tasks, that.tasks)
                && Objects.equals(epics, that.epics) && Objects.equals(subTasks, that.subTasks);
    }

    @Override
    public int hashCode() {
        return Objects.hash(historyManager, tasks, epics, subTasks, id);
    }

    @Override
    public String toString() {
        return "InMemoryTaskManager{" +
                "historyManager=" + historyManager +
                ", tasks=" + tasks +
                ", epics=" + epics +
                ", subTasks=" + subTasks +
                ", id=" + id +
                '}';
    }
}
