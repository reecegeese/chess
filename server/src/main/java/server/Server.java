package server;

import dataaccess.*;
import io.javalin.*;
import model.GameData;
import model.JoinGameInput;
import model.UserData;
import com.google.gson.Gson;
import services.DeleteService;
import services.GameService;
import services.UserService;
import static dataaccess.sql.SQLAuthDataAccess.getAuthData;

import java.util.List;
import java.util.Map;

public class Server {
    private final Javalin javalin;
    private final Gson serializer = new Gson();
    private String currentUsername;

    public Server() {
        try {
            DatabaseManager.createDatabase();
        } catch (DataAccessException e) {
            throw new RuntimeException();
        }
        javalin = Javalin.create(config -> config.staticFiles.add("web"));
        javalin.exception(BadRequestException.class, (e, ctx) ->
                ctx.status(400).result(errorJson("Error: bad request")));
        javalin.exception(UnauthorizedException.class, (e, ctx) ->
                ctx.status(401).result(errorJson("Error: unauthorized")));
        javalin.exception(AlreadyTakenException.class, (e, ctx) ->
                ctx.status(403).result(errorJson("Error: already taken")));
        javalin.exception(Exception.class, (e, ctx) ->
                ctx.status(500).result(errorJson("Error: internal error")));
        javalin.delete("/db", (ctx)-> {
            try {
                DeleteService.deleteService();
                ctx.status(200).result("");
            } catch  /*Server <- Handler*/ (RuntimeException e){
                String resultJson = serializer.toJson(Map.of("message", "Error: internal error"));
                ctx.status(500).result(resultJson);
            }

        });
        javalin.post("/user", (ctx)-> {
            UserData user = serializer.fromJson(ctx.body(), UserData.class);
            String authToken = UserService.registerUser(user);
            ctx.status(200).result(serializer.toJson(Map.of("username", currentUsername
                    , "authToken", authToken)));
        });
        javalin.post("/session", (ctx)-> {
            UserData user = serializer.fromJson(ctx.body(), UserData.class);
            String authToken = UserService.loginUser(user);
            ctx.status(200).result(serializer.toJson(Map.of("username", currentUsername
                    , "authToken", authToken)));
        });
        javalin.delete("/session", (ctx)-> {
            try {
                String authToken = ctx.header("authorization");
                UserService.logoutUser(authToken);
                ctx.status(200).result("");
            } catch  /*Server <- Handler*/ (UnauthorizedException e){
                String resultJson = serializer.toJson(Map.of("message", "Error: already taken"));
                ctx.status(401).result(resultJson);
            } catch  /*Server <- Handler*/ (RuntimeException e){
                String resultJson = serializer.toJson(Map.of("message", "Error: internal error"));
                ctx.status(500).result(resultJson);
            }
        });
        javalin.post("/game", (ctx)-> {
            GameData gameName = serializer.fromJson(ctx.body(), GameData.class);
            GameData gameData = GameService.createGame(gameName.gameName(), ctx.header("authorization"));
            if (ctx.header("authorization") == null) {
                throw new UnauthorizedException("Error: unauthorized");
            }
            ctx.status(200).result(serializer.toJson(Map.of("gameID", gameData.gameID(),
                    "gameName", gameData.gameName())));
        });
        javalin.get("/game", (ctx)-> {
            String authToken = ctx.header("authorization");
            List<GameData> games = GameService.listGames(authToken);
            ctx.status(200).result(serializer.toJson(Map.of("games", games)));
        });
        javalin.put("/game", (ctx)-> {
            JoinGameInput joinGameInput = serializer.fromJson(ctx.body(), JoinGameInput.class);
            String authToken = ctx.header("authorization");
            currentUsername = getAuthData(authToken).username();
            GameService.joinGame(joinGameInput.gameID(), joinGameInput.playerColor(), currentUsername, authToken);
            ctx.status(200).result(serializer.toJson(Map.of("gameID", joinGameInput.gameID())));
        });
    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }

    public static String[] getHeaderParts(String header) throws RuntimeException{
        String[] parts = header.split(",");
        if (parts.length != 2) {
            throw new RuntimeException("Error: internal error");
        }
        String usernameToTrim = parts[0].trim();
        String username = usernameToTrim.replaceAll("[\":]", "");
        String authTokenToTrim = parts[1].trim();
        String authToken = authTokenToTrim.replaceAll("[\":]", "");
        return new String[] {username, authToken};
    }

    private String errorJson(String message) {
        return serializer.toJson(Map.of("message", message));
    }
}
