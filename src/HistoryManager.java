import java.util.List;

public interface HistoryManager {

    void add(Task task);//помачает задачи, как просмотренные

    List<Task> getHistory();//возвращает задачи в список
}
