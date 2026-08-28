package protocol.message;

import protocol.AbstractMessage;
import protocol.MessageType;

public final class TopicControl extends AbstractMessage {

    public TopicControl(MessageType type) {
        super(type);
        if (type.ordinal() != MessageType.MSG_JOIN.ordinal() && type.ordinal() != MessageType.MSG_LEAVE.ordinal())
            throw new IllegalArgumentException("Invalid MessageType to Message package");

    }
}
