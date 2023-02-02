package org.example.geoBaseStruct;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.LinkedList;

public class Point extends GeoStruct{

    private static HashMap<String,Point> points = new HashMap<String,Point>();

    private LinkedList<String> roadLabels = new LinkedList<>();

    private int coordinateX;

    private int coordinateY;

    public Point(String label,int coordinateX,int coordinateY) {
        super(label);
        this.coordinateX = coordinateX;
        this.coordinateY = coordinateY;
        points.put(label,this);
    }

    @Override
    public void setCoordination() {
        super.setCoordination(coordinateX,coordinateY,coordinateX,coordinateY);
    }

    public void setRoadLabels(String roadLabel) {
        this.roadLabels.add(roadLabel);
    }

    public void setCoordinateX(int coordinateX) {
        this.coordinateX = coordinateX;
    }

    public void setCoordinateY(int coordinateY) {
        this.coordinateY = coordinateY;
    }

    public ByteBuffer toChunk() {
        int len = 2*2+label.length() + 4*2;
        for (String tmp:roadLabels) {
            len += tmp.length() + 2;
        }
        ByteBuffer byteBuffer = ByteBuffer.allocate(len);
        byteBuffer.put(Utils.valueToBytes(label.length()))
                .put(Utils.valueToBytes(roadLabels.size()))
                .put(this.label.getBytes(StandardCharsets.US_ASCII))
                .put(Utils.valueToBytes(coordinateX))
                .put(Utils.valueToBytes(coordinateY));
        for (String tmp:roadLabels) {
            byteBuffer.put(Utils.valueToBytes(tmp.length()))
                    .put(tmp.getBytes(StandardCharsets.US_ASCII));
        }
        return byteBuffer;
    }

    public void fromChunk(ByteBuffer byteBuffer) {
        int labelLen = byteBuffer.getShort(0);
        int roadNum = byteBuffer.getShort(2);
        this.label = new String(byteBuffer.array(), 4, labelLen, StandardCharsets.US_ASCII);
        this.coordinateX = byteBuffer.getInt(4 + labelLen);
        this.coordinateY = byteBuffer.getInt(8 + labelLen);
        int accumulation = 8 + labelLen;
        for (int i = 0; i < roadNum; i++) {
            int roadLabelLen = byteBuffer.getShort(accumulation);
            this.roadLabels.add(new String(byteBuffer.array(), accumulation + 2, roadLabelLen, StandardCharsets.US_ASCII));
            accumulation += roadLabelLen + 2;
        }
    }
}
