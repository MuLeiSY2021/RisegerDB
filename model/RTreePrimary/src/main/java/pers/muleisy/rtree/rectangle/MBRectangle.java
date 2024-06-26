package pers.muleisy.rtree.rectangle;

import io.netty.buffer.ByteBuf;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

public abstract class MBRectangle implements Rectangle {
    protected Double threshold;
    private Double minX = (Double.MAX_VALUE);
    private Double maxX = -(Double.MAX_VALUE);
    private Double minY = (Double.MAX_VALUE);
    private Double maxY = -(Double.MAX_VALUE);

    public MBRectangle(Double threshold) {
        this.threshold = threshold;
        truncateAll();
    }

    public MBRectangle(Collection<? extends Rectangle> rects, Double threshold) {
        expandAll(rects);
        this.threshold = threshold;
        truncateAll();
    }

    public MBRectangle(Double threshold, Rectangle... rects) {
        expandAll(rects);
        this.threshold = threshold;
        truncateAll();
    }

    public static void serialize(ByteBuf byteBuf, MBRectangle rectangle) {
        byteBuf.writeDouble(rectangle.minX);
        byteBuf.writeDouble(rectangle.maxX);
        byteBuf.writeDouble(rectangle.maxY);
        byteBuf.writeDouble(rectangle.minY);
    }

    // Deserialize a Leaf object
    public void deserialize(ByteBuf byteBuf) {
        minX = byteBuf.readDouble();
        maxX = byteBuf.readDouble();
        maxY = byteBuf.readDouble();
        minY = byteBuf.readDouble();
    }

    private void initialize() {
        this.setMinX(Double.MAX_VALUE);
        this.setMaxX(Double.MIN_VALUE);
        this.setMinY(Double.MAX_VALUE);
        this.setMaxY(Double.MIN_VALUE);
    }

    @Override
    public Double maxX() {
        return maxX;
    }

    @Override
    public Double maxY() {
        return maxY;
    }

    @Override
    public Double minX() {
        return minX;
    }

    @Override
    public Double minY() {
        return minY;
    }

    public void setMinX(Double minX) {
        this.minX = truncateDecimal(minX);
    }

    public void setMaxX(Double maxX) {
        this.maxX = truncateDecimal(maxX);
    }

    public void setMinY(Double minY) {
        this.minY = truncateDecimal(minY);
    }

    public void setMaxY(Double maxY) {
        this.maxY = truncateDecimal(maxY);
    }

    public void setThreshold(Double threshold) {
        this.threshold = threshold;
        truncateAll();
    }

    public void setAll(Rectangle rectangle) {
        this.setMinX(rectangle.minX());
        this.setMinY(rectangle.minY());
        this.setMaxX(rectangle.maxX());
        this.setMaxY(rectangle.maxY());
    }

    @Override
    public void expand(Rectangle rectangle) {
        this.setMinX(Math.min(rectangle.minX(), this.minX()));
        this.setMinY(Math.min(rectangle.minY(), this.minY()));
        this.setMaxX(Math.max(rectangle.maxX(), this.maxX()));
        this.setMaxY(Math.max(rectangle.maxY(), this.maxY()));
    }

    public boolean willBeExpand(Rectangle rectangle) {
        return this.minX.isNaN() || rectangle.minX() < this.minX() ||
                this.minY.isNaN() || rectangle.minY() < this.minY() ||
                this.maxX.isNaN() || rectangle.maxX() > this.maxX() ||
                this.maxY.isNaN() || rectangle.maxY() > this.maxY();
    }

    @Override
    public boolean expandAll(Collection<? extends Rectangle> rects) {
        boolean result = false;
        for (Rectangle rect : rects) {
            boolean tmp = willBeExpand(rect);
            result |= tmp;
            if (tmp) {
                this.expand(rect);
            }
        }
        return result;
    }

    @Override
    public final boolean expandAll(Rectangle... rects) {
        boolean result = false;
        for (Rectangle rect : rects) {
            boolean tmp = willBeExpand(rect);
            result |= tmp;
            if (tmp) {
                this.expand(rect);
            }
        }
        return result;
    }

    @Override
    public void copy(Rectangle rectangle) {
        this.setMinX(rectangle.minX());
        this.setMinY(rectangle.minY());
        this.setMaxX(rectangle.maxX());
        this.setMaxY(rectangle.maxY());
    }

    @Override
    public Double area() {
        return width() * height();
    }

    @Override
    public double overlap(Rectangle rectangle) {
        double h = Math.min(this.maxY, rectangle.maxY()) - Math.max(this.minY, rectangle.minY());
        double w = Math.min(this.maxX, rectangle.maxX()) - Math.max(this.minX, rectangle.minX());
        if (h < 0 || w < 0) {
            return -1;
        }
        return h * w;

    }

    @Override
    public boolean isLegal() {
        return this.minX <= this.maxX && this.minY <= this.maxY;
    }

    @Override
    public Double margin() {
        return width() + height();
    }

    @Override
    public Double distance(Rectangle rect) {

        return Math.sqrt(distance_compare(rect));
    }

    public Double distance_compare(Rectangle rect) {
        Double self_middle_x = width();
        Double self_middle_y = height();
        Double rect_middle_x = rect.width();
        Double rect_middle_y = rect.height();

        return Math.pow(Math.abs(self_middle_x - rect_middle_x), 2)
                + Math.pow(Math.abs(self_middle_y - rect_middle_y), 2);
    }

    @Override
    public boolean match(Rectangle rect) {
        return (Objects.equals(rect.maxX(), this.maxX)) &&
                (Objects.equals(rect.maxY(), this.maxY)) &&
                (Objects.equals(rect.minX(), this.minX)) &&
                (Objects.equals(rect.minY(), this.minY));
    }

    @Override
    public boolean inner(Rectangle rect) {
        return rect.maxX() <= this.maxX &&
                rect.maxY() <= this.maxY &&
                rect.minX() >= this.minX &&
                rect.minY() >= this.minY;
    }

    public void adjustAll(Collection<? extends Rectangle> sets) {
        initialize();
        expandAll(sets);
    }

    @Override
    public boolean intersects(Rectangle other) {
        return this.overlap(other) > 0;
    }

    @Override
    public Double width() {
        return this.maxX() - this.minX();
    }

    @Override
    public Double height() {
        return this.maxY() - this.minY();
    }

    public void move(Double x, Double y) {
        this.setMaxX(x + width());
        this.setMinX(x);
        this.setMaxY(y + height());
        this.setMinY(y);
    }

    @Override
    public boolean intersects(List<? extends Rectangle> other) {
        for (Rectangle rectangle : other) {
            if (this.intersects(rectangle)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Rectangle) {
            Rectangle rect = (Rectangle) obj;
            return rect.match(this);
        }
        return false;
    }

    protected double truncateDecimal(double value) {
        if (threshold == null) {
            return value;
        }
        // 将小于 threshold 的部分舍去
        return Math.floor(value / threshold) * threshold;
    }

    private void truncateAll() {
        this.setMinX(this.minX);
        this.setMaxX(this.maxX);
        this.setMinY(this.minY);
        this.setMaxY(this.maxY);
    }

    @Override
    public int hashCode() {
        return Objects.hash(minX, maxX, minY, maxY, threshold);
    }

    @Override
    public String toString() {
        return "MBRectangle{" +
                "minX=" + minX +
                ", maxX=" + maxX +
                ", minY=" + minY +
                ", maxY=" + maxY +
                '}';
    }

    public String toFileString() {
        return "x" + minX +
                "y" + minY +
                "x" + maxX +
                "y" + maxY;
    }


}
