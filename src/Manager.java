import java.util.ArrayList;
import java.util.HashMap;

public class Manager {
    private final HashMap<Integer, Task> tasks;
    private final HashMap<Integer, Epic> epics;
    private final HashMap<Integer, SubTask> subTasks;
    private int id = 0;

    public Manager() {
        this.tasks = new HashMap<>();
        this.epics = new HashMap<>();
        this.subTasks = new HashMap<>();
    }

    //    создание задач, эпиков, подзадач
    public void createTasks(Task task) {
        task.setId(id);
        id++;
        tasks.put(task.getId(), task);
    }

    public void createEpics(Epic epic) {
        epic.setId(id);
        id++;
        epics.put(epic.getId(), epic);
    }

    public void createSubTasks(SubTask subTask) {
        subTask.setId(id);
        id++;
        if (epics.get(id) != null) {
            subTasks.put(subTask.getId(), subTask);
            getSubtasksByEpicId(subTask.getEpicId()).add(subTask.getId());
            refreshStatusEpic(subTask.getEpicId());
        }
    }


    //    получение списка задач, эпиков, подзадач
    public HashMap<Integer, Task> getAllTasks() {
        for (Task t : tasks.values()) {
            System.out.println(t);
        }
        return new HashMap<>(tasks);
    }

    public HashMap<Integer, Epic> getAllEpics() {
        for (Epic e : epics.values()) {
            System.out.println(e);
        }
        return new HashMap<>(epics);
    }

    public HashMap<Integer, SubTask> getAllSubTasks() {
        for (SubTask t : subTasks.values()) {
            System.out.println(t);
        }
        return new HashMap<>(subTasks);
    }

    //    получение задач, эпиков, подзадач по идентификатору
    public Task getTaskById(int id) {
        return tasks.get(id);
    }

    public Epic getEpicById(int id) {
        return epics.get(id);
    }

    public SubTask getSubTaskById(int id) {
        return subTasks.get(id);
    }

    public ArrayList<Integer> getSubtasksByEpicId(int epicId) {
        if (getEpicById(epicId) != null) {
            return getEpicById(epicId).getSubTasksId();
        } else {
            return null;
        }
    }

    //    обновление задач, эпиков, подзадач
    public void refreshTasks(Task task) {
        if (tasks.containsKey(task.getId())) {
            tasks.put(task.getId(), task);
        }
    }

    public void refreshEpics(Epic epic) {
        if (epics.containsKey(epic.getId())) {
            epics.put(epic.getId(), epic);
        }
    }

    public void refreshSubTasks(SubTask subTask) {
        if (subTasks.containsKey(subTask.getId())) {
            subTasks.put(subTask.getId(), subTask);
            refreshStatusEpic(subTask.getEpicId());
        }
    }

    //    обновление статуса эпика
    public void refreshStatusEpic(int id) {
        Epic epic = getEpicById(id);
        int newStatus = 0;
        int done = 0;
        if (getSubtasksByEpicId(id) != null) {
            for (SubTask i : subTasks.values()) {
                if (i.getEpicId() == id) {
                    if (i.getStatus().equals(Status.NEW)) {
                        newStatus++;
                    } else if (i.getStatus().equals(Status.DONE)) {
                        done++;
                    } else {
                        epic.setStatus(String.valueOf(Status.IN_PROGRESS));
                    }
                }
            }
            if (getSubtasksByEpicId(id).size() == newStatus) {
                epic.setStatus(String.valueOf(Status.NEW));
            } else if (getSubtasksByEpicId(id).size() == done) {
                epic.setStatus(String.valueOf(Status.DONE));
            } else {
                epic.setStatus(String.valueOf(Status.IN_PROGRESS));
            }
        }
    }

    //    удаление задач, эпиков, подзадач
    public void clearTasks() {
        tasks.clear();
    }

    public void clearEpics() {
        epics.clear();
        subTasks.clear();
    }

    public void clearSubTasks() {
        subTasks.clear();
        for (Epic epic : epics.values()) {
            epic.setStatus(String.valueOf(Status.NEW));
            epic.getSubTasksId().clear();
        }
    }

    //    удаление задач, эпиков, подзадач по  идентификатору
    public void removeTaskById(int id) {
        if (getTaskById(id) != null) {
            tasks.remove(id);
        }
    }

    public void removeEpicById(int id) {
        if (getSubtasksByEpicId(id) != null) {
            for (Integer del : getSubtasksByEpicId(id)) {
                subTasks.remove(del);
            }
            epics.remove(id);
        }
    }

    public void removeSubTaskById(int id) {
        if (getSubtasksByEpicId(getSubTaskById(id).getEpicId()) != null) {
            getSubtasksByEpicId(getSubTaskById(id).getEpicId()).remove((Integer) id);
            refreshStatusEpic(getSubTaskById(id).getEpicId());
            subTasks.remove(id);
        }
    }
}
