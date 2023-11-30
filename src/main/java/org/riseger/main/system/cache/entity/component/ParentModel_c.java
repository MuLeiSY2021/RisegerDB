package org.riseger.main.system.cache.entity.component;

import org.riseger.protoctl.struct.entity.ParentModel;
import org.riseger.protoctl.struct.entity.Type;

public class ParentModel_c extends Model_c {
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
        LINE.addParameter("KEY_LINE", Type.COORDS_LINE);
    }

    static {
        RECTANGLE.addParameter("KEY_LOOP", Type.COORDS_LOOP);
    }

    public ParentModel_c(String name) {
        super(name);
    }
}
