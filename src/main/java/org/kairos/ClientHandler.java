package org.kairos;

import org.kairos.protocol.MessageType;
import org.kairos.protocol.generic.Message;
import org.kairos.protocol.message.*;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.net.Socket;
import java.net.SocketException;

public class ClientHandler extends Thread implements Runnable {

    static final int CLIENT_IDENTIFY_TIMEOUT = 5000;

    private final Socket socket;
    private DataOutputStream out;
    private DataInputStream in;
    //    private HashSet topicKeys;
    private Topic subscribedTopic;

    public ClientHandler(Socket socket) {
        this.socket = socket;
//        this.topicKeys = new HashSet<String>();
        try {
            out = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
            in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));

        } catch (IOException e) {
            Logger.error("Unable to open IO channels for the client handler", e);
        }
    }

    @Override
    public void run() {

        MessageType clientType = greetClient();
        if (clientType == null) return;
        boolean isPublisher = clientType == MessageType.MSG_ID_PUBLISHER;
        reply(new Status(MessageType.MSG_OK));

        JoinTopic joinTopicMessage = (JoinTopic) waitForResponse();
        subscribedTopic = TopicManager.getInstance().join(joinTopicMessage.getTopic(), this);

        if (isPublisher) {
            loopPublisher();
        } else {
            loopConsumer();
        }

    }

    private void loopPublisher() {
        try {
            socket.setSoTimeout(0);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        while (true) {
            Message newMessage = waitForResponse();

            if (newMessage.getType() == MessageType.MSG_DISCONNECT) {
                return;
            }

            Logger.info("Client " + socket.getPort() + ": " + newMessage);

            try {
                subscribedTopic.publish( ((Publish) newMessage).getMessage() );
            } catch (InterruptedException e) {
                Logger.warn("Client interrupted the publishing of a new message");
                break;
            }
        }

    }

    private void loopConsumer() {
        String lastTopicMessage;
        boolean success = true;

        while (success) {
            try {
                lastTopicMessage = subscribedTopic.consume();
                reply((new Consume(lastTopicMessage)));
                Logger.info("Client " + socket.getPort() + " consumed " + lastTopicMessage);
            } catch (InterruptedException e) {
                success = false;
            }
        }
    }

    private MessageType greetClient() {

        try {
            socket.setSoTimeout(CLIENT_IDENTIFY_TIMEOUT);
            Control clientIdentify = (Control) waitForResponse();

            if (clientIdentify.getType() != MessageType.MSG_ID_PUBLISHER && clientIdentify.getType() != MessageType.MSG_ID_CONSUMER) {
                Logger.warn("Client " + socket.getPort() + " did not identify the type of user in time (" + CLIENT_IDENTIFY_TIMEOUT / 1000 + " seconds)");
                closeConnection(this);
                return null;
            }

            Logger.info("Client " + socket.getPort() + " identified as " + clientIdentify.getType());
            return clientIdentify.getType();

        } catch (IOException e) {
            Logger.error("Connection was closed abruptly", e);
            throw new RuntimeException(e);
        }

    }

    private boolean reply(Message message) {
        try {
            out.write(message.serialize());
            out.flush();
            Logger.info("Replied message " + message + " to client " + socket.getPort());
            return true;

        } catch (IOException e) {
            Logger.error("Unable to reply to client " + socket.getPort(), e);
            return false;
        }
    }

    private Message waitForResponse() {   //(Class<M> expectedPackager)
        try {
            return Message.deserialize(in.readUnsignedByte(), in);

        } catch (IOException | InvocationTargetException | NoSuchMethodException | InstantiationException |
                 IllegalAccessException e) {
            if (e.getClass() == SocketException.class) {
                Logger.error("Connection was closed abruptly", e);
            }
            throw new RuntimeException(e);
        }
    }

    public static void closeConnection(ClientHandler clientHandler) throws IOException {
        clientHandler.in.close();
        clientHandler.out.close();
        clientHandler.socket.close();
    }
}
