import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashSet;

public class ClientHandler extends Thread implements Runnable {
    private final Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private HashSet topicKeys;

    public ClientHandler(Socket socket) {
        this.socket = socket;
        this.topicKeys = new HashSet<String>();
    }
}
