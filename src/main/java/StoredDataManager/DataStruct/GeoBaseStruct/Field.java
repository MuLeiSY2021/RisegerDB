package org.example.geoBaseStruct;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;

public class Field extends GeoStruct{

    private boolean passable;

    private LinkedList<String> pointLabels = new LinkedList<>();

    public Field(String label) {
        super(label);
    }

    public void setPassable(boolean passable) {
        this.passable = passable;
    }

    public void setPointLabels(String pointLabel) {
        this.pointLabels.add(pointLabel);
    }

    public ByteBuffer toChunk() {
        int len = 5+label.length();
        for (String tmp:pointLabels) {
            len += tmp.length() + 2;
        }
        ByteBuffer byteBuffer = ByteBuffer.allocate(len);
        byteBuffer.put(Utils.valueToBytes(label.length()))
                .put(Utils.valueToBytes(this.passable))
                .put(Utils.valueToBytes(pointLabels.size()))
                .put(this.label.getBytes(StandardCharsets.US_ASCII));
        for (String tmp:pointLabels) {
            byteBuffer.put(Utils.valueToBytes(tmp.length()))
                    .put(tmp.getBytes(StandardCharsets.US_ASCII));
        }
        return byteBuffer;
    }

    public static Field fromChunk(ByteBuffer byteBuffer) {
        int labelLen = byteBuffer.getShort(0);
        boolean passable = byteBuffer.get() == 1;
        int pointNum = byteBuffer.getShort(3);
        String label = new String(byteBuffer.array(), 5, labelLen, StandardCharsets.US_ASCII);
        Field field = new Field(label);
        field.setPassable(passable);

        int accumulation = 5 + labelLen;
        for (int i = 0; i < pointNum; i++) {
            int roadLabelLen = byteBuffer.getShort(accumulation);
            this.pointLabels.add(new String(byteBuffer.array(), accumulation + 2, roadLabelLen, StandardCharsets.US_ASCII));
            accumulation += roadLabelLen + 2;
        }
    }
}
