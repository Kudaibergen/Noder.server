package com.ka.noder.utils.widget;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;

class DateTimeConverter implements JsonSerializer<LocalDateTime>, JsonDeserializer<LocalDateTime> {

    @Override
    public JsonElement serialize(LocalDateTime localDateTime, Type type, JsonSerializationContext context) {
        return new JsonPrimitive(localDateTime.toInstant(ZoneOffset.UTC).toEpochMilli());
    }

    @Override
    public LocalDateTime deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context) throws JsonParseException {
        long unixMilli = jsonElement.getAsJsonPrimitive().getAsLong();
        Instant instant = Instant.ofEpochMilli(unixMilli);
        return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
    }
}
