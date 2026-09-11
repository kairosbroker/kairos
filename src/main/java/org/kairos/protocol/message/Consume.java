package org.kairos.protocol.message;

import org.kairos.protocol.AbstractTopicMessage;
import org.kairos.protocol.MessageType;

public final class Consume extends AbstractTopicMessage {
    public Consume(String message) {
        super(MessageType.MSG_CONSUME, message);
    }
}
