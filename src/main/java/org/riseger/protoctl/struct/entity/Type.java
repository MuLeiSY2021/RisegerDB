package org.riseger.protoctl.struct.entity;

public enum Type {
    //Basic
    //Number
    //Int
    SHORT, INT, LONG,
    //Float
    FLAOT, DOUBLE,
    //String
    STRING,
    //Boolean
    BOOLEAN,
    //Geometry
    COORD, COORDS_LINE, COORDS_LOOP;

    public Object convert(String value) {
        switch (this) {
            case SHORT:
                return Short.valueOf(value);

            case INT:
                return Integer.valueOf(value);

            case LONG:
                return Long.valueOf(value);

            case FLAOT:
                return Float.valueOf(value);

            case DOUBLE:
                return Double.valueOf(value);

            case STRING:
                return String.valueOf(value);

            case BOOLEAN:
                return Boolean.valueOf(value);

            default:
                throw new IllegalArgumentException("Unknown value type: " + this + " value: " + value);
        }

    }
}
