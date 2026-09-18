package org.kairos.protocol.message;

import org.kairos.protocol.AbstractCodeMessage;
import org.kairos.protocol.MessageType;

import java.nio.ByteBuffer;

public final class Control extends AbstractCodeMessage {
    public Control(MessageType type) {
        super(type);
        if (type.ordinal() < MessageType.MSG_CONNECT.ordinal() || type.ordinal() > MessageType.MSG_TIMEOUT.ordinal())
            throw new IllegalArgumentException("Invalid MessageType to Message package");

    }

    public Control(MessageType type, ByteBuffer data) {
        super(type, data);
    }

}
