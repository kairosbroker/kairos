package org.kairos;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.Socket;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TopicTest {

    ClientHandler clientHandleAlpha;
    ClientHandler clientHandleBravo;
    Topic topic;

    @BeforeEach
    void setUp() {
        clientHandleAlpha = new ClientHandler(new Socket());
        clientHandleBravo = new ClientHandler(new Socket());
        topic = new Topic("topic-key", clientHandleAlpha);
    }

    @Test
    void getName() {
        assertEquals("topic-key", topic.getName());
    }

    @Test
    void addSubscriber() {
        assertEquals(-1, topic.addSubscriber(clientHandleAlpha));
        assertEquals(2, topic.addSubscriber(clientHandleBravo));
    }

    @Test
    void removeSubscriber() {
        topic.addSubscriber(clientHandleBravo);
        assertEquals(1, topic.removeSubscriber(clientHandleAlpha));
        assertEquals(-1, topic.removeSubscriber(clientHandleAlpha));
        assertEquals(0, topic.removeSubscriber(clientHandleBravo));
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
