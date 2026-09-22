package org.kairos.protocol;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.ProtocolException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public abstract class AbstractTopicMessage extends AbstractMessage {
    private final String msg;

    public AbstractTopicMessage(MessageType type, String message) {
        super(type);
        this.msg = message;
    }

    public AbstractTopicMessage(MessageType type, DataInputStream input) throws IOException {
        super(type, input);
//        System.out.println("Building a " + type + " Message");
        int msgSizeParam = input.readInt();
        if (msgSizeParam < 0 || msgSizeParam > MAX_MSG_BODY)
            throw new ProtocolException("Message body size arrgument has invalid payload size");

        byte[] buffer = new byte[msgSizeParam];
        int msgSizeRead = input.read(buffer);

//        System.out.println("msgSizeParam " + msgSizeParam + " msgSizeRead " + msgSizeRead);
        if (msgSizeParam != msgSizeRead) throw new ProtocolException("payload size mismatch");

        this.msg = new String(buffer);
    }

    public final String getMessage() {
        return msg;
    }

    @Override
    public byte[] serialize() {
        int msgSize = (Byte.SIZE / 8) + (Integer.SIZE / 8) + getMessage().getBytes(StandardCharsets.UTF_8).length;
        ByteBuffer serializedMessage = ByteBuffer.allocate(msgSize);

        byte myByte = (byte) getType().getID();
//        System.out.println("MyByte: " + myByte + " was supposed to be: " + getType().getID());
        serializedMessage.put((byte) getType().getID());
        serializedMessage.putInt(getMessage().getBytes(StandardCharsets.UTF_8).length);
        serializedMessage.put(getMessage().getBytes());

        return serializedMessage.array();
    }
}
