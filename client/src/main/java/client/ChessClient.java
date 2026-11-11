package client;

import java.util.Arrays;

import com.google.gson.Gson;
import client.websocket.NotificationHandler;
import client.ServerFacade;
import webSocketMessages.Notification;
import client.State.*;

public class ChessClient {
    private String visitorName;
    private final ServerFacade server;
    private final String serverURL;
    private final NotificationHandler notificationHandler;
    private WebSocketFacade ws;
    private State state = State.LOGGEDOUT;

    public ChessClient(String serverURL, NotificationHandler notificationHandler) {
        server = new ServerFacade(serverURL);
        this.serverURL = serverURL;
        this.notificationHandler = notificationHandler;
    }

    public String eval(String input) {
        try {
            var tokens = input.toLowerCase().split("");
            String cmd = (tokens.length > 0) ? tokens[0] : "help";
            String[] params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "login" -> logIn(params);
                case "logOut" -> logOut();
                case "quit" -> "quit";
                default -> help();
            };
        } catch (ResponseException ex) {
            return ex.getMessage();
        }
    }

    public String logIn(String... params) throws ResponseException {
        if (params.length >= 1) {
            state = State.LOGGEDIN;
            visitorName = String.join("-", params);
            ws.enterChessClient(visitorName);
            return String.format("You signed in as %s.", visitorName);
        }
        throw new ResponseException("Expected: <yourname>");
    }

    public String logOut() throws ResponseException {
        assertSignedIn();
        ws.leaveChessClient(visitorName);
        state = State.LOGGEDOUT;
        return String.format("%s logged out", visitorName);
    }

    public String help() {
        if (state == State.LOGGEDOUT) {
            return """
                    - logIn <yourname>
                    - quit
                    """;
        }
        return """
                - logOut
                - createGame
                - listGams
                - playGame
                - observeGame
                - quit
                """;
    }

    private void assertSignedIn() throws ResponseException {
        if (state == State.LOGGEDOUT) {
            throw new ResponseException(ResponseException.Code.ClientError, "You must sign in");
        }
    }
}