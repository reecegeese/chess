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
                case "create" -> createGame(params);
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
        throw new ResponseException("Expected: register <USERNAME>, <PASSWORD>, <EMAIL>");
    }

    public String logIn(String... params) throws ResponseException {
        if (params.length == 2) {
            LogInRequest request = new LogInRequest(params[0], params[1]);
            sf.logIn(request);
            state = State.LOGGEDIN;
            return String.format("You have logged in as %s.", params[0]);
        }
        throw new ResponseException("Expected: logIn <USERNAME>, <PASSWORD>");
    }

    public String logOut() throws ResponseException {
        try {
            assertSignedIn();
            sf.logOut();
            state = State.LOGGEDOUT;
            return "You have logged out";
        } catch (ResponseException ex) {
            throw new ResponseException("You are not signed in");
        }
    }

    public String createGame(String... params) throws ResponseException {
        try {
            assertSignedIn();
            if (params.length == 1) {
                CreateRequest request = new CreateRequest(params[0]);
                sf.createGame(request);
                return String.format("You have created the game %s.", params[0]);
            }
        } catch (ResponseException ex) {
            throw new ResponseException("You are not signed in");
        }
        return "You are not signed in";
    }

    public String help() {
        if (state == State.LOGGEDOUT) {
            return """
                    - register <USERNAME>, <PASSWORD>, <EMAIL>
                    - login <USERNAME>, <PASSWORD>
                    - quit
                    - help
                    """;
        }
        return """
                - create <NAME>
                - list
                - join <ID> [WHITE][BLACK]
                - observe <ID>
                - logout
                - quit
                - help
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