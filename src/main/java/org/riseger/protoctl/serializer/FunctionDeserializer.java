package org.riseger.protoctl.serializer;

import com.google.gson.*;
import org.riseger.protoctl.search.function.FUNCTION;
import org.riseger.protoctl.search.function.type.FUNCTIONBLE;

import java.lang.reflect.Type;

public class FunctionDeserializer implements JsonDeserializer<FUNCTIONBLE> {

    @Override
    public FUNCTIONBLE deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        Class<? extends FUNCTIONBLE> clazz = FUNCTION.idMap.get(Integer.parseInt(jsonObject.get("functionId").getAsString()));
        return JsonSerializer.gson.fromJson(json, clazz);
    }
}
