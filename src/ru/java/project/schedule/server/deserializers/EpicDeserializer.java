package ru.java.project.schedule.server.deserializers;

import com.google.gson.*;
import ru.java.project.schedule.tasks.Epic;
import ru.java.project.schedule.tasks.Subtask;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class EpicDeserializer implements JsonDeserializer<Epic> {
    @Override
    public Epic deserialize(JsonElement json, Type typeOfT,
                               JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();

        String name = jsonObject.get("name").getAsString();
        String description = jsonObject.get("description").getAsString();

        Epic epic = new Epic(name, description);
        return epic;
    }
}
