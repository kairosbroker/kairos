import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashSet;

public class ClientHandler extends Thread implements Runnable {
    private final Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    //    private HashSet topicKeys;
    private Topic subscribedTopic;

    public ClientHandler(Socket socket) {
        this.socket = socket;
//        this.topicKeys = new HashSet<String>();

        try {
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
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
    }
}
