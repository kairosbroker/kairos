package org.kairos.protocol;

import org.kairos.protocol.message.*;

public enum MessageType {
    MSG_PING, MSG_CONNECT, MSG_DISCONNECT, MSG_TIMEOUT,   //  Control
    MSG_JOIN, /*MSG_CREATE,*/ MSG_LEAVE,        //  Topic Traffic (Topic Control)
    MSG_PUBLISH, MSG_CONSUME,                   //  Topic Message
    MSG_OK, MSG_ERROR;                          //  Status Message

    public static Class<?> getMessagePackageByOrdinal(int msgTypeOrdinal) {
        MessageType msgType = MessageType.values()[msgTypeOrdinal];
        return getMessagePackageByType(msgType);
    }

    public static Class<?> getMessagePackageByType(MessageType msgType) {
        return switch (msgType) {
            case MSG_PING -> Ping.class;
            case MSG_CONNECT, MSG_DISCONNECT, MSG_TIMEOUT -> Control.class;
            case MSG_JOIN, MSG_LEAVE -> TopicControl.class;
            case MSG_PUBLISH -> Publish.class;
            case MSG_CONSUME -> Consume.class;
            case MSG_OK, MSG_ERROR -> Status.class;
        };
    }
}
