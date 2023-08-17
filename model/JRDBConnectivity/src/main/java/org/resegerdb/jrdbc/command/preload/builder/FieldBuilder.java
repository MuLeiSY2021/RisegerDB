package org.resegerdb.jrdbc.command.preload.builder;

import org.riseger.protoctl.struct.config.Option;
import org.riseger.protoctl.struct.model.Element;
import org.riseger.protoctl.struct.model.MapDB;
import org.riseger.protoctl.struct.model.ParentModel;

import java.util.HashMap;
import java.util.Map;

public class FieldBuilder {
    private final MapDB map;

    private String model;

    protected ParentModel parent = ParentModel.RECTANGLE;

    private int loopSize = 0;

    protected Map<String, String> attributes = new HashMap<>();

    protected Map<Option, String> configs = new HashMap<Option, String>();

    private Element element;

    public FieldBuilder(MapDB map) {
        this.map = map;
    }

    public Element build() {
        this.element = new Element(this.parent, this.attributes, this.configs);
        map.addElement(this.element);
        return this.element;
    }

    public FieldBuilder addCoord(double x, double y) {
        //KEY_LOOP::0::x --> 在环的第0个位置添加一个点的x坐标
        attributes.put("KEY_LOOP::" + loopSize + "::x", String.valueOf(x));
        //KEY_LOOP::0::x --> 在环的第0个位置添加一个点的y坐标
        attributes.put("KEY_LOOP::" + loopSize + "::y", String.valueOf(y));
        loopSize++;
        return this;
    }

    public FieldBuilder value(String name, String value) {
        this.attributes.put(name, value);
        return this;
    }

    public FieldBuilder config(Option option, String value) {
        this.configs.put(option, value);
        return this;
    }

    public FieldBuilder addModel(String model) {
        this.model = model;
        return this;
    }
}
