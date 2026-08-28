package protocol.message;

import protocol.AbstractMessage;
import protocol.MessageType;

public class Status extends AbstractMessage {

    public Status(MessageType type) {
        super(type);
        if (type.ordinal() != MessageType.MSG_OK.ordinal() && type.ordinal() != MessageType.MSG_ERROR.ordinal())
            throw new IllegalArgumentException("Invalid MessageType to Message package");
    }

}
