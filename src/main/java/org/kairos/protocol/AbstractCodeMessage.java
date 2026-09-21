package org.kairos.protocol;

import java.io.DataInputStream;
import java.nio.ByteBuffer;

public abstract class AbstractCodeMessage extends AbstractMessage {

    public AbstractCodeMessage(MessageType type) {
        super(type);
    }

    public AbstractCodeMessage(MessageType type, DataInputStream input) {
        super(type, input);
    }

    @Override
    public byte[] serialize() {
        int msgSize = (Byte.SIZE / 8);
        ByteBuffer serializedMessage = ByteBuffer.allocate(msgSize);

        serializedMessage.put((byte) getType().getID());

        return serializedMessage.array();
    }
}
