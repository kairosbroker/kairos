package org.kairos.protocol.message;

import org.kairos.protocol.AbstractTopicMessage;
import org.kairos.protocol.MessageType;

import java.io.DataInputStream;
import java.io.IOException;

public final class Publish extends AbstractTopicMessage {
    public Publish(String message) {
        super(MessageType.MSG_PUBLISH, message);
    }

    public Publish(MessageType type, DataInputStream input) throws IOException {
        super(type, input);
    }
}
