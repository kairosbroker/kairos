package org.kairos.protocol.message;

import org.kairos.protocol.AbstractMessage;
import org.kairos.protocol.MessageType;

public final class Control extends AbstractMessage {
    public Control(MessageType type) {
        super(type);
        if (type.ordinal() < MessageType.MSG_CONNECT.ordinal() || type.ordinal() > MessageType.MSG_TIMEOUT.ordinal())
            throw new IllegalArgumentException("Invalid MessageType to Message package");

    }
}
