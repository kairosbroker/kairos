package org.kairos;

public class ArgParser
{
    private final int port;
    private final int max_clients;
    private final int max_threads;

    public ArgParser(String[] args) {
        if(args.length < 3)
            throw new RuntimeException("Kairos usage: java -jar <app_file>.jar PORT MAX_CLIENTS MAX_THREADS");

//  Also, why was this put here, and never used???? Not even set to a var or something;
//        parseIP( args[0] );
        this.port = parseIntValue(args[0], "port", 0, 65535);
        this.max_clients = parseIntValue(args[1], "max clients", 1, 99999);
        this.max_threads = parseIntValue(args[2], "max threads", 1, 99999);

    }

/* App is dockerized, so IP shouldn't matter,
but let's keep this commented out for a while, just to make sure we don't need it */

//    private void parseIP(String ip)
//    {
//        if(ip == null || ip.isEmpty())
//            throw new IllegalArgumentException("Arg validation: Invalid IP \"" + ip + "\"");
//    }

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
