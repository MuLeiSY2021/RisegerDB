package com.easyarch.protocol;

import io.netty.buffer.ByteBuf;

public class PacketCodec {

    public static final int MAGIC_NUMBER = 0x12345678;
    public static final PacketCodec INSTANCE = new PacketCodec();
    private static final byte VERSION = 1;
    private static final byte SERIALIZE = 1;
    private static final byte COMMAND = 1;
    private static final byte LENGTH = 4;

    private PacketCodec() {

    }


    public ByteBuf encode(ByteBuf buffer, BaseMsgPacket packet) {

        byte[] bytes = Serializer.DEFAULT.serialize(packet);

        buffer.writeInt(MAGIC_NUMBER);
        buffer.writeByte(packet.getVersion());
        buffer.writeByte(Constant.JSON_SERIALIZER);
        buffer.writeByte(packet.getCommand());
        buffer.writeInt(bytes.length);
        buffer.writeBytes(bytes);

        return buffer;

    }


    public BaseMsgPacket decode(ByteBuf byteBuf) {
        byteBuf.skipBytes(4);

        byteBuf.skipBytes(VERSION);

        byte serializerAlgorithm = byteBuf.readByte();

        byte command = byteBuf.readByte();

        int length = byteBuf.readInt();

        byte[] bytes = new byte[length];
        byteBuf.readBytes(bytes);

        Serializer serializer = Constant.serializerMap.get(serializerAlgorithm);
        Class<? extends BaseMsgPacket> packetClazz = Constant.packetMap.get(command);

        if (packetClazz != null && serializer != null) {
            return serializer.deserialize(packetClazz, bytes);
        }
        return null;
    }
}
