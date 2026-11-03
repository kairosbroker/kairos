package org.kairos;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.PriorityBlockingQueue;

public class TopicManager {
    private static TopicManager INSTANCE;
    private static final int SECONDS_TO_DELETION = (60 * 60);     // 1 hour
    private final ConcurrentHashMap<String, Topic> topicPool;
    private final PriorityBlockingQueue<Topic> deleteQueue;
    private final DeleteScheduler deleteScheduler;

    private TopicManager() {
        this.topicPool = new ConcurrentHashMap<>();
        this.deleteQueue = new PriorityBlockingQueue<>();
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

        if(_topic.getDeletionTimestamp() != null)
        {
            if(deleteQueue.peek() == _topic)
            {
                _topic.removeDeletionTimestamp();
                deleteScheduler.reschedule();
            }
            else
            {
                _topic.removeDeletionTimestamp();
            }

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
                return true;
            }

            deleteScheduler.schedule(_topic);
            return true;
        }
        return false;
    }

    public boolean topicExist(String name) {
        return topicPool.get(name) != null;
    }

    class DeleteScheduler implements Runnable {
        private Thread worker;

        @Override
        public void run() {
            while(true){
                Topic nextToDelete = deleteQueue.peek();

                if(nextToDelete == null )   //  || nextToDelete.getDeletionTimestamp() == null) // should not happen
                {
                    Logger.info("Delete Scheduler cleared queue, terminating.");
                    return;
                }

                Duration remaining = Duration.between(Instant.now(), nextToDelete.getDeletionTimestamp());

                try{
                    Thread.sleep(remaining);
                    Logger.info("Delete Scheduler removed topic \"" + nextToDelete.getName() + "\" from pool.");
                    topicPool.remove(nextToDelete.getName(), nextToDelete);

                } catch (InterruptedException e) {
                    Logger.info("Delete Scheduler interrupted, rescheduling");
                }
            }
        }

        void reschedule()
        {
            worker.interrupt();
        }

        synchronized void schedule(Topic topic)
        {
            topic.markForDeletion(Instant.now().plusSeconds(SECONDS_TO_DELETION));
            deleteQueue.add(topic);

            if(worker == null || !worker.isAlive())
            {
                worker = new Thread(this);
                worker.start();
            }
        }
    }
}
