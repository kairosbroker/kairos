package org.kairos.protocol;

import javax.imageio.stream.IIOByteBuffer;
import java.nio.ByteBuffer;

public abstract class AbstractTopicMessage extends AbstractMessage {
    private final String msg;

    public AbstractTopicMessage(MessageType type, String message) {
        super(type);
        if (type.ordinal() != MessageType.MSG_PUBLISH.ordinal() && type.ordinal() != MessageType.MSG_CONSUME.ordinal())
            throw new IllegalArgumentException("Invalid MessageType to Message package");
        this.msg = message;
    }

    public AbstractTopicMessage(MessageType type, ByteBuffer data) {
        super(type, data);
        this.msg = data.toString();
    }

    public final String getMessage() {
        return msg;
    }

    @Override
    public byte[] serialize() {
        int msgSize = (Integer.SIZE / 8) + (Integer.SIZE / 8) + getMessage().getBytes().length;
        ByteBuffer serializedMessage = ByteBuffer.allocate(msgSize);

        serializedMessage.putInt(this.getType().ordinal());
        serializedMessage.putInt(getMessage().length());
        serializedMessage.put(getMessage().getBytes());

        return serializedMessage.array();
    }
}
