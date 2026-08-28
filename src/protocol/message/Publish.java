package protocol.message;

import protocol.AbstractTopicMessage;
import protocol.MessageType;

public final class Publish extends AbstractTopicMessage {
    public Publish(String message) {
        super(MessageType.MSG_PUBLISH, message);
    }
}
