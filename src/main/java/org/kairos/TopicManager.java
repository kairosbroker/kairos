package org.kairos;

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
        Logger.info("New Topic created by client " + client.getName() + ": " + name );
        return _topic;
    }

    public Topic join(String name, ClientHandler client) {
        Topic _topic = topicPool.get(name);

        if (_topic == null) {
            Logger.warn("Topic nonexistent in poll, creating new one");
            _topic = create(name, client);
            return _topic;
        }

        if( _topic.addSubscriber(client) == -1 ){
            return null;
        }

        Logger.info("Client " + client.getName() + " joined an existing topic: " + _topic.getName() );

        return _topic;
    }

    public boolean leave(String name, ClientHandler client) {
        Topic _topic = topicPool.get(name);

        if (_topic == null) {
            Logger.error("Topic nonexistent in poll", null);
            return false;
        }

        int remainingSubscribers = _topic.removeSubscriber(client);

        if (remainingSubscribers >= 0) {
            if (remainingSubscribers == 0 && _topic.getMessagesQuantity() == 0) {
                topicPool.remove(name, _topic);
                Logger.info("Last client unsubscribed from \"" + name + "\", removed from topic pool.");
            }
            return true;
        }
        return false;
    }
}
