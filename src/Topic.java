import java.util.HashSet;
import java.util.concurrent.ArrayBlockingQueue;

public class Topic {
    private final String topicName;
    private int subscriberCount = 0;
    private HashSet<ClientHandler> subscribers;
    private ArrayBlockingQueue<String> msgs;

    public Topic(String key, ClientHandler creator) {
        this.topicName = key;
        this.subscribers = new HashSet<ClientHandler>();
        this.msgs = new ArrayBlockingQueue<String>(10);
        addSubscriber(creator);
    }

    public int addSubscriber(ClientHandler client) {
        if (subscribers.contains(client)) {
            System.err.println("Error: client already subscribed to this topic.");
            return -1;
        }

        subscribers.add(client);
        subscriberCount++;

        return subscriberCount;
    }

    public int removeSubscriber(ClientHandler client) {
        if (!subscribers.contains(client)) {
            System.err.println("Error: client is not subscribed on this topic.");
            return -1;
        }

        subscribers.remove(client);
        subscriberCount--;

        return subscriberCount;
    }

    public int getMessagesQuantity() {
        return msgs.size();
    }
}
