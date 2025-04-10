package org.riseger.protocol.struct.entity;

import lombok.Data;
import org.riseger.protocol.struct.config.Option;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Data
public class Element_p {
    private ParentModel parent;

    private String modelName;

    private ConcurrentMap<String, String> attributes;

    private ConcurrentMap<Option, String> configs;

    {
        new ConcurrentHashMap<>();
    }

    public Element_p(ParentModel parent, String modelName, ConcurrentMap<String, String> attributes, ConcurrentMap<Option, String> configs) {
        this.parent = parent;
        this.attributes = attributes;
        this.configs = configs;
        this.modelName = modelName;
    }

    public void setValue(String name, String value) {
        this.attributes.put(name, value);
    }

}
