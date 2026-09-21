package org.kairos.protocol;

import java.io.DataInputStream;
import java.lang.reflect.InvocationTargetException;

public abstract class AbstractMessage {

    protected final MessageType msgType;
    static final int MAX_MSG_BODY = 1008;

    public AbstractMessage(MessageType type) {
        if (type.getID() < 0 || type.getID() > 255)
            throw new IllegalArgumentException("MessageType ID out-of-bounds [0 - 255]");
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

    public static AbstractMessage deserialize(int msgID, DataInputStream input) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
//        System.out.println("Read byte: " + msgID);
        MessageType msgType = MessageType.fromId(msgID);
        Class<? extends AbstractMessage> msgPackager = msgType.getMessagePackager();
        return msgPackager.getDeclaredConstructor(MessageType.class, DataInputStream.class).newInstance(msgType, input);
    }

}
