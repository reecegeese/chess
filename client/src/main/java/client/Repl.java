package client;

import java.util.Scanner;
import client.ChessClient.*;
import static ui.EscapeSequences.*;

public class Repl /*implements NotificationHandler*/{
    private final ChessClient client;

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
            try {
                result = client.eval(line);
                System.out.print(SET_TEXT_COLOR_BLUE + result);
            } catch (Throwable e){
                var msg = e.toString();
                System.out.print(msg);
            }
        }
        System.out.println();
    }

    public void notify(Notification notification) {
        System.out.println(SET_TEXT_COLOR_RED + notification.message());

    }
}