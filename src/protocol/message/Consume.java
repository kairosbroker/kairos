package protocol.message;

import protocol.AbstractTopicMessage;
import protocol.MessageType;

public final class Consume extends AbstractTopicMessage {
    public Consume(String message) {
        super(MessageType.MSG_CONSUME, message);
    }
}
