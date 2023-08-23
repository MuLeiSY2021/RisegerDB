package org.riseger.protoctl.struct.entity;

import lombok.Data;
import org.riseger.protoctl.struct.config.Option;

import java.util.HashMap;
import java.util.Map;

@Data
public class Element {
    private ParentModel parent;

    private String modelName;

    private Map<String, String> attributes;

    private Map<Option, String> configs = new HashMap<Option, String>();

    public Element(ParentModel parent,String modelName, Map<String, String> attributes, Map<Option, String> configs) {
        this.parent = parent;
        this.attributes = attributes;
        this.configs = configs;
        this.modelName = modelName;
    }

    public void setValue(String name, String value) {
        this.attributes.put(name, value);
    }

}
