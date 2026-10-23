package server;

import dataaccess.AlreadyTakenException;
import io.javalin.*;
import io.javalin.json.JsonMapper;
import model.AuthData;
import model.UserData;
import com.google.gson.Gson;
import services.DeleteService;
import services.UserService;
import dataaccess.authdataaccess.AuthDataAccess;
import dataaccess.gamedataaccess.GameDataAccess;
import dataaccess.userdataaccess.UserDataAccess;

import java.lang.reflect.Type;
import java.util.Map;

public class Server {

    private final Javalin javalin;

    private final Gson serializer = new Gson();


    public Server() {
        javalin = Javalin.create(config -> {
            config.staticFiles.add("web");
            config.jsonMapper(new JsonMapper() {
                @Override
                public String toJsonString(Object obj, Type type) {
                    return serializer.toJson(obj, type);
                }

                @Override
                public <T> T fromJsonString(String json, Type targetType) {
                    return serializer.fromJson(json, targetType);
                }
            });

        });

        //Client -> Server
        javalin.delete("/db", (ctx)-> {
            //Handler -> Service
            DeleteService.deleteService();
            // Client <- Server
            ctx.status(200).json(Map.of("", String.class));
        });

        //Client -> Server
        javalin.post("/user", (ctx)-> {
            //Server -> Handler
            String body = ctx.body();
            System.out.println("RAW BODY: " + body);
            UserData user = serializer.fromJson(ctx.body(), UserData.class);
            System.out.println("PARSED: " + user);
            try {
                //Handler -> Service
                String authToken = UserService.registerUser(user);
                // Client <- Server
                ctx.status(200).json(Map.of("username", user.username()
                , "authToken", authToken));
            } catch  /*Server <- Handler*/ (AlreadyTakenException e){
                // Client <- Server
                ctx.status(403).json(Map.of("message", "Error: already taken"));
            } catch (Exception e) {
                ctx.status(500).json(Map.of("message", "Error: (description of error)"));
            }
        });

        //Server -> Handler
        javalin.post("/session", (ctx)-> {
            UserData user = serializer.fromJson(ctx.body(), UserData.class);
            String loginResult = UserService.loginUser(user);
            ctx.status(200);
            ctx.result(loginResult);
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
