package org.kairos.protocol.message;

import org.kairos.protocol.AbstractMessage;
import org.kairos.protocol.MessageType;

import java.nio.ByteBuffer;

public final class TopicControl extends AbstractMessage {
    public TopicControl(MessageType type) {
        super(type);
        if (type.ordinal() != MessageType.MSG_JOIN.ordinal() && type.ordinal() != MessageType.MSG_LEAVE.ordinal())
            throw new IllegalArgumentException("Invalid MessageType to Message package");

    }

    @Override
    public byte[] serialize() {
        int msgSize = (Integer.SIZE / 8);
        ByteBuffer serializedMessage = ByteBuffer.allocate(msgSize);

        serializedMessage.putInt(this.getType().ordinal());

        return serializedMessage.array();
    }
}
