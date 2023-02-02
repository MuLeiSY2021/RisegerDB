package org.example.geoBaseStruct;

public class Utils {
    public static byte[] valueToBytes(int value)
    {
        int len = 4;

        byte[] src = new byte[len];
        for (int i = 0; i < len; i++) {
            src[len - 1 - i] =  (byte) ((value>>8*i) & 0xFF);
        }
        return src;
    }

    public static byte[] valueToBytes(long value)
    {
        int len = 8;
        byte[] src = new byte[len];
        for (int i = 0; i < len; i++) {
            src[len - 1 - i] =  (byte) ((value>>8*i) & 0xFF);
        }
        return src;
    }

    public static byte[] valueToBytes(short value)
    {
        int len = 2;
        byte[] src = new byte[len];
        for (int i = 0; i < len; i++) {
            src[len - 1 - i] =  (byte) ((value>>8*i) & 0xFF);
        }
        return src;
    }

    public static byte[] valueToBytes(boolean value)
    {
        int len = 1;
        byte[] src = new byte[len];
        src[0] = value ? (byte) 1 : (byte) 0;
        return src;
    }

    public static int bytesToInt(byte[] src, int offset, int len) {
        int value = 0;
        if(len > 4 || len < 1) {
            throw new IndexOutOfBoundsException();
        }
        for (int i = 0; i < len; i++) {
            value |=(int) ((src[offset + i] & 0xFF)<< i*8);
        }
        return value;
    }

    public static long bytesToLong(byte[] src, int offset, int len) {
        long value = 0;
        if(len > 8 || len < 1) {
            throw new IndexOutOfBoundsException();
        }
        for (int i = 0; i < len; i++) {
            value |=(int) ((src[offset + i] & 0xFF)<< i*8);
        }
        return value;
    }

    public static short bytesToShort(byte[] src, int offset, int len) {
        short value = 0;
        if(len > 2 || len < 1) {
            throw new IndexOutOfBoundsException();
        }
        for (int i = 0; i < len; i++) {
            value |=(int) ((src[offset + i] & 0xFF)<< i*8);
        }
        return value;
    }
}
