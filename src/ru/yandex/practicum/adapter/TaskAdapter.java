package adapter;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import model.Task;

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
        result.addProperty("status", task.getStatus().name());
        result.addProperty("duration", task.getDuration());
        result.addProperty("startTime", task.getStartTime().format(DateTimeFormatter.
                ofPattern("yyyy-MM-dd|HH:mm:ss")));
        return result;
    }
}
