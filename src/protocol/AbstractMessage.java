package protocol;

import java.io.Serializable;

public abstract class AbstractMessage implements Serializable {

    protected final MessageType msgType;

    public AbstractMessage(MessageType type) {
        this.msgType = type;
    }

    public final MessageType getType() {
        return msgType;
    }
}
