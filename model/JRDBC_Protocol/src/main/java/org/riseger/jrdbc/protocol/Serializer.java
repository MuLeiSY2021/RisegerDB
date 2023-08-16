package org.riseger.jrdbc.protocol;

public interface Serializer {

    byte getSerializerAlgorithm();


    byte[] serialize(Object o);


    <T> T deserialize(Class<T> clazz, byte[] bytes);


    Serializer DEFAULT = new JsonSerializer();
}
