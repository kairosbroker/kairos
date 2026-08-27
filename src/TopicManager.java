import java.util.HashMap;

public class TopicManager {
    private static TopicManager INSTANCE;
    private final HashMap<String, Topic> topicPool;

    private TopicManager() {
        this.topicPool = new HashMap<>();
    }

    public static TopicManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new TopicManager();
        }

        return INSTANCE;
    }

    public Topic create(String name, ClientHandler client) {
        Topic _topic = new Topic(name, client);
        topicPool.put(name, _topic);

        return _topic;
    }

    public boolean join(String name, ClientHandler client) {
        Topic _topic = topicPool.get(name);

        if (_topic == null) {
            System.err.println("Error: topic nonexistent in poll, creating new one");
            create(name, client);
            return true;
        }

        int joined = _topic.addSubscriber(client);

        return joined != -1;
    }

    public boolean leave(String name, ClientHandler client) {
        Topic _topic = topicPool.get(name);

        if (_topic == null) {
            System.err.println("Error: topic nonexistent in poll");
            return false;
        }

        int remainingSubscribers = _topic.removeSubscriber(client);

        if (remainingSubscribers >= 0) {
            if (remainingSubscribers == 0 && _topic.getMessagesQuantity() == 0) {
                topicPool.remove(name, _topic);
                System.out.println("Last client unsubscribed from \"" + name + "\", removed from topic pool.");
            }
            return true;
        }
        return false;
    }
}
