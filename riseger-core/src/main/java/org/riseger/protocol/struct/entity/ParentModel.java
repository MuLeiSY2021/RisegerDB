package org.riseger.protocol.struct.entity;

public class ParentModel extends Model_p {

    public static final String POINT_NAME = "point";
    public static final ParentModel POINT = new ParentModel(POINT_NAME);
    public static final String LINE_NAME = "road";
    public static final ParentModel LINE = new ParentModel(LINE_NAME);
    public static final String FIELD = "field";
    public static final ParentModel RECTANGLE = new ParentModel(FIELD);

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
