package org.riseger.protoctl.serializer;

import com.google.gson.*;
import org.riseger.protoctl.search.function.Function_f;
import org.riseger.protoctl.search.function.type.FUNCTIONAL;

import java.lang.reflect.Type;

public class FunctionDeserializer implements JsonDeserializer<FUNCTIONAL> {

    @Override
    public FUNCTIONAL deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        Class<? extends FUNCTIONAL> clazz = Function_f.idMap.get(Integer.parseInt(jsonObject.get("functionId").getAsString()));
        return JsonSerializer.gson.fromJson(json, clazz);
    }
}
