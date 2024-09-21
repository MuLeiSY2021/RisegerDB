package org.riseger.main.system.cache.component;

import org.riseger.protocol.struct.entity.Type;

public class ParentModel extends Model {
    public static final String POINT_NAME = "point";
    public static final org.riseger.protocol.struct.entity.ParentModel POINT = new org.riseger.protocol.struct.entity.ParentModel(POINT_NAME);
    public static final String LINE_NAME = "road";
    public static final org.riseger.protocol.struct.entity.ParentModel LINE = new org.riseger.protocol.struct.entity.ParentModel(LINE_NAME);
    public static final String FIELD = "field";
    public static final org.riseger.protocol.struct.entity.ParentModel RECTANGLE = new org.riseger.protocol.struct.entity.ParentModel(FIELD);

    static {
        POINT.addParameter("KEY_CORD", Type.COORD);
    }

    static {
        LINE.addParameter("KEY_LINE", Type.LINE);
    }

    static {
        RECTANGLE.addParameter("KEY_LOOP", Type.LOOP);
    }

    public ParentModel(String name) {
        super(name);
    }
}
