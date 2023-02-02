package org.example.geoBaseStruct;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class Road extends GeoStruct{
    public enum Direction{
        SINGLE,
        BOTH;

        public byte toChunk() {
            switch (this) {
                case SINGLE:
                    return 0;
                case BOTH:
                    return 1;
                default:
                    throw new IllegalArgumentException();
            }
        }

        public static Direction fromChunk(byte b) {
            switch (b) {
                case 0:
                    return Direction.SINGLE;
                case 1:
                    return Direction.BOTH;
                default:
                    throw new IllegalArgumentException();
            }
        }
    }
    private String p1Label;

    private String p2Label;

    private Direction side;

    private int weight;

    public Road(String label) {
        super(label);
    }

    public Road setP1Lable(String p1Label) {
        this.p1Label = p1Label;
        return this;
    }

    public Road setP2Lable(String p2Label) {
        this.p2Label = p2Label;
        return this;
    }

    public Road setSide(Direction side) {
        this.side = side;
        return this;
    }

    public Road setWeight(int weight) {
        this.weight = weight;
        return this;
    }

    public ByteBuffer toChunk() {
        int len = 2 * 3 + 1 + this.label.length() + p1Label.length() + p2Label.length() + 4;
        ByteBuffer byteBuffer = ByteBuffer.allocate(len);
        byteBuffer.put(Utils.valueToBytes(label.length()))
                .put(Utils.valueToBytes(p1Label.length()))
                .put(Utils.valueToBytes(p2Label.length()))
                .put(this.side.toChunk())
                .put(this.label.getBytes(StandardCharsets.US_ASCII))
                .put(this.p1Label.getBytes(StandardCharsets.US_ASCII))
                .put(this.p2Label.getBytes(StandardCharsets.US_ASCII))
                .put(Utils.valueToBytes(weight));
        return byteBuffer;
    }

    public void fromChunk(ByteBuffer byteBuffer) {
        int labelLen = byteBuffer.getShort(0);
        int p1LabelLen = byteBuffer.getShort(2);
        int p2LabelLen = byteBuffer.getShort(4);
        this.side = Direction.fromChunk(byteBuffer.get(6));
        this.label = new String(byteBuffer.array(), 7, labelLen, StandardCharsets.US_ASCII);
        this.p1Label = new String(byteBuffer.array(), 7 + labelLen, p1LabelLen ,StandardCharsets.US_ASCII);
        this.p2Label = new String(byteBuffer.array(), 7 + labelLen + p1LabelLen ,p2LabelLen,StandardCharsets.US_ASCII);
    }
}
