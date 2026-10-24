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
import java.util.ArrayList;

public class Server {

    private final Javalin javalin;

    private final Gson serializer = new Gson();


    public Server() {
        javalin = Javalin.create(config -> config.staticFiles.add("web"));

        //Client -> Server
        javalin.delete("/db", (ctx)-> {
            //Handler -> Service
            DeleteService.deleteService();
            // Client <- Server
            ctx.status(200).result("");
        });

        //Client -> Server
        javalin.post("/user", (ctx)-> {
            //Server -> Handler
            UserData user = serializer.fromJson(ctx.body(), UserData.class);
            try {
                //Handler -> Service
                String authToken = UserService.registerUser(user);
                // Client <- Server
                String resultJson = serializer.toJson(Map.of("username", user.username()
                        , "authToken", authToken));
                ctx.status(200).result(resultJson);
            } catch  /*Server <- Handler*/ (BadRequestException e){
                // Client <- Server
                String resultJson = serializer.toJson(Map.of("message", "Error: already taken"));
                ctx.status(400).result(resultJson);
            } catch  /*Server <- Handler*/ (AlreadyTakenException e){
                // Client <- Server
                String resultJson = serializer.toJson(Map.of("message", "Error: already taken"));
                ctx.status(403).result(resultJson);
            }
        });

        //Server -> Handler
        javalin.post("/session", (ctx)-> {
            UserData user = serializer.fromJson(ctx.body(), UserData.class);
            try {
                //Handler -> Service
                String authToken = UserService.loginUser(user);
                // Client <- Server
                String resultJson = serializer.toJson(Map.of("username", user.username()
                        , "authToken", authToken));
                ctx.status(200).result(resultJson);
            } catch  /*Server <- Handler*/ (BadRequestException e){
                // Client <- Server
                String resultJson = serializer.toJson(Map.of("message", "Error: bad request"));
                ctx.status(400).result(resultJson);
            } catch  /*Server <- Handler*/ (UnauthorizedException e){
                // Client <- Server
                String resultJson = serializer.toJson(Map.of("message", "Error: already taken"));
                ctx.status(401).result(resultJson);
            }
        });

        //Server -> Handler
        javalin.delete("/session", (ctx)-> {
            String header = ctx.header("authorization");
            String[] parts = header.split(",");
            String authTokenToTrim = parts[1].trim();
            String authToken = authTokenToTrim.replaceAll("[\":]", "");
            try {
                //Handler -> Service
                UserService.logoutUser(authToken);
                // Client <- Server
                ctx.status(200).result("");
            } catch  /*Server <- Handler*/ (UnauthorizedException e){
                // Client <- Server
                String resultJson = serializer.toJson(Map.of("message", "Error: already taken"));
                ctx.status(401).result(resultJson);
            }
        });

        //Server -> Handler
        javalin.post("/game", (ctx)-> {
            GameData gameName = serializer.fromJson(ctx.body(), GameData.class);
            String header = ctx.header("authorization");
            String[] parts = header.split(",");
            String authTokenToTrim = parts[1].trim();
            String authToken = authTokenToTrim.replaceAll("[\":]", "");
            try {
                //Handler -> Service
                GameData gameData = GameService.createGame(gameName.gameName(), authToken);
                // Client <- Server
                String resultJson = serializer.toJson(Map.of("gameID", gameData.gameID()));
                ctx.status(200).result(resultJson);
            } catch  /*Server <- Handler*/ (BadRequestException e){
                // Client <- Server
                String resultJson = serializer.toJson(Map.of("message", "Error: already taken"));
                ctx.status(400).result(resultJson);
            } catch  /*Server <- Handler*/ (UnauthorizedException e){
                // Client <- Server
                String resultJson = serializer.toJson(Map.of("message", "Error: already taken"));
                ctx.status(401).result(resultJson);
            }
        });

        //Server -> Handler
        javalin.get("/game", (ctx)-> {
            String header = ctx.header("authorization");
            String[] parts = header.split(",");
            String authTokenToTrim = parts[1].trim();
            String authToken = authTokenToTrim.replaceAll("[\":]", "");
            try {
                //Handler -> Service
                List<GameData> games = GameService.listGames(authToken);
                // Client <- Server
                String resultJson = serializer.toJson(Map.of("games", games));
                ctx.status(200).result(resultJson);
            } catch  /*Server <- Handler*/ (UnauthorizedException e){
                // Client <- Server
                String resultJson = serializer.toJson(Map.of("message", "Error: unauthorized"));
                ctx.status(401).result(resultJson);
            }
        });

        //Server -> Handler
        javalin.put("/game", (ctx)-> {
            JoinGameInput joinGameInput = serializer.fromJson(ctx.body(), JoinGameInput.class);
            String header = ctx.header("authorization");
            System.out.println("Join header: " + header);
            String[] parts = header.split(",");
            String usernameToTrim = parts[0].trim();
            String username = usernameToTrim.replaceAll("[\":]", "");
            String authTokenToTrim = parts[1].trim();
            String authToken = authTokenToTrim.replaceAll("[\":]", "");
            try {
                //Handler -> Service
                GameService.joinGame(joinGameInput.gameID(), joinGameInput.playerColor(), username, authToken);
                // Client <- Server
                String resultJson = serializer.toJson(Map.of("gameID", joinGameInput.gameID()));
                ctx.status(200).result(resultJson);
            } catch  /*Server <- Handler*/ (BadRequestException e){
                // Client <- Server
                String resultJson = serializer.toJson(Map.of("message", "Error: bad request"));
                ctx.status(400).result(resultJson);
            } catch  /*Server <- Handler*/ (UnauthorizedException e){
                // Client <- Server
                String resultJson = serializer.toJson(Map.of("message", "Error: unauthorized"));
                ctx.status(401).result(resultJson);
            } catch  /*Server <- Handler*/ (AlreadyTakenException e){
                // Client <- Server
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
}
