package server;

import dataaccess.*;
import io.javalin.*;
import model.*;
import com.google.gson.Gson;
import services.*;
import static dataaccess.sql.SQLAuthDataAccess.*;
import static dataaccess.sql.SQLGameDataAccess.*;

import java.util.List;
import java.util.Map;

public class Server {
    private final Javalin javalin;
    private final Gson serializer = new Gson();

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
            String currentUsername = user.username();
            ctx.status(200).result(serializer.toJson(Map.of("username", currentUsername
                    , "authToken", authToken)));
        });
        javalin.post("/session", (ctx)-> {
            UserData user = serializer.fromJson(ctx.body(), UserData.class);
            String authToken = UserService.loginUser(user);
            String currentUsername = user.username();
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
                    "gameName", gameData.gameName(), "board", gameData.game().getBoard())));
        });
        javalin.get("/game", (ctx)-> {
            String authToken = ctx.header("authorization");
            List<GameData> games = GameService.listGames(authToken);
            ctx.status(200).result(serializer.toJson(Map.of("games", games)));
        });
        javalin.put("/game", (ctx)-> {
            JoinGameInput joinGameInput = serializer.fromJson(ctx.body(), JoinGameInput.class);
            String authToken = ctx.header("authorization");
            var auth = getAuthData(authToken);
            if (joinGameInput.playerColor() == null) {
                ctx.status(400).result(serializer.toJson(Map.of("message", "Error: no team color provided")));
                return;
            }
            if (auth == null) {
                ctx.status(401).result(serializer.toJson(Map.of("message", "Error: unauthorized")));
                return;
            }
            GameService.joinGame(joinGameInput.gameID(), joinGameInput.playerColor().toUpperCase(), auth.username(), authToken);
            GameData gameData = getGameData(joinGameInput.gameID());
            ctx.status(200).result(serializer.toJson(Map.of("gameID", gameData.gameID(),
                    "gameName", gameData.gameName(), "board", gameData.game().getBoard())));
        });
    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }

    private String errorJson(String message) {
        return serializer.toJson(Map.of("message", message));
    }
}