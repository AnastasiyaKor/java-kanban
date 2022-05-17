import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class InMemoryTaskManager implements TaskManager {
    private final HistoryManager historyManager = Managers.getDefaultHistory();
    private final HashMap<Integer, Task> tasks;
    private final HashMap<Integer, Epic> epics;
    private final HashMap<Integer, SubTask> subTasks;
    private int id = 0;

    public InMemoryTaskManager() {
        this.tasks = new HashMap<>();
        this.epics = new HashMap<>();
        this.subTasks = new HashMap<>();
    }


    //    создание задач, эпиков, подзадач
    @Override
    public void createTasks(Task task) {
        task.setId(id);
        id++;
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
        if (epics.containsKey(subTask.getEpicId())) {
            subTasks.put(subTask.getId(), subTask);
            getSubtasksByEpicId(subTask.getEpicId()).add(subTask.getId());
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
        historyManager.add(tasks.get(id));//добавляем вызов задачи в список
        return tasks.get(id);
    }

    @Override
    public Epic getEpicById(int id) {
        historyManager.add(epics.get(id));//добавляем вызов эпика в список
        return epics.get(id);
    }

    @Override
    public SubTask getSubTaskById(int id) {
        historyManager.add(subTasks.get(id));//добавляем вызов подзадачи в список
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
    @Override
    public void refreshTasks(Task task) {
        if (tasks.containsKey(task.getId())) {
            tasks.put(task.getId(), task);
        }
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
            subTasks.put(subTask.getId(), subTask);
            refreshStatusEpic(subTask.getEpicId());
        }
    }

    //    обновление статуса эпика
    public void refreshStatusEpic(int epicId) {
        Epic epic = getEpicById(epicId);
        int newStatus = 0;
        int done = 0;
        if (getSubtasksByEpicId(epicId) != null) {
            for (Integer i : getSubtasksByEpicId(epicId)) {
                if (getSubTaskById(i).getStatus().equals(Status.NEW)) {
                    newStatus++;
                } else if (getSubTaskById(i).getStatus().equals(Status.DONE)) {
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

    //    удаление задач, эпиков, подзадач
    @Override
    public void clearTasks() {
        tasks.clear();
    }

    @Override
    public void clearEpics() {
        epics.clear();
        subTasks.clear();
    }

    @Override
    public void clearSubTasks() {
        subTasks.clear();
        for (Epic epic : epics.values()) {
            epic.setStatus(Status.NEW);
            epic.getSubTasksId().clear();
        }
    }

    //    удаление задач, эпиков, подзадач по  идентификатору
    @Override
    public void removeTaskById(int id) {
        if (getTaskById(id) != null) {
            tasks.remove(id);
        }
    }

    @Override
    public void removeEpicById(int id) {
        if (getSubtasksByEpicId(id) != null) {
            for (Integer del : getSubtasksByEpicId(id)) {
                subTasks.remove(del);
            }
            epics.remove(id);
        }
    }

    @Override
    public void removeSubTaskById(int id) {
        if (getSubtasksByEpicId(getSubTaskById(id).getEpicId()) != null) {
            getSubtasksByEpicId(getSubTaskById(id).getEpicId()).remove((Integer) id);
            refreshStatusEpic(getSubTaskById(id).getEpicId());
            subTasks.remove(id);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InMemoryTaskManager that = (InMemoryTaskManager) o;
        return id == that.id && Objects.equals(historyManager, that.historyManager) && Objects.equals(tasks, that.tasks) && Objects.equals(epics, that.epics) && Objects.equals(subTasks, that.subTasks);
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
