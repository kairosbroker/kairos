package org.kairos.protocol;

import java.io.DataInputStream;
import java.lang.reflect.InvocationTargetException;

public abstract class AbstractMessage {

    protected final MessageType msgType;
    static final int MAX_MSG_BODY = 1008;

    public AbstractMessage(MessageType type) {
        if (type.ordinal() >= MessageType.values().length)
            throw new IllegalArgumentException("Invalid MessageType to Message package");
        this.msgType = type;
    }

    public AbstractMessage(MessageType type, DataInputStream input) {
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

    public static Object deserialize(Class<?> msgPackage, MessageType msgType, DataInputStream input) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        return msgPackage.getDeclaredConstructor(MessageType.class, DataInputStream.class).newInstance(msgType, input);
    }

}
