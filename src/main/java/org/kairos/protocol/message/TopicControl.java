package org.kairos.protocol.message;

import org.kairos.protocol.AbstractCodeMessage;
import org.kairos.protocol.MessageType;

import java.io.DataInputStream;

public final class TopicControl extends AbstractCodeMessage {
    public TopicControl(MessageType type) {
        super(type);
        if (type.ordinal() != MessageType.MSG_JOIN.ordinal() && type.ordinal() != MessageType.MSG_LEAVE.ordinal())
            throw new IllegalArgumentException("Invalid MessageType to Message package");

    }

    public TopicControl(MessageType type, DataInputStream input) {
        super(type, input);
    }
}
