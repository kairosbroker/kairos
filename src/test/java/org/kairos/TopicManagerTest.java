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
    void leave() {
        assertFalse(INSTANCE.leave("new-topic-test", cHandler));

        //  Missing cases
    }

    @Test
    void topicExist() {
        Topic newTopic = INSTANCE.join("new-topic-test", cHandler);
        assertTrue(INSTANCE.topicExist("new-topic-test"));

        INSTANCE.leave("new-topic-test", cHandler);
        assertFalse(INSTANCE.topicExist("new-topic-test"));

        //  Missing case when last client leaves but messages still pending
    }
}
