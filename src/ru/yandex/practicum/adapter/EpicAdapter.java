package adapter;

import com.google.gson.*;
import model.Epic;

import java.lang.reflect.Type;
import java.time.format.DateTimeFormatter;

public class EpicAdapter implements JsonSerializer<Epic> {
    @Override
    public JsonElement serialize(Epic epic, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject result = new JsonObject();
        result.addProperty("id", epic.getId());
        result.addProperty("type", "EPIC");
        result.addProperty("name", epic.getName());
        result.addProperty("description", epic.getDescription());
        result.addProperty("status", epic.getStatus().name());
        result.addProperty("subTasksId", String.valueOf(epic.getSubTasksId()));
        result.addProperty("duration", epic.getDuration());
        result.addProperty("startTime", epic.getStartTime().
                format(DateTimeFormatter.ofPattern("yyyy-MM-dd|HH:mm:ss")));
        return result;
    }


}
