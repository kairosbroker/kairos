package org.kairos;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.Socket;

import static org.junit.jupiter.api.Assertions.*;

class TopicManagerTest {

    ClientHandler clientHandleAlpha;
    TopicManager INSTANCE;

    @BeforeEach
    void setUp() {
        INSTANCE = TopicManager.getInstance();
        clientHandleAlpha = new ClientHandler(new Socket());
    }

    @Test
    void getInstance() {
        assertEquals(INSTANCE, TopicManager.getInstance());
    }

    @Test
    void join() throws InterruptedException {
        //  Joining new Topic
        Topic newTopic = INSTANCE.join("new-topic-test", clientHandleAlpha);
        assertNotNull(newTopic);

        //  Same client trying to join a topic which is already subscribed to. Should return Null
        Topic alreadyJoinedTopic = INSTANCE.join("new-topic-test", clientHandleAlpha);
        assertNull(alreadyJoinedTopic);

        //  New Client joining an already existing topic
        ClientHandler clientHandleBravo = new ClientHandler(new Socket());
        Topic existingTopic = INSTANCE.join("new-topic-test", clientHandleBravo);
        assertSame(newTopic, existingTopic);
    }

    @Test
    void leave() throws InterruptedException {
        //  Try leave() on a non-existing topic
        assertFalse(INSTANCE.leave("topic-leave-test", clientHandleAlpha));

        //  Try leave() an existing topic with more than one client
        ClientHandler anotherClient = new ClientHandler(new Socket());

        Topic newTopic = INSTANCE.join("topic-leave-test", clientHandleAlpha);
        INSTANCE.join("topic-leave-test", anotherClient);
        assertTrue(INSTANCE.leave("topic-leave-test", anotherClient));

        //  Try leave() an existing topic with pending messages
        newTopic.publish("message-1");  //  throws InterruptedException
        assertTrue(INSTANCE.leave("topic-leave-test", clientHandleAlpha));

        //  Try leave() on a topic which is not subscribed to
        //      (Topic still exists at this point due to pending messages)
        assertFalse(INSTANCE.leave("topic-leave-test", clientHandleAlpha));

        //  Try leave() on an existing topic after depleting existing messages
        newTopic = INSTANCE.join("topic-leave-test", clientHandleAlpha);
        newTopic.consume();  //  throws InterruptedException
        assertTrue(INSTANCE.leave("topic-leave-test", clientHandleAlpha));
    }

    @Test
    void topicExist() throws InterruptedException {
        INSTANCE.join("new-topic-test", clientHandleAlpha);
        assertTrue(INSTANCE.topicExist("new-topic-test"));

        INSTANCE.leave("new-topic-test", clientHandleAlpha);
        assertFalse(INSTANCE.topicExist("new-topic-test"));

        //  last client leaves but messages still pending
        Topic newTopic = INSTANCE.join("new-topic-test", clientHandleAlpha);
        assertTrue(INSTANCE.topicExist("new-topic-test"));

        newTopic.publish("message-1");  //  throws InterruptedException
        INSTANCE.leave("new-topic-test", clientHandleAlpha);
        assertTrue(INSTANCE.topicExist("new-topic-test"));
    }
}
