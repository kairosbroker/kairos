package org.kairos;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private ExecutorService pool;
    private final ArgParser args;
    private final TopicManager topicManager = TopicManager.getInstance();

    public Server(ArgParser args) {
        this.args = args;
        this.pool = Executors.newFixedThreadPool(args.getMaxThreads());
    }

    public void run() throws IOException {
        ServerSocket ss = new ServerSocket(args.getPort());

        try {
            while (true) {
                Socket socket = ss.accept();
                ClientHandler ch = new ClientHandler(socket);
                pool.execute(ch);
            }
        } catch (IOException e) {
            System.out.println("Algo de errado não está certo");
        } finally {
            ss.close();
        }
    }
}
