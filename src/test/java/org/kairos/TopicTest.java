package org.kairos;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.Socket;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TopicTest {

    ClientHandler cHandler;
    ClientHandler otherClient;
    Topic topic;

    @BeforeAll
    static void setUpAll() {
    }

    @BeforeEach
    void setUp() {
        cHandler = new ClientHandler(new Socket());
        otherClient = new ClientHandler(new Socket());
        topic = new Topic("topic-key", cHandler);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void getName() {
        assertEquals("topic-key", topic.getName());
    }

    @Test
    void addSubscriber() {
        assertEquals(-1, topic.addSubscriber(cHandler));
        assertEquals(2, topic.addSubscriber(otherClient));
    }

    @Test
    void removeSubscriber() {
        topic.addSubscriber(otherClient);
        assertEquals(1, topic.removeSubscriber(cHandler));
        assertEquals(-1, topic.removeSubscriber(cHandler));
        assertEquals(0, topic.removeSubscriber(otherClient));
    }

    @Test
    void getMessagesQuantity() throws InterruptedException {
        topic.publish("message 1");
        topic.publish("message 2");
        topic.publish("message 3");
        assertEquals(3, topic.getMessagesQuantity());

        assertEquals("message 1", topic.consume());
        assertEquals(2, topic.getMessagesQuantity());

        assertEquals("message 2", topic.consume());
        assertEquals(1, topic.getMessagesQuantity());

        assertEquals("message 3", topic.consume());
        assertEquals(0, topic.getMessagesQuantity());
    }

    @Test
    void consume() throws InterruptedException {
        topic.publish("message 1");
        topic.publish("message 2");
        topic.publish("message 3");

        assertEquals("message 1", topic.consume());
        assertEquals("message 2", topic.consume());
        assertEquals("message 3", topic.consume());
    }

    @Test
    void publish() throws InterruptedException {
        topic.publish("message 1");
        topic.publish("message 2");
        topic.publish("message 3");

        assertEquals(3, topic.getMessagesQuantity());
    }
}
