package server;

import dataaccess.AlreadyTakenException;
import dataaccess.BadRequestException;
import dataaccess.UnauthorizedException;
import io.javalin.*;
import model.AuthData;
import model.UserData;
import com.google.gson.Gson;
import services.DeleteService;
import services.UserService;
import java.util.Map;

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
            String resultJson = serializer.toJson(Map.of("username", String.class));
            ctx.status(200).result(resultJson);
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
                String resultJson = serializer.toJson(Map.of("message", "Error: already taken"));
                ctx.status(400).result(resultJson);
            } catch  /*Server <- Handler*/ (UnauthorizedException e){
                // Client <- Server
                String resultJson = serializer.toJson(Map.of("message", "Error: already taken"));
                ctx.status(401).result(resultJson);
            }
        });

        //AuthDataAccess AuthDA = new AuthDataAccess();
        //GameDataAccess GameDA = new GameDataAccess();
        //UserDataAccess UserDA = new UserDataAccess();

        //ctx.attribute("auth",authData)
        //ctx.header("authorization")
        //ctx.body()
        // ctx.header("authorization"),
        //Get authToken, not AuthData

        //services talk with DAO
        //ctx.status(200)
        //ctx.result(data)

    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }
}
