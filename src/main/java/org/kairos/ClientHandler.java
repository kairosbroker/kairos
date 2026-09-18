package org.kairos;

import org.kairos.protocol.AbstractMessage;
import org.kairos.protocol.MessageType;
import org.kairos.protocol.message.Consume;
import org.kairos.protocol.message.Control;
import org.kairos.protocol.message.Publish;
import org.kairos.protocol.message.Status;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.nio.ByteBuffer;
import java.util.Arrays;

public class ClientHandler extends Thread implements Runnable {
    private final Socket socket;
    //    private ObjectOutputStream out;
//    private ObjectInputStream in;
    private DataOutputStream out;
    private DataInputStream in;
    //    private HashSet topicKeys;
    private Topic subscribedTopic;

    public ClientHandler(Socket socket) {
        this.socket = socket;
//        this.topicKeys = new HashSet<String>();
        try {
//            out = new ObjectOutputStream( socket.getOutputStream() );
//            in = new ObjectInputStream( socket.getInputStream() );
            socket.setSoTimeout(5000);
            out = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
            in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));

        } catch (IOException e) {
//            throw new RuntimeException( e );
            System.err.println("Was not able to open IO channels for the client handler");
        }
    }

    @Override
    public void run() {
        //  Get topic name
        //  Accept and continue / Refuse and disconnect
        //  Consume msgs in topic
        try {
            int messageTypeOrdinal = in.readInt();
            Class<?> msgPackage = MessageType.getMessagePackageByOrdinal(messageTypeOrdinal);
            Object connectMsg = AbstractMessage.deserialize(msgPackage, MessageType.values()[messageTypeOrdinal], in);
            if (msgPackage != Control.class) {
                closeConnection(this);
            }

        } catch (IOException | InvocationTargetException | NoSuchMethodException | InstantiationException |
                 IllegalAccessException e) {
            if (e.getClass() == SocketException.class) {
                System.err.println("Connection was closed abruptly");
            }
            throw new RuntimeException(e);
        }

        Status okMessage = new Status(MessageType.MSG_OK);
        try {
            out.write(okMessage.serialize());
            out.flush();
            System.out.println("Sent message " + okMessage + " to client " + this.socket.getPort());
            //closeConnection(this);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        boolean disconnect = false;

        while (!disconnect) {
            //  infinite
            try {
                int messageTypeOrdinal = in.readInt();
                Class<?> msgPackage = MessageType.getMessagePackageByOrdinal(messageTypeOrdinal);
                Object msg = AbstractMessage.deserialize(msgPackage, MessageType.values()[messageTypeOrdinal], in);

                if (msg instanceof Publish pubMessage) {
                    System.out.println("Received message: " + pubMessage.getMessage() );
                    try {
                        System.out.println("Trying to send consumer message");
                        Consume consumeMessage = new Consume("kairós client worked, very cool Ó_ö");
                        byte[] encodedMsg = consumeMessage.serialize();
                        System.out.println("encoded: " + Arrays.toString(encodedMsg));
                        out.write(encodedMsg);
                        out.flush();

                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } else if (msg instanceof Control controlMsg) {
                    if (controlMsg.getType() == MessageType.MSG_DISCONNECT) {
                        disconnect = true;
                    }
                }
            } catch (IOException e) {

                System.err.println("Caught IOException: " + e.getClass() + " - " + e.getLocalizedMessage());
                disconnect = true;

            } catch (InvocationTargetException | NoSuchMethodException | InstantiationException |
                     IllegalAccessException e) {
                throw new RuntimeException("Something wrong happened: " + e);
            } finally {
                if (disconnect) {
                    try {
                        closeConnection(this);
                        System.out.println("Closed connection to client " + this.socket.getPort());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
    }

    public static void closeConnection(ClientHandler clientHandler) throws IOException {
        clientHandler.in.close();
        clientHandler.out.close();
        clientHandler.socket.close();
    }
}
