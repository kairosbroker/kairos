package org.kairos.protocol.message;

import org.kairos.protocol.AbstractMessage;
import org.kairos.protocol.MessageType;

public final class Ping extends AbstractMessage {
    public Ping() {
        super(MessageType.MSG_PING);
    }
}
