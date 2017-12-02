package com.ka.noder.utils.widget;

import com.google.gson.*;
import com.ka.noder.model.Note;

import java.lang.reflect.Type;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.UUID;

class NoteConverter implements JsonSerializer<Note>, JsonDeserializer<Note> {

    @Override
    public JsonElement serialize(Note note, Type type, JsonSerializationContext context) {
        LocalDateTime dateTime = note.getDate();
        long date = dateTime.toInstant(ZoneOffset.UTC).toEpochMilli();

        System.out.println("date to json in milli: " + date);

        JsonObject result = new JsonObject();
        result.add("uuid", context.serialize(note.getUuid(), UUID.class));
        result.addProperty("title", note.getTitle());
        result.addProperty("text", note.getText());
        result.addProperty("password", note.getPassword() != null ? note.getPassword() : "null_password");
        result.add("date", context.serialize(note.getDate(), LocalDateTime.class));
        result.addProperty("status", note.getStatus());

        return result;
    }

    @Override
    public Note deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context) throws JsonParseException {

        JsonObject jsonObject = jsonElement.getAsJsonObject();

        long unixMilli = jsonObject.get("date").getAsLong();
        Instant instant = Instant.ofEpochMilli(unixMilli);
        LocalDateTime dateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
        System.out.println("date FROM json-");
        System.out.println("long milli: " + unixMilli);
        System.out.println("instant date: " + instant);
        System.out.println("local dateTime: " + dateTime);

        Note note = new Note();
        note.setUuid(context.deserialize(jsonObject.get("uuid"), UUID.class));
        note.setTitle(jsonObject.get("title").getAsString());
        note.setText(jsonObject.get("text").getAsString());
        note.setPassword(jsonObject.get("password").getAsString());
        note.setDate(context.deserialize(jsonObject.get("date"), LocalDateTime.class));
        note.setStatus(jsonObject.get("status").getAsInt());

        return note;
    }
}
