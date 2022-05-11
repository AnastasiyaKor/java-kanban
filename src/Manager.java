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
        subTasks.put(subTask.getId(), subTask);
        subTaskList(subTask.getEpicId()).add(subTask.getId());
        refreshStatusEpic(subTask.getEpicId());
    }

    //    получение списка задач, эпиков, подзадач
    public void printTasks() {
        for (Task t : tasks.values()) {
            System.out.println(t);
        }
    }

    public void printEpics() {
        for (Epic e : epics.values()) {
            System.out.println(e);
        }
    }

    public void printSubTasks() {
        for (SubTask t : subTasks.values()) {
            System.out.println(t);
        }
    }

    //    получение задач, эпиков, подзадач по идентификатору
    public Task receiveTask(int id) {
        return tasks.get(id);
    }

    public Epic receiveEpics(int id) {
        return epics.get(id);
    }

    public SubTask receiveSubTasks(int id) {
        return subTasks.get(id);
    }

    public ArrayList<Integer> subTaskList(int epicId) {
        if (receiveEpics(epicId) != null) {
            return receiveEpics(epicId).getSubTasksId();
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
        int progress = 0;
        int done = 0;
        if (subTaskList(id) != null) {
            for (int i = 0; i < subTaskList(id).size(); i++) {
                if (receiveSubTasks(subTaskList(id).get(i)).equals(Task.Status.IN_PROGRESS)) {
                    progress++;
                } else {
                    done++;
                }
            }
            if (progress < 1 || done < 1) {
                receiveEpics(id).setStatus(String.valueOf(Task.Status.NEW));
            } else if (progress > 1) {
                receiveEpics(id).setStatus(String.valueOf(Task.Status.IN_PROGRESS));
            } else {
                receiveEpics(id).setStatus(String.valueOf(Task.Status.DONE));
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
        for (Integer id : epics.keySet()) {
            receiveEpics(id).setStatus(String.valueOf(Task.Status.NEW));
            receiveEpics(id).setSubTasksId(null);
        }
    }

    //    удаление задач, эпиков, подзадач по  идентификатору
    public void removeTasks(int id) {
        if (receiveTask(id) != null) {
            tasks.remove(id);
        }
    }

    public void removeEpics(int id) {
        if (subTaskList(id) != null) {
            for (int i = 0; i < subTaskList(id).size(); i++) {
                subTasks.remove(subTaskList(id).get(i));
            }
            epics.remove(id);
        }
    }

    public void removeSubTasks(int id) {
        if (subTaskList(receiveSubTasks(id).getEpicId()) != null) {
            subTaskList(receiveSubTasks(id).getEpicId()).remove((Integer) id);
            refreshStatusEpic(receiveSubTasks(id).getEpicId());
            subTasks.remove(id);
        }
    }


}
