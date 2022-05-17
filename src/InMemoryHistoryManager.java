import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

class InMemoryHistoryManager implements HistoryManager {
    List<Task> viewedTasks = new ArrayList<>();
    int MAXI_ITEMS_IN_THE_LIST = 10;


    // просмотр истории
    @Override
    public List<Task> getHistory() {
        for (Task t : viewedTasks) {
            if (t instanceof Task) {
                System.out.println(t);
            } else if (t instanceof Epic) {
                System.out.println(t);
            } else if (t instanceof SubTask) {
                System.out.println(t);
            }
        }
        return viewedTasks;
    }

    //обновление истории
    @Override
    public void add(Task task) {
        viewedTasks.add(task);
        if (viewedTasks.size() == MAXI_ITEMS_IN_THE_LIST) {
            viewedTasks.remove(0);
        }
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
        return "InMemoryHistoryManager{" +
                "viewedTasks=" + viewedTasks +
                '}';
    }
}
