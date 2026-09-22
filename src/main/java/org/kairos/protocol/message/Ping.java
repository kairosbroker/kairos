package org.kairos.protocol.message;

import org.kairos.protocol.AbstractCodeMessage;
import org.kairos.protocol.MessageType;

public final class Ping extends AbstractCodeMessage {
    public Ping() {
        super(MessageType.MSG_PING);
    }
}
