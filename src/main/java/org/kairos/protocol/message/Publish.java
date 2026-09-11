package org.kairos.protocol.message;

import org.kairos.protocol.AbstractTopicMessage;
import org.kairos.protocol.MessageType;

public final class Publish extends AbstractTopicMessage {
    public Publish(String message) {
        super(MessageType.MSG_PUBLISH, message);
    }
}
