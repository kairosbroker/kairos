package org.kairos;

import java.time.Instant;
import java.util.HashSet;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

public class Topic {

    private static final int MAX_MSGS = 100;
    private final String topicName;
    private int subscriberCount = 0;
    private Instant deletionTimestamp = null;
    private final HashSet<ClientHandler> subscribers;
    private final ArrayBlockingQueue<String> msgs;

    public Topic(String key, ClientHandler creator) {
        this.topicName = key;
        this.subscribers = new HashSet<>();
        this.msgs = new ArrayBlockingQueue<>(MAX_MSGS);
        addSubscriber(creator);
    }

    public String getName() {
        return topicName;
    }

    public synchronized int addSubscriber(ClientHandler client) {
        if (subscribers.contains(client)) {
            Logger.error("Error: client already subscribed to this topic.", new RuntimeException("Client is already subscribe to the requested topic"));
            return -1;
        }

        subscribers.add(client);
        subscriberCount++;

        return subscriberCount;
    }

    public synchronized int removeSubscriber(ClientHandler client) {
        if (!subscribers.contains(client)) {
            Logger.warn("Error: client is not subscribed on this topic.");
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
        return msgs.poll(2, TimeUnit.SECONDS);
    }

    public void publish(String data) throws InterruptedException {
        msgs.put(data);
    }

    public void markForDeletion(Instant timestamp) {
        deletionTimestamp = timestamp;
    }

    public void removeDeletionTimestamp() {
        deletionTimestamp = null;
    }

    public Instant getDeletionTimestamp() {
        return deletionTimestamp;
    }
}
