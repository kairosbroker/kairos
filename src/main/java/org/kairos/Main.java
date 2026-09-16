package org.kairos;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        Logger.info(
                "Kairos waits for no one: like Perseus before Medusa, you must recognize the moment and act before it vanishes."
        );

        try {
            ArgParser serverArgs = new ArgParser(args);
            Server s = new Server(serverArgs);

            try {
                s.run();
            } catch (IOException e) {
                System.out.println("Fatal error");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
