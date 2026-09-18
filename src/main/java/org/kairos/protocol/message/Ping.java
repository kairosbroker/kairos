package org.kairos.protocol.message;

import org.kairos.protocol.AbstractCodeMessage;
import org.kairos.protocol.AbstractMessage;
import org.kairos.protocol.MessageType;

import java.nio.ByteBuffer;

public final class Ping extends AbstractCodeMessage {
    public Ping() {
        super(MessageType.MSG_PING);
    }
}
