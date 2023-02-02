package org.example.geoBaseStruct;

import org.example.rTree.Rectangle;

import java.nio.ByteBuffer;

public abstract class GeoStruct extends Rectangle {
    protected final String label;

    public GeoStruct(String label) {
        if(label.length() > 2 << 16) {
            this.label = label;
        } else {
            throw new IllegalArgumentException();
        }
    }


    public abstract ByteBuffer toChunk();

    public abstract void fromChunk(ByteBuffer chunk);
}
