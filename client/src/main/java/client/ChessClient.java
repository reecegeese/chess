package client;

import java.util.Arrays;
import java.util.Scanner;
import client.exceptions.*;
import client.model.*;
import static ui.EscapeSequences.*;

public class ChessClient {
    private String visitorName;
    private final ServerFacade sf;
    private State state = State.LOGGEDOUT;

    public ChessClient(String serverUrl) {
        sf = new ServerFacade(serverUrl);
    }

    public void run() {
        System.out.println("♕ Welcome to 240 Chess. Type Help to get started ♕");
        System.out.print(help());
        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals("quit")) {
            printPrompt();
            String line = scanner.nextLine();
            try {
                result = eval(line);
                System.out.print(SET_TEXT_COLOR_GREEN + result);
            } catch (Throwable e){
                var msg = e.toString();
                System.out.print(msg);
            }
        }
        System.out.println();
    }

    public String eval(String input) {
        try {
            String[] tokens = input.toLowerCase().split(" ");
            String cmd = (tokens.length > 0) ? tokens[0] : "help";
            String[] params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "register" -> register(params);
                case "login" -> logIn(params);
                case "logout" -> logOut();
                case "quit" -> "quit";
                default -> help();
            };
        } catch (ResponseException ex) {
            return ex.getMessage();
        }
    }

    public String register(String... params) throws ResponseException {
        if (params.length == 3) {
            RegisterRequest request = new RegisterRequest(params[0], params[1], params[2]);
            sf.register(request);
            state = State.LOGGEDIN;
            return String.format("You have registered as %s.", params[0]);
        }
        throw new ResponseException("Expected: register <username>, <password>, <email>");
    }

    public String logIn(String... params) throws ResponseException {
        if (params.length == 2) {
            LogInRequest request = new LogInRequest(params[0], params[1]);
            sf.logIn(request);
            state = State.LOGGEDIN;
            return String.format("You have logged in as %s.", params[0]);
        }
        throw new ResponseException("Expected: logIn <username>, <password>");
    }

    public String logOut() {
        sf.logOut();
        state = State.LOGGEDOUT;
        return "You have logged out";
    }

    public String help() {
        if (state == State.LOGGEDOUT) {
            return """
                    - help
                    - register <username>, <password>, <email>
                    - login <username>, <password>
                    - quit
                    """;
        }
        return """
                - help
                - logout
                - quit
                """;
    }

    private void assertSignedIn() throws ResponseException {
        if (state == State.LOGGEDOUT) {
            throw new ResponseException("You must sign in");
        }
    }

    public void notify(Notification notification) {
        System.out.println(SET_TEXT_COLOR_RED + notification.message());
    }

    private void printPrompt() {
        System.out.print("\n" + SET_TEXT_COLOR_BLACK + ">>> " + SET_TEXT_COLOR_GREEN);
    }
}