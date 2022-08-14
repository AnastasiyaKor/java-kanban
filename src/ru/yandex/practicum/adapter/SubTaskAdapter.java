package adapter;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import model.SubTask;

import java.lang.reflect.Type;
import java.time.format.DateTimeFormatter;

public class SubTaskAdapter implements JsonSerializer<SubTask> {
    @Override
    public JsonElement serialize(SubTask subTask, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject result = new JsonObject();
        result.addProperty("id", subTask.getId());
        result.addProperty("type", "SUB_TASK");
        result.addProperty("name", subTask.getName());
        result.addProperty("description", subTask.getDescription());
        result.addProperty("status", subTask.getStatus().name());
        result.addProperty("duration", subTask.getDuration());
        result.addProperty("startTime", subTask.getStartTime().
                format(DateTimeFormatter.ofPattern("yyyy-MM-dd|HH:mm:ss")));
        result.addProperty("epicId", subTask.getEpicId());
        return result;
    }
}
