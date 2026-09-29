package org.kairos;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.Socket;

import static org.junit.jupiter.api.Assertions.*;

class TopicManagerTest {

    ClientHandler cHandler;
    TopicManager INSTANCE;

    @BeforeAll
    static void setUpAll() {
    }

    @BeforeEach
    void setUp() {
        INSTANCE = TopicManager.getInstance();
        cHandler = new ClientHandler(new Socket());
    }

    @Test
    void getInstance() {
        assertEquals(INSTANCE, TopicManager.getInstance());
    }

    @Test
    void create() {
        Topic newTopic = INSTANCE.create("topic-test", cHandler);
        assertNotNull(newTopic);
    }

    @Test
    void join() {
        Topic newTopic = INSTANCE.join("new-topic-test", cHandler);
        assertNotNull(newTopic);

        Topic alreadyJoined = INSTANCE.join("new-topic-test", cHandler);
        assertNull(alreadyJoined);

        ClientHandler otherClient = new ClientHandler(new Socket());

        Topic existingTopic = INSTANCE.join("new-topic-test", otherClient);
        assertSame(newTopic, existingTopic);
    }

    @Test
    void leave() throws InterruptedException {
        //  Try leave() on a non-existing topic
        assertFalse(INSTANCE.leave("topic-leave-test", cHandler));

        //  Try leave() an existing topic with more than one client
        ClientHandler anotherClient = new ClientHandler(new Socket());

        Topic newTopic = INSTANCE.join("topic-leave-test", cHandler);
        INSTANCE.join("topic-leave-test", anotherClient);
        assertTrue(INSTANCE.leave("topic-leave-test", anotherClient));

        //  Try leave() an existing topic with pending messages
        newTopic.publish("message-1");  //  throws InterruptedException
        assertTrue(INSTANCE.leave("topic-leave-test", cHandler));

        //  Try leave() on a topic which is not subscribed to
        //      (Topic still exists at this point due to pending messages)
        assertFalse(INSTANCE.leave("topic-leave-test", cHandler));

        //  Try leave() on an existing topic after depleting existing messages
        newTopic = INSTANCE.join("topic-leave-test", cHandler);
        newTopic.consume();  //  throws InterruptedException
        assertTrue(INSTANCE.leave("topic-leave-test", cHandler));
    }

    @Test
    void topicExist() throws InterruptedException {
        INSTANCE.join("new-topic-test", cHandler);
        assertTrue(INSTANCE.topicExist("new-topic-test"));

        INSTANCE.leave("new-topic-test", cHandler);
        assertFalse(INSTANCE.topicExist("new-topic-test"));

        //  last client leaves but messages still pending
        Topic newTopic = INSTANCE.join("new-topic-test", cHandler);
        assertTrue(INSTANCE.topicExist("new-topic-test"));

        newTopic.publish("message-1");  //  throws InterruptedException
        INSTANCE.leave("new-topic-test", cHandler);
        assertTrue(INSTANCE.topicExist("new-topic-test"));
    }
}
