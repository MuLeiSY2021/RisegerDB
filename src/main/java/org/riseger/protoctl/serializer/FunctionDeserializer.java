package org.riseger.protoctl.serializer;

import com.google.gson.*;
import org.riseger.protoctl.search.function.Function_f;
import org.riseger.protoctl.search.function.type.Functional;

import java.lang.reflect.Type;

public class FunctionDeserializer implements JsonDeserializer<Functional> {

    @Override
    public Functional deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        Class<? extends Functional> clazz = Function_f.idMap.get(Integer.parseInt(jsonObject.get("functionId").getAsString()));
        return JsonSerializer.gson.fromJson(json, clazz);
    }
}
