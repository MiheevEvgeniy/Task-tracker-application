package ru.java.project.schedule.server.deserializers;

import com.google.gson.*;
import ru.java.project.schedule.tasks.Epic;
import ru.java.project.schedule.tasks.Subtask;
import ru.java.project.schedule.tasks.Task;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TaskDeserializer implements JsonDeserializer<Task> {
    @Override
    public Task deserialize(JsonElement json, Type typeOfT,
                               JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();

        String name = jsonObject.get("name").getAsString();
        String description = jsonObject.get("description").getAsString();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        LocalDateTime startTime = LocalDateTime.parse(jsonObject.get("startTime").getAsString(), formatter);

        int duration = jsonObject.get("duration").getAsInt();
        Task task = new Task(name, description, startTime, duration);
        return task;
    }
}
