package org.riseger.protoctl.struct.model;

import lombok.Data;
import org.riseger.protoctl.struct.config.Option;

import java.util.HashMap;
import java.util.Map;

@Data
public class Element {
    private ParentModel parent;

    private Map<String, String> attributes;

    private Map<Option, String> configs = new HashMap<Option, String>();

    public Element(ParentModel parent, Map<String, String> attributes, Map<Option, String> configs) {
        this.parent = parent;
        this.attributes = attributes;
        this.configs = configs;
    }

    public void setValue(String name, String value) {
        this.attributes.put(name, value);
    }

}
