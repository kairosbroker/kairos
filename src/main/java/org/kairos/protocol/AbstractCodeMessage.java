package org.kairos.protocol;

import java.io.DataInputStream;
import java.nio.ByteBuffer;

public abstract class AbstractCodeMessage extends AbstractMessage {

    public AbstractCodeMessage(MessageType type) {
        super(type);
        if (type.ordinal() > MessageType.MSG_LEAVE.ordinal() && type.ordinal() < MessageType.MSG_OK.ordinal())
            throw new IllegalArgumentException("Invalid MessageType to Message package");
    }

    public AbstractCodeMessage(MessageType type, DataInputStream input) {
        super(type, input);
    }

    @Override
    public byte[] serialize() {
        int msgSize = (Integer.SIZE / 8);
        ByteBuffer serializedMessage = ByteBuffer.allocate(msgSize);

        serializedMessage.putInt(this.getType().ordinal());

        return serializedMessage.array();
    }
}
