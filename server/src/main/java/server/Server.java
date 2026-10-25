package server;

import dataaccess.AlreadyTakenException;
import dataaccess.BadRequestException;
import dataaccess.UnauthorizedException;
import io.javalin.*;
import model.GameData;
import model.JoinGameInput;
import model.UserData;
import com.google.gson.Gson;
import services.DeleteService;
import services.GameService;
import services.UserService;

import java.util.List;
import java.util.Map;

public class Server {

    private final Javalin javalin;

    private final Gson serializer = new Gson();


    public Server() {
        javalin = Javalin.create(config -> config.staticFiles.add("web"));
        javalin.delete("/db", (ctx)-> {
            DeleteService.deleteService();
            ctx.status(200).result("");
        });
        javalin.post("/user", (ctx)-> {
            UserData user = serializer.fromJson(ctx.body(), UserData.class);
            try {
                String authToken = UserService.registerUser(user);
                String resultJson = serializer.toJson(Map.of("username", user.username()
                        , "authToken", authToken));
                ctx.status(200).result(resultJson);
            } catch  /*Server <- Handler*/ (BadRequestException e){
                String resultJson = serializer.toJson(Map.of("message", "Error: already taken"));
                ctx.status(400).result(resultJson);
            } catch  /*Server <- Handler*/ (AlreadyTakenException e){
                String resultJson = serializer.toJson(Map.of("message", "Error: already taken"));
                ctx.status(403).result(resultJson);
            }
        });
        javalin.post("/session", (ctx)-> {
            UserData user = serializer.fromJson(ctx.body(), UserData.class);
            try {
                String authToken = UserService.loginUser(user);
                String resultJson = serializer.toJson(Map.of("username", user.username()
                        , "authToken", authToken));
                ctx.status(200).result(resultJson);
            } catch  /*Server <- Handler*/ (BadRequestException e){
                String resultJson = serializer.toJson(Map.of("message", "Error: bad request"));
                ctx.status(400).result(resultJson);
            } catch  /*Server <- Handler*/ (UnauthorizedException e){
                String resultJson = serializer.toJson(Map.of("message", "Error: already taken"));
                ctx.status(401).result(resultJson);
            }
        });
        javalin.delete("/session", (ctx)-> {
            String[] headerParts = getHeaderParts(ctx.header("authorization"));
            try {
                UserService.logoutUser(headerParts[1]);
                ctx.status(200).result("");
            } catch  /*Server <- Handler*/ (UnauthorizedException e){
                String resultJson = serializer.toJson(Map.of("message", "Error: already taken"));
                ctx.status(401).result(resultJson);
            }
        });
        javalin.post("/game", (ctx)-> {
            GameData gameName = serializer.fromJson(ctx.body(), GameData.class);
            String[] headerParts = getHeaderParts(ctx.header("authorization"));
            try {
                GameData gameData = GameService.createGame(gameName.gameName(), headerParts[1]);
                String resultJson = serializer.toJson(Map.of("gameID", gameData.gameID()));
                ctx.status(200).result(resultJson);
            } catch  /*Server <- Handler*/ (BadRequestException e){
                String resultJson = serializer.toJson(Map.of("message", "Error: already taken"));
                ctx.status(400).result(resultJson);
            } catch  /*Server <- Handler*/ (UnauthorizedException e){
                String resultJson = serializer.toJson(Map.of("message", "Error: already taken"));
                ctx.status(401).result(resultJson);
            }
        });
            javalin.get("/game", (ctx)-> {
            String[] headerParts = getHeaderParts(ctx.header("authorization"));
            try {
                List<GameData> games = GameService.listGames(headerParts[1]);
                String resultJson = serializer.toJson(Map.of("games", games));
                ctx.status(200).result(resultJson);
            } catch  /*Server <- Handler*/ (UnauthorizedException e){
                String resultJson = serializer.toJson(Map.of("message", "Error: unauthorized"));
                ctx.status(401).result(resultJson);
            }
        });
        javalin.put("/game", (ctx)-> {
            JoinGameInput joinGameInput = serializer.fromJson(ctx.body(), JoinGameInput.class);
            String[] headerParts = getHeaderParts(ctx.header("authorization"));
            try {
                GameService.joinGame(joinGameInput.gameID(), joinGameInput.playerColor(), headerParts[0], headerParts[1]);
                String resultJson = serializer.toJson(Map.of("gameID", joinGameInput.gameID()));
                ctx.status(200).result(resultJson);
            } catch  /*Server <- Handler*/ (BadRequestException e){
                String resultJson = serializer.toJson(Map.of("message", "Error: bad request"));
                ctx.status(400).result(resultJson);
            } catch  /*Server <- Handler*/ (UnauthorizedException e){
                String resultJson = serializer.toJson(Map.of("message", "Error: unauthorized"));
                ctx.status(401).result(resultJson);
            } catch  /*Server <- Handler*/ (AlreadyTakenException e){
                String resultJson = serializer.toJson(Map.of("message", "Error: already taken"));
                ctx.status(403).result(resultJson);
            }
        });
    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }

    public static String[] getHeaderParts(String header) {
        String[] parts = header.split(",");
        String usernameToTrim = parts[0].trim();
        String username = usernameToTrim.replaceAll("[\":]", "");
        String authTokenToTrim = parts[1].trim();
        String authToken = authTokenToTrim.replaceAll("[\":]", "");
        return new String[] {username, authToken};
    }
}
