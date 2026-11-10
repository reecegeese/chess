package client;

import java.util.Scanner;

public class Repl /*implements NotificationHandler*/{
    //private final ChessClient client;

    public Repl(String serverUrl) {
        //client = new ChessClient(serverUrl, this);
    }

    public void run() {
        System.out.println("♕ Welcome to 240 Chess. Type Help to get started ♕");
        //System.out.print(client.help());

        //Scanner scanner = new Scanner(system.in);
        var result = "";
        while (!result.equals("quit")) {
            //printPrompt();
            //String line = Scanner.nextLine();
        }
    }
}