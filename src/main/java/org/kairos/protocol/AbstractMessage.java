package org.kairos.protocol;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.lang.reflect.InvocationTargetException;
import java.nio.ByteBuffer;

public abstract class AbstractMessage {

    protected final MessageType msgType;

    public AbstractMessage(MessageType type) {
        if (type.ordinal() >= MessageType.values().length)
            throw new IllegalArgumentException("Invalid MessageType to Message package");
        this.msgType = type;
    }

    public AbstractMessage(MessageType type, ByteBuffer data) {
        this.msgType = type;
    }

    public final MessageType getType() {
        return msgType;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + ":" + getType().toString();
    }

    public abstract byte[] serialize();

    public static Object deserialize(byte[] data) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        ByteBuffer buffer = ByteBuffer.allocate(Integer.SIZE / 8);
        buffer.put(data);
        int msgOrdinal = buffer.getInt(0);
        System.out.println("msgOrdinal: " + msgOrdinal);

        MessageType msgType = MessageType.values()[msgOrdinal];
        Class<?> msgPackage = MessageType.getMessagePackage(msgType);
        return msgPackage.getDeclaredConstructor(MessageType.class, ByteBuffer.class).newInstance(msgType, buffer);
    }

}
