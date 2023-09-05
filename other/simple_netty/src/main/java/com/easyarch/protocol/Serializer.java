package com.easyarch.protocol;

public interface Serializer {

    Serializer DEFAULT = new JsonSerializer();

    byte getSerializerAlgorithm();

    byte[] serialize(Object o);

    <T> T deserialize(Class<T> clazz, byte[] bytes);
}
