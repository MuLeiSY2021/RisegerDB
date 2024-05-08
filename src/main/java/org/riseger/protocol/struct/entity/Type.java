package org.riseger.protocol.struct.entity;

import org.riseger.main.system.cache.component.Coord;
import org.riseger.main.system.cache.component.Coords;
import org.riseger.main.system.cache.component.GeoRectangle;

import java.lang.reflect.Method;

public enum Type {
    //Basic
    //Number
    //Int
    SHORT(Short.class), INT(Integer.class), LONG(Long.class),
    //Float
    FLOAT(Float.class), DOUBLE(Double.class),
    //String
    STRING(String.class),
    //Boolean
    BOOLEAN(Boolean.class),
    //Geometry
    COORD(Coord.class, true), LINE(Coords.class, true), LOOP(Coords.class, true), RECT(GeoRectangle.class, true);

    private final Class<?> type;

    private final boolean isKeyword;

    Type(Class<?> type) {
        this.type = type;
        this.isKeyword = false;
    }

    Type(Class<?> type, boolean isKeyword) {
        this.type = type;
        this.isKeyword = isKeyword;
    }

    public Object convert(String value) {
        try {
            if (this.type == String.class) {
                return value;
            } else {
                Method m = this.type.getMethod("valueOf", String.class);
                return m.invoke(null, value);
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("Unknown value type: " + this + " value: " + value);
        }
    }

    public boolean isConvertable(Object value) {
        return this.type.isInstance(value);
    }

    public boolean isKey() {
        return this.isKeyword;
    }
}
