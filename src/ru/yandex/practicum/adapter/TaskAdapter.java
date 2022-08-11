package adapter;

import com.google.gson.*;
import model.Task;
import service.Status;


import java.lang.reflect.Type;
import java.time.format.DateTimeFormatter;

public class TaskAdapter implements JsonSerializer<Task> {
    @Override
    public JsonElement serialize(Task task, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject result = new JsonObject();
        result.addProperty("id", task.getId());
        result.addProperty("type", "TASK");
        result.addProperty("name", task.getName());
        result.addProperty("description", task.getDescription());
        Status status = task.getStatus();
        if (status != null) {
            if (status.equals(Status.NEW)) {
                result.addProperty("status", "NEW");
            } else if (status.equals(Status.IN_PROGRESS)) {
                result.addProperty("status", "IN_PROGRESS");
            } else if (status.equals(Status.DONE)) {
                result.addProperty("status", "DONE");
            } else {
                result.addProperty("status", task.getStatus().toString());
            }
        }
        result.addProperty("duration", task.getDuration());
        result.addProperty("startTime", task.getStartTime().format(DateTimeFormatter.
                ofPattern("yyyy-MM-dd|HH:mm:ss")));
        return result;
    }
}
