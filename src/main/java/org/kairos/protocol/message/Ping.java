package org.kairos.protocol.message;

import org.kairos.protocol.AbstractMessage;
import org.kairos.protocol.MessageType;

import java.nio.ByteBuffer;

public final class Ping extends AbstractMessage {
    public Ping() {
        super(MessageType.MSG_PING);
    }

    @Override
    public byte[] serialize() {
        int msgSize = (Integer.SIZE / 8);
        ByteBuffer serializedMessage = ByteBuffer.allocate(msgSize);

        serializedMessage.putInt(this.getType().ordinal());

        return serializedMessage.array();
    }
}
