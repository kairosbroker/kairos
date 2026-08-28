package protocol;

public enum MessageType {
    MSG_PING,
    MSG_CONNECT, MSG_DISCONNECT, MSG_TIMEOUT,   //  Control
    MSG_JOIN, /*MSG_CREATE,*/ MSG_LEAVE,        //  Topic Traffic
    MSG_PUBLISH, MSG_CONSUME,                   //  Topic Message
    MSG_OK, MSG_ERROR                           //  Status Message
}
