package client;

import client.Repl;

public class ClientMain {
    static void main(String[] args) {
        var serverUrl = "http://localhost:8000";
        if (args.length == 1) {
            serverUrl = args[0];
        }
        new Repl(serverUrl).run();
    }
}
