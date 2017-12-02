package com.ka.noder.utils.widget;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.UUID;

class UuidConverter implements JsonSerializer<UUID>, JsonDeserializer<UUID>{

    @Override
    public JsonElement serialize(UUID uuid, Type type, JsonSerializationContext context) {
        System.out.println("uuid ser");
        return new JsonPrimitive(uuid.toString());
    }

    @Override
    public UUID deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context) throws JsonParseException {
        System.out.println("uuid deser");
        return UUID.fromString(jsonElement.getAsJsonPrimitive().getAsString());
    }
}
