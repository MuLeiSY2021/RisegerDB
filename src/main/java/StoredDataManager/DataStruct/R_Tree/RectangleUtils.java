package StoredDataManager.DataStruct.rTree;

import java.util.Collection;

public class RectangleUtils {
    public static int getSquare(Collection<? extends Rectangle> collection) {
        BaseRectangle rectangle = new BaseRectangle();
        for (Rectangle rectangle1: collection) {
            rectangle.extendRectangle(rectangle1);
        }
        return rectangle.getSquare();
    }

    public static int getSquare(Rectangle...rectangles) {
        BaseRectangle rectangle = new BaseRectangle();
        for (Rectangle rectangle1: rectangles) {
            rectangle.extendRectangle(rectangle1);
        }
        return rectangle.getSquare();
    }
}
