package ru.java.project.schedule.server.deserializers;

import com.google.gson.*;
import ru.java.project.schedule.tasks.Subtask;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SubtaskDeserializer implements JsonDeserializer<Subtask> {
    @Override
    public Subtask deserialize(JsonElement json, Type typeOfT,
                               JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();

        String name = jsonObject.get("name").getAsString();
        String description = jsonObject.get("description").getAsString();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        LocalDateTime startTime = LocalDateTime.parse(jsonObject.get("startTime").getAsString(), formatter);

        int duration = jsonObject.get("duration").getAsInt();
        int epicId = jsonObject.get("epicId").getAsInt();
        Subtask subtask = new Subtask(name, description, startTime, duration, epicId);
        return subtask;
    }
}
