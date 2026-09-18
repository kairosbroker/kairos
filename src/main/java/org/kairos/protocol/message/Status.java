package org.kairos.protocol.message;

import org.kairos.protocol.AbstractMessage;
import org.kairos.protocol.MessageType;

import java.nio.ByteBuffer;

public class Status extends AbstractMessage {
    public Status(MessageType type) {
        super(type);
        if (type.ordinal() != MessageType.MSG_OK.ordinal() && type.ordinal() != MessageType.MSG_ERROR.ordinal())
            throw new IllegalArgumentException("Invalid MessageType to Message package");
    }

    public Status(MessageType type, ByteBuffer data) {
        super(type);
    }

    @Override
    public byte[] serialize() {
        int msgSize = (Integer.SIZE / 8);
        ByteBuffer serializedMessage = ByteBuffer.allocate(msgSize);

        serializedMessage.putInt(this.getType().ordinal());

        return serializedMessage.array();
    }
}
