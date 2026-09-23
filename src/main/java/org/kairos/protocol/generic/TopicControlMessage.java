package org.kairos.protocol.generic;

import org.kairos.protocol.MessageType;

import java.io.DataInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

public abstract class TopicControlMessage extends Message {
    private final String topicName;

    public TopicControlMessage(MessageType type, String topicName) {
        super(type);
        this.topicName = topicName;
    }

    public TopicControlMessage(MessageType type, DataInputStream input) throws IOException {
        super(type, input);

        byte[] buffer = new byte[MAX_TOPIC_NAME];
        input.read(buffer);

        this.topicName = new String(buffer);
    }

    public final String geTopic() {
        return topicName;
    }

    @Override
    public byte[] serialize() {
        int msgSize = (Byte.SIZE / 8) + MAX_TOPIC_NAME;
        ByteBuffer serializedMessage = ByteBuffer.allocate(msgSize);

        serializedMessage.put((byte) getType().getID());
        serializedMessage.put(geTopic().getBytes());

        return serializedMessage.array();
    }
}
