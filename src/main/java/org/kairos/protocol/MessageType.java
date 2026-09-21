package org.kairos.protocol;

import org.kairos.protocol.message.*;

public enum MessageType {
    MSG_PING(255, Ping.class), //  Control
    MSG_CONNECT(1, Control.class), MSG_DISCONNECT(2, Control.class), MSG_TIMEOUT(3, Control.class),
    MSG_JOIN(4, TopicControl.class), /*MSG_CREATE(TopicControl.class),*/
    MSG_LEAVE(5, TopicControl.class),
    MSG_PUBLISH(8, Publish.class), MSG_CONSUME(9, Consume.class),
    MSG_OK(200, Status.class), MSG_ERROR(240, Status.class);

    private final int msgID;
    private final Class<? extends AbstractMessage> msgPackager;

    MessageType(int msgCode, Class<? extends AbstractMessage> msgPackager) {
        this.msgID = msgCode;
        this.msgPackager = msgPackager;
    }

    public int getID() {
        return msgID;
    }

    public Class<? extends AbstractMessage> getMessagePackager() {
        return msgPackager;
    }

    public static MessageType fromId(int msgID) {
        for (MessageType type : values()) {
            if (type.getID() == msgID) {
                return type;
            }
        }

        throw new IllegalArgumentException("Unknown MessageType id: " + msgID);
    }

    public static Class<? extends AbstractMessage> getPackagerByID(int msgID) {
        MessageType msgType = MessageType.values()[msgID];
        return msgType.getMessagePackager();
    }

}
