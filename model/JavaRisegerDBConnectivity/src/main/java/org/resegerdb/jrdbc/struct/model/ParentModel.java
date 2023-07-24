package org.resegerdb.jrdbc.struct.model;

public class ParentModel extends Model{

    public ParentModel(String name) {
        super(name);
    }

    public static final String POINT_NAME = "point";

    public static final ParentModel POINT = new ParentModel(POINT_NAME);
    static {
        POINT.addParameter("x", Type.COORD);
        POINT.addParameter("y", Type.COORD);
    }

    public static final String LINE_NAME = "line";

    public static final ParentModel LINE = new ParentModel(LINE_NAME);
    static {
        LINE.addParameter("x1", Type.COORD);
        LINE.addParameter("y1", Type.COORD);
        LINE.addParameter("x2", Type.COORD);
        LINE.addParameter("y2", Type.COORD);
    }

    public static final String RECTANGLE_NAME = "rect";

    public static final ParentModel RECTANGLE = new ParentModel(RECTANGLE_NAME);
    static {
        RECTANGLE.addParameter("x1", Type.COORD);
        RECTANGLE.addParameter("y1", Type.COORD);
        RECTANGLE.addParameter("x2", Type.COORD);
        RECTANGLE.addParameter("y2", Type.COORD);
    }
}
