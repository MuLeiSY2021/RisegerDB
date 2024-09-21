package org.riseger.protocol.serializer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;

public class JsonSerializer {
    static final Gson gson;

    static {
        gson = new GsonBuilder()
                .registerTypeAdapterFactory(new FunctionTypeAdapterFactory())
                .serializeSpecialFloatingPointValues()
                .create();
    }



    public static byte[] serialize(Object obj) {
        return gson.toJson(obj).getBytes(StandardCharsets.UTF_8);
    }

    public static String serializeToString(Object obj) {
        return gson.toJson(obj);
    }


    public static <T> T deserialize(byte[] bytes, Class<T> valueType) {
        return gson.fromJson(new String(bytes, StandardCharsets.UTF_8), valueType);
    }

    public static <T> T deserialize(byte[] bytes, Type valueType) {
        return gson.fromJson(new String(bytes, StandardCharsets.UTF_8), valueType);
    }

    public static Object deserialize(String text, TypeToken<?> parameterized) {
        return gson.fromJson(text, parameterized);
    }

    public static <T> T deserialize(String line, Class<T> valueType) {
        return deserialize(line.getBytes(StandardCharsets.UTF_8), valueType);
    }
}
