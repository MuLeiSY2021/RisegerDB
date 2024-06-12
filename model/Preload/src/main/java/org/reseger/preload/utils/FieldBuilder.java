package org.reseger.preload.utils;

import org.riseger.protocol.struct.config.Option;
import org.riseger.protocol.struct.entity.Element_p;
import org.riseger.protocol.struct.entity.GeoMap_p;
import org.riseger.protocol.struct.entity.ParentModel;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class FieldBuilder {
    protected final ParentModel parent = ParentModel.RECTANGLE;
    protected final ConcurrentMap<String, String> attributes = new ConcurrentHashMap<>();
    protected final ConcurrentMap<Option, String> configs = new ConcurrentHashMap<>();
    private final GeoMap_p map;
    private String model;
    private int loopSize = 0;
    private Element_p element;

    public FieldBuilder(GeoMap_p map) {
        this.map = map;
    }

    public void build() {
        this.element = new Element_p(this.parent, model, this.attributes, this.configs);
        map.addElement(this.element);
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
