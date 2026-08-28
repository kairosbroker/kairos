package protocol;

public abstract class AbstractTopicMessage extends AbstractMessage {

    private final String msg;

    public AbstractTopicMessage(MessageType type, String message) {
        super(type);
        if (type.ordinal() != MessageType.MSG_PUBLISH.ordinal() && type.ordinal() != MessageType.MSG_CONSUME.ordinal())
            throw new IllegalArgumentException("Invalid MessageType to Message package");
        this.msg = message;
    }

    public final String getMessage() {
        return msg;
    }
}
