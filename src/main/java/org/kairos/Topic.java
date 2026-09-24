package org.kairos;

import java.util.HashSet;
import java.util.concurrent.ArrayBlockingQueue;

public class Topic {
    private final String topicName;
    private int subscriberCount = 0;
    private final HashSet<ClientHandler> subscribers;
    private final ArrayBlockingQueue<String> msgs;

    public Topic(String key, ClientHandler creator) {
        this.topicName = key;
        this.subscribers = new HashSet<ClientHandler>();
        this.msgs = new ArrayBlockingQueue<String>(10);
        addSubscriber(creator);
    }

    public String getName(){
        return topicName;
    }

    public int addSubscriber(ClientHandler client) {
        if (subscribers.contains(client)) {
            Logger.error("Client already subscribed to this topic.", new InternalError("fds"));
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

    public String consume() throws InterruptedException {
        return msgs.take();
    }

    public void publish(String data) throws InterruptedException {
        msgs.put(data);
    }
}
