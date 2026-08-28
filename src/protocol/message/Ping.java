package protocol.message;

import protocol.AbstractMessage;
import protocol.MessageType;

public final class Ping extends AbstractMessage {
    public Ping() {
        super(MessageType.MSG_PING);
    }
}
