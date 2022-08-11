package adapter;

import com.google.gson.*;
import model.Epic;
import service.Status;

import java.lang.reflect.Type;

public class EpicAdapter implements JsonSerializer<Epic> {
    @Override
    public JsonElement serialize(Epic epic, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject result = new JsonObject();
        result.addProperty("id", epic.getId());
        result.addProperty("type", "EPIC");
        result.addProperty("name", epic.getName());
        result.addProperty("description", epic.getDescription());
        Status status = epic.getStatus();
        if (status != null) {
            if (status.equals(Status.NEW)) {
                result.addProperty("status", "NEW");
            } else if (status.equals(Status.IN_PROGRESS)) {
                result.addProperty("status", "IN_PROGRESS");
            } else if (status.equals(Status.DONE)) {
                result.addProperty("status", "DONE");
            } else {
                result.addProperty("status", epic.getStatus().toString());
            }
        }
        result.addProperty("subTasksId", String.valueOf(epic.getSubTasksId()));
        return result;
    }


}
