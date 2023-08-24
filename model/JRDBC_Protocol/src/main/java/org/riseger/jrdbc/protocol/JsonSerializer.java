package org.riseger.jrdbc.protocol;

import com.google.gson.Gson;

import java.nio.charset.StandardCharsets;

public class JsonSerializer implements Serializer {

    private static final Gson GSON = new Gson();

    @Override
    public byte getSerializerAlgorithm() {
        return Constant.JSON_SERIALIZER;
    }

    @Override
    public byte[] serialize(Object o) {
        return GSON.toJson(o).getBytes(StandardCharsets.UTF_8);
    }

    @Override
    public <T> T deserialize(Class<T> clazz, byte[] bytes) {
        return GSON.fromJson(new String(bytes, StandardCharsets.UTF_8), clazz);
    }
}
