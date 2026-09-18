package org.kairos;

import org.kairos.protocol.AbstractMessage;
import org.kairos.protocol.MessageType;
import org.kairos.protocol.message.Control;
import org.kairos.protocol.message.Status;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.net.Socket;
import java.net.SocketException;
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
            System.out.println("reading all bytes");
            byte[] buffer = new byte[4];
            in.read(buffer);
            System.out.println("just read all bytes: " + Arrays.toString(buffer));
            Control connectMsg = (Control) AbstractMessage.deserialize(buffer);
            if (connectMsg.getType() != MessageType.MSG_CONNECT) {
                closeConnection(this);
            }

        } catch (IOException | InvocationTargetException | NoSuchMethodException | InstantiationException |
                 IllegalAccessException e) {
            if (e.getClass() == java.net.SocketException.class) {
                System.err.println("Connection was closed abruptly");
            }
            throw new RuntimeException(e);
        }

        Status okMessage = new Status(MessageType.MSG_OK);
        try {
            out.write(okMessage.serialize());
            out.flush();
            System.out.println("Sent message " + okMessage + " to client " + this.socket.getPort());
            closeConnection(this);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
//        while (true) {
//            //  infinite
//        }
    }

    public static void closeConnection(ClientHandler clientHandler) throws IOException {
        clientHandler.in.close();
        clientHandler.out.close();
        clientHandler.socket.close();
    }
}
