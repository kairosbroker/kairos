import java.util.HashSet;
import java.util.concurrent.ArrayBlockingQueue;

public class Topic
{
    private final String topicName;
    private int subscriberCount;
    private HashSet<ClientHandler> subscribers;
    private ArrayBlockingQueue<String> msgs;

    public Topic(String key, ClientHandler creator)
    {
        this.topicName = key;
        this.subscriberCount = 1;   // a new topic starts with a single subscriber
        this.subscribers = new HashSet<ClientHandler>();
        subscribers.add( creator );
        this.msgs = new ArrayBlockingQueue<String>( 10 );
    }


}
