public class ArgParser {
    private final int port;
    private final int max_clients;
    private final int max_threads;

    public ArgParser(String[] args) {
        if (args.length < 4)
            throw new RuntimeException("Missing args: IP port maxclients maxthreads");

        parseIP(args[0]);
        this.port = parseIntValue(args[1], "port", 0, 65535);
        this.max_clients = parseIntValue(args[2], "max clients", 1, 99999);
        this.max_threads = parseIntValue(args[3], "max threads", 1, 99999);

    }

    private void parseIP(String ip) {
        if (ip == null || ip.isEmpty())
            throw new IllegalArgumentException("Arg validation: Invalid IP \"" + ip + "\"");
    }

    private static int parseIntValue(String argString, String argName, int minBound, int maxBound) {
        int _value;

        try {
            _value = Integer.parseInt(argString);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Arg validation: Invalid value \"" + argString + "\" for argument \"" + argName + "\"");
        }

        if (_value < minBound || _value > maxBound) { //  (port < 49152 || port > 65535 )
            throw new IllegalArgumentException("Arg validation: " + argName + " out-of-range [" + minBound + "-" + maxBound + "]");
        }

        return _value;
    }

    public int getPort() {
        return port;
    }

    public int getMaxClients() {
        return max_clients;
    }

    public int getMaxThreads() {
        return max_threads;
    }
}
