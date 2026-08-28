package protocol;

public abstract class AbstractMessage {

    protected final MessageType msgType;

    public AbstractMessage(MessageType type){
        this.msgType = type;
    }

    public final MessageType getType(){
        return msgType;
    }
}
