package client;

import java.util.Arrays;
import java.util.Scanner;

import chess.*;
import client.exceptions.*;
import client.model.*;
import model.*;

import static chess.ChessGame.TeamColor.*;
import static dataaccess.sql.SQLGameDataAccess.*;
import static ui.EscapeSequences.*;

public class ChessClient {
    private final ServerFacade sf;
    private RegisterResult currentUserData;
    private State state = State.LOGGEDOUT;

    public ChessClient(String serverUrl) {
        sf = new ServerFacade(serverUrl);
        currentUserData = new RegisterResult(null, null);
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
                case "logout" -> logOut(currentUserData);
                case "create" -> createGame(params);
                case "list" -> listGames();
                case "join" -> joinGame(params);
                case "observe" -> observeGame(params);
                case "quit" -> "quit";
                default -> help();
            };
        } catch (Exception ex) {
            return ex.getMessage();
        }
    }

    public String register(String... params) throws Exception {
        if (params.length == 3) {
            RegisterRequest request = new RegisterRequest(params[0], params[1], params[2]);
            RegisterResult result = sf.register(request);
            currentUserData = result;
            state = State.LOGGEDIN;
            return String.format("You have registered as %s.", result.username());
        }
        throw new ResponseException(ResponseException.Code.ClientError, "Expected: register <USERNAME>, <PASSWORD>, <EMAIL>");
    }

    public String logIn(String... params) throws Exception {
        if (state == State.LOGGEDIN) {
            throw new ResponseException(ResponseException.Code.ClientError, "You are already logged in");
        }
        if (params.length == 2) {
            LogInRequest request = new LogInRequest(params[0], params[1]);
            System.out.println("Here 1");
            RegisterResult result = sf.logIn(request);
            System.out.println("Goal");
            currentUserData = result;
            state = State.LOGGEDIN;
            return String.format("You have logged in as %s.", result.username());
        }
        throw new ResponseException(ResponseException.Code.ClientError, "Expected: logIn <USERNAME>, <PASSWORD>");
    }

    public String logOut(RegisterResult currentUserData) throws Exception {
        try {
            assertSignedIn();
            sf.logOut(currentUserData);
            state = State.LOGGEDOUT;
            return "You have logged out";
        } catch (Exception ex) {
            throw new ResponseException(ResponseException.Code.ClientError, "You are not logged in");
        }
    }

    public String createGame(String... params) throws Exception {
        assertSignedIn();
        if (params.length == 1) {
            CreateGameRequest request = new CreateGameRequest(params[0], currentUserData.authToken());
            GameData gameData = sf.createGame(request);
            GameData currentGame = getGameData(gameData.gameID());
            ChessGame board = currentGame.game();
            printBoard(board.getBoard(), WHITE);
            return String.format("You have created the game %s.", params[0]);
        }
        throw new ResponseException(ResponseException.Code.ClientError, "Expected: create <GAME NAME>");
    }

    public String listGames() throws Exception {
        try {
            assertSignedIn();
            sf.listGames(currentUserData.authToken());
        } catch (ResponseException ex) {
            throw new ResponseException(ResponseException.Code.ClientError, "You are not logged in");
        }
        return "You are not logged in";
    }

    public String joinGame(String... params) throws Exception {
        try {
            assertSignedIn();
            if (params.length == 2) {
                int gameID = Integer.parseInt(params[0]);
                JoinGameRequest request = new JoinGameRequest(gameID, params[1], currentUserData.username(), currentUserData.authToken());
                sf.joinGame(request);
                return String.format("You have joined the game %s as %s", params[0], params[1]);
            }
        } catch (ResponseException ex) {
            throw new ResponseException(ResponseException.Code.ClientError, "You are not logged in");
        }
        return "You are not logged in";
    }

    public String observeGame(String... params) throws Exception {
        try {
            assertSignedIn();
            if (params.length == 1) {
                int gameID = Integer.parseInt(params[0]);
                JoinGameRequest request = new JoinGameRequest(gameID, "watch", currentUserData.username(), currentUserData.authToken());
                sf.joinGame(request);
                return String.format("You are observing the game %s", params[0]);
            }
        } catch (ResponseException ex) {
            throw new ResponseException(ResponseException.Code.ClientError, "You are not logged in");
        }
        return "You are not logged in";
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
                - join <ID> [WHITE | BLACK]
                - observe <ID>
                - logout
                - quit
                - help
                """;
    }

    private void assertSignedIn() throws Exception {
        if (state == State.LOGGEDOUT) {
            throw new ResponseException(ResponseException.Code.ClientError, "You must log in");
        }
    }

    private void printPrompt() {
        System.out.print("\n" + SET_TEXT_COLOR_BLACK + ">>> " + SET_TEXT_COLOR_GREEN);
    }

    private void printBoard(ChessBoard board, ChessGame.TeamColor teamColor) {
        String[] columns = {"a", "b", "c", "d", "e", "f", "g", "h"};
        String[] rows = {"1", "2", "3", "4", "5", "6", "7", "8"};
        boolean isWhite;
        isWhite = teamColor == WHITE;
        for (int i=0; i<8 ; i++) {
            int row = isWhite ? 1 + i : 8 - i;
            System.out.print(SET_BG_COLOR_DARK_GREY + " " + row + " " + RESET_BG_COLOR);
            for (int j=0; j<8 ; j++) {
                int column = isWhite ? 1 + i : 8 - i;
                System.out.print(SET_BG_COLOR_DARK_GREY + " " + column + " " + RESET_BG_COLOR);
                boolean lightSquare = (row + column) % 2 == 1;
                String bg = lightSquare ? SET_BG_COLOR_WHITE : SET_BG_COLOR_BLACK;
                ChessPiece piece = board.getPiece(new ChessPosition(row, column));
                System.out.print(bg + pieceIcon(piece) + RESET_BG_COLOR);
            }
            System.out.println(SET_BG_COLOR_DARK_GREY + " " + row + " " + RESET_BG_COLOR);
        }
    }

    private static String pieceIcon(ChessPiece piece) {
        if (piece == null) {
            return EMPTY;
        }
        return switch (piece.getTeamColor()) {
            case WHITE -> switch (piece.getPieceType()) {
                case KING -> WHITE_KING;
                case QUEEN -> WHITE_QUEEN;
                case BISHOP -> WHITE_BISHOP;
                case KNIGHT -> WHITE_KNIGHT;
                case ROOK -> WHITE_ROOK;
                case PAWN -> WHITE_PAWN;
                };
            case BLACK -> switch (piece.getPieceType()) {
                case KING -> BLACK_KING;
                case QUEEN -> BLACK_QUEEN;
                case BISHOP -> BLACK_BISHOP;
                case KNIGHT -> BLACK_KNIGHT;
                case ROOK -> BLACK_ROOK;
                case PAWN -> BLACK_PAWN;
            };
        };
    }
}