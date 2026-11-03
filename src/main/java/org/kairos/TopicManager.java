package org.kairos;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

public class TopicManager {
    private static TopicManager INSTANCE;
    private static final int SECONDS_TO_DELETION = 10;
    private final ConcurrentHashMap<String, Topic> topicPool;
    private final ArrayBlockingQueue<Topic> deleteQueue;
    private final DeleteScheduler deleteScheduler;

    private TopicManager() {
        this.topicPool = new ConcurrentHashMap<>();
        this.deleteQueue = new ArrayBlockingQueue<>(10);
        this.deleteScheduler = new DeleteScheduler();
    }

    public static TopicManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new TopicManager();
        }

        return INSTANCE;
    }

    private Topic create(String name, ClientHandler client) {
        Topic _topic = new Topic(name, client);
        topicPool.put(name, _topic);
        Logger.info("New Topic created by client " + client.getName() + ": " + name);
        return _topic;
    }

    public synchronized Topic join(String name, ClientHandler client) throws InterruptedException {
        Topic _topic = topicPool.get(name);

        if (_topic == null) {
            Logger.warn("Topic nonexistent in poll, creating new one");
            _topic = create(name, client);
            return _topic;
        }

        if (_topic.addSubscriber(client) == -1) {
            return null;
        }

        if (_topic.getDeletionTimestamp() != null) {
            if (deleteQueue.peek() == _topic) {
                _topic.removeDeletionTimestamp();
                deleteScheduler.reschedule();
            } else {
                _topic.removeDeletionTimestamp();
            }

        }

        Logger.info("Client " + client.getName() + " joined an existing topic: " + _topic.getName());

        return _topic;
    }

    public synchronized boolean leave(String name, ClientHandler client) {
        Topic _topic = topicPool.get(name);

        if (_topic == null) {
            Logger.error("Topic nonexistent in poll", null);
            return false;
        }

        int remainingSubscribers = _topic.removeSubscriber(client);

        if (remainingSubscribers < 0) {
            Logger.warn("Something went wrong when trying to unsubscribe client " + client.threadId() + " from topic \"" + name + "\".");
            return false;
        }

        if (remainingSubscribers > 0) return true;

        if (_topic.getMessagesQuantity() == 0) {
            topicPool.remove(name, _topic);
            Logger.info("Last client unsubscribed from \"" + name + "\"; no messages pending. Removed topic from pool.");
            return true;
        }

        Logger.info("Last client unsubscribed from \"" + name + "\"; " + _topic.getMessagesQuantity() + " messages pending. Scheduled for deletion from pool.");
        deleteScheduler.schedule(_topic);
        return true;
    }

    public boolean topicExist(String name) {
        return topicPool.get(name) != null;
    }

    class DeleteScheduler implements Runnable {
        private Thread worker;

        @Override
        public void run() {
            while (true) {
                Topic nextToDelete = deleteQueue.peek();

                if (nextToDelete == null)   // empty queue head, job done
                {
                    Logger.info("Delete Scheduler cleared queue, terminating.");
                    return;
                }

                Duration remaining = Duration.between(Instant.now(), nextToDelete.getDeletionTimestamp());

                try {
                    Logger.warn("DeleteScheduler: will delete topic \"" + nextToDelete.getName() + "\" in " + remaining.getSeconds() + " seconds");
                    Thread.sleep(remaining.toMillis());
                    Logger.info("DeleteScheduler: removed topic \"" + nextToDelete.getName() + "\" from pool");
                    topicPool.remove(nextToDelete.getName(), nextToDelete);
                    deleteQueue.take();

                } catch (InterruptedException e) {
                    Logger.info("DeleteScheduler: interruption, rescheduling");
                }
            }
        }

        void reschedule() throws InterruptedException {
            worker.interrupt();
            Topic nextToDelete = deleteQueue.peek();
            if (nextToDelete != null && nextToDelete.getDeletionTimestamp() == null) {
                deleteQueue.take();
            }
        }

        synchronized void schedule(Topic topic) {
            Logger.info("DeleteScheduler: added topic \"" + topic.getName() + "\" to delete schedule");
            topic.markForDeletion(Instant.now().plusSeconds(SECONDS_TO_DELETION));
            deleteQueue.add(topic);

            if (worker == null || !worker.isAlive()) {
                worker = new Thread(this);
                worker.start();
            }
        }
    }
}
