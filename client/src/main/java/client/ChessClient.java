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
    public sealed interface EvalResult {
        record EvalString(String text) implements EvalResult {}
        record EvalList(ListGamesResult list) implements EvalResult {}
    }

    public ChessClient(String serverUrl) {
        sf = new ServerFacade(serverUrl);
        currentUserData = new RegisterResult(null, null);
    }

    public void run() {
        System.out.println("♕ Welcome to 240 Chess. Type Help to get started ♕");
        System.out.print(help());
        Scanner scanner = new Scanner(System.in);
        EvalResult result = new EvalResult.EvalString("");
        while (!result.equals("quit")) {
            printPrompt();
            String line = scanner.nextLine();
            result = eval(line);
            try {
                switch (result) {
                    case EvalResult.EvalString s -> System.out.println(s.text());
                    case EvalResult.EvalList l    -> { for (var game : l.list().games()) {
                        System.out.println("Name: " + game.gameName() + " - ID: " + game.gameID()); }
                    }
                }
            } catch (Throwable e){
                var msg = e.toString();
                System.out.print(msg);
            }
        }
        System.out.println();
    }

    public EvalResult eval(String input) {
        try {
            String[] tokens = input.toLowerCase().split(" ");
            String cmd = (tokens.length > 0) ? tokens[0] : "help";
            String[] params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "register" -> new EvalResult.EvalString(register(params));
                case "login" -> new EvalResult.EvalString(logIn(params));
                case "logout" -> new EvalResult.EvalString(logOut(currentUserData));
                case "create" -> new EvalResult.EvalString(createGame(params));
                case "list" -> new EvalResult.EvalList(listGames());
                case "join" -> new EvalResult.EvalString(joinGame(params));
                case "observe" -> new EvalResult.EvalString(observeGame(params));
                case "quit" -> new EvalResult.EvalString("quit");
                default -> new EvalResult.EvalString(help());
            };
        } catch (Exception ex) {
            return new EvalResult.EvalString(ex.getMessage());
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
            RegisterResult result = sf.logIn(request);
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

    public ListGamesResult listGames() throws Exception {
        try {
            assertSignedIn();
            return sf.listGames(currentUserData.authToken());
        } catch (ResponseException ex) {
            throw new ResponseException(ResponseException.Code.ClientError, "You are not logged in");
        }
    }

    public String joinGame(String... params) throws Exception {
        try {
            assertSignedIn();
            if (params.length == 2) {
                int gameID = Integer.parseInt(params[0]);
                JoinGameRequest request = new JoinGameRequest(gameID, params[1], currentUserData.username(), currentUserData.authToken());
                sf.joinGame(request);
                ChessGame.TeamColor teamColor = ChessGame.TeamColor.valueOf(request.playerColor().toUpperCase());
                GameData currentGame = getGameData(gameID);
                ChessGame board = currentGame.game();
                printBoard(board.getBoard(), teamColor);
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
                GameData currentGame = getGameData(gameID);
                ChessGame board = currentGame.game();
                printBoard(board.getBoard(), WHITE);
                return String.format("You are observing game %s", params[0]);
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
        boolean isWhite = teamColor == WHITE;
        String[] columns = {"a", "b", "c", "d", "e", "f", "g", "h"};
        printHeader(columns, isWhite);
        boolean once = true;
        for (int i=0; i<8 ; i++) {
            int row = isWhite ? 1 + i : 8 - i;
            int column = isWhite ? 1 + i : 8 - i;
            System.out.print(SET_BG_COLOR_DARK_GREY + " " + row + " " + RESET_BG_COLOR);
            for (int j=0; j<8 ; j++) {
                boolean lightSquare = (row + column) % 2 == 1;
                String bg = lightSquare ? SET_BG_COLOR_WHITE : SET_BG_COLOR_BLACK;
                ChessPiece piece = board.getPiece(new ChessPosition(i+1, j+1));
                System.out.print(bg + pieceIcon(piece) + RESET_BG_COLOR);
            }
            System.out.println(SET_BG_COLOR_DARK_GREY + " " + row + " " + RESET_BG_COLOR);
        }
        printHeader(columns, isWhite);
    }

    private void printHeader(String[] columns, boolean isWhite) {
        System.out.print(SET_BG_COLOR_DARK_GREY + "   " + RESET_BG_COLOR);
        for (int n = 0; n < 8; n++) {
            String letter = isWhite ? columns[n] : columns[7 - n];
            System.out.print(SET_BG_COLOR_DARK_GREY + " " + letter + " " + RESET_BG_COLOR);
            if (n==0 || n==2 || n==3 || n==5 || n==6) {
                System.out.print(SET_BG_COLOR_DARK_GREY + " " + RESET_BG_COLOR);
            }
        }
        System.out.print(SET_BG_COLOR_DARK_GREY + "   " + RESET_BG_COLOR + "\n");
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