package adapter;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import model.SubTask;
import service.Status;

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
        Status status = subTask.getStatus();
        if (status != null) {
            if (status.equals(Status.NEW)) {
                result.addProperty("status", "NEW");
            } else if (status.equals(Status.IN_PROGRESS)) {
                result.addProperty("status", "IN_PROGRESS");
            } else if (status.equals(Status.DONE)) {
                result.addProperty("status", "DONE");
            } else {
                result.addProperty("status", subTask.getStatus().toString());
            }
        }
        result.addProperty("duration", subTask.getDuration());
        result.addProperty("startTime", subTask.getStartTime().
                format(DateTimeFormatter.ofPattern("yyyy-MM-dd|HH:mm:ss")));
        result.addProperty("epicId", subTask.getEpicId());
        return result;
    }
}
