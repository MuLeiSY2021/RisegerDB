package org.riseger.protoctl.message;

import com.google.gson.Gson;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class JsonSerializer {

    private static final Gson gson = new Gson();

    public static byte[] serialize(Object obj) throws IOException {
        return gson.toJson(obj).getBytes(StandardCharsets.UTF_8);
    }

    public static <T> T deserialize(byte[] bytes, Class<T> valueType) throws IOException {
        return gson.fromJson(new String(bytes,StandardCharsets.UTF_8),valueType);
    }
}
