package pers.muleisy.rtree.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.nio.charset.StandardCharsets;

public class JsonSerializer {

    private static final Gson gson = new GsonBuilder().serializeSpecialFloatingPointValues().create();

    public static byte[] serialize(Object obj) {
        return gson.toJson(obj).getBytes(StandardCharsets.UTF_8);
    }

    public static <T> T deserialize(byte[] bytes, Class<T> valueType) {
        return gson.fromJson(new String(bytes, StandardCharsets.UTF_8), valueType);
    }

    public static Object deserialize(String text, TypeToken<?> parameterized) {
        return gson.fromJson(text, parameterized);
    }
}
