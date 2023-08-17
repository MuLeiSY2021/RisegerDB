package org.riseger.protoctl.struct.model;

public class ParentModel extends Model {

    public ParentModel(String name) {
        super(name);
    }

    public static final String POINT_NAME = "point";

    public static final ParentModel POINT = new ParentModel(POINT_NAME);

    static {
        POINT.addParameter("KEY_CORD", Type.COORD);
    }

    public static final String LINE_NAME = "road";

    public static final ParentModel LINE = new ParentModel(LINE_NAME);

    static {
        LINE.addParameter("KEY_LINE", Type.COORDS_LINE);
    }

    public static final String FIELD = "field";

    public static final ParentModel RECTANGLE = new ParentModel(FIELD);

    static {
        RECTANGLE.addParameter("KEY_LOOP", Type.COORDS_LOOP);
    }
}
