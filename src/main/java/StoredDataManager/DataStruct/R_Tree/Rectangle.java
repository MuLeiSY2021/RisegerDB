package StoredDataManager.DataStruct.rTree;

import java.util.Arrays;
import java.util.Collection;

public abstract class Rectangle {
    private static int num;

    private String ID;

    private int minX = Integer.MAX_VALUE;

    private int maxX = Integer.MIN_VALUE;

    private int minY = Integer.MAX_VALUE;

    private int maxY = Integer.MIN_VALUE;

    protected abstract void setCoordination();

    protected void setCoordination(int minX, int minY, int maxX, int maxY) {
        this.minX = minX;
        this.minY = minY;
        this.maxX = maxX;
        this.maxY = maxY;
    }

    public Rectangle(String ID) {
        this.setID(ID);
    }

    public String getID() {
        return ID;
    }

    public static void numUpper() {
        num++;
    }

    public static int getNum() {
        return num;
    }

    public int[][] getRactangle() {
        int[][] rectangle = new int[2][2];
        rectangle[0][0] = getMinX();
        rectangle[0][1] = getMinY();

        rectangle[1][0] = getMaxX();
        rectangle[1][1] = getMaxY();
        return rectangle;
    }

    public int getSquare() {
        return (getMaxX() - getMinX()) * (getMaxY() - getMinY());
    }



    public Rectangle() {
    }

    public Rectangle(Rectangle rectangle) {
        setCoordination(rectangle);
    }

    protected void setCoordination(Rectangle rectangle) {
        this.minX = rectangle.getMinX();
        this.minY = rectangle.getMinY();
        this.maxX = rectangle.getMaxX();
        this.maxY = rectangle.getMaxY();
    }

    public void extendRectangle(Rectangle rectangle) {
        this.minX = Math.min(rectangle.getMinX(), this.getMinX());
        this.minY = Math.min(rectangle.getMinY(), this.getMinY());
        this.maxX = Math.max(rectangle.getMaxX(), this.getMaxX());
        this.maxY = Math.max(rectangle.getMaxY(), this.getMaxY());
    }

    protected void extendRectangle(Collection<? extends Rectangle> collection) {
        for (Rectangle rectangle:
                collection) {
            this.extendRectangle(rectangle);
        }
    }

    public void shrunkRectangle(Rectangle rectangle) {
        this.minX = Math.max(rectangle.getMinX(), this.getMinX());
        this.minY = Math.max(rectangle.getMinY(), this.getMinY());
        this.maxX = Math.min(rectangle.getMaxX(), this.getMaxX());
        this.maxY = Math.min(rectangle.getMaxY(), this.getMaxY());
    }

    protected void shrunkRectangle(Collection<? extends Rectangle> collection) {
        for (Rectangle rectangle:
                collection) {
            this.shrunkRectangle(rectangle);
        }
    }

    public Rectangle(Collection<? extends Rectangle> collection) {
        extendRectangle(collection);
    }

    public Rectangle(Rectangle...collection) {
        extendRectangle(Arrays.asList(collection));
    }

    @Override
    public String toString() {
        return "P1:(" + getMinX() + "," + getMinY() + ") P2:(" + getMaxX() + "," + getMaxY() + ")";
    }

    public boolean isIntersection(Rectangle rectangle) {
        int[] middleP_self = {getMinX() + (getMaxX() - getMinX()) / 2, getMinY() + (getMaxY() - getMinY()) / 2};
        int[] middleP_other = {rectangle.getMinX() + (rectangle.getMaxX() - rectangle.getMinX()) / 2, rectangle.getMinY() + (rectangle.getMaxY() - rectangle.getMinY()) / 2};
        return Math.abs(middleP_self[0] - middleP_other[0])<<1 <= getMaxX() - getMinX() + rectangle.getMaxX() - rectangle.getMinX()
                && Math.abs(middleP_self[1] - middleP_other[1])<<1 <= getMaxY() - getMinY() + rectangle.getMaxY() - rectangle.getMinY();
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public int getMinX() {
        return minX;
    }

    public int getMaxX() {
        return maxX;
    }

    public int getMinY() {
        return minY;
    }

    public int getMaxY() {
        return maxY;
    }

}
