package server;

import io.javalin.*;
import model.AuthData;
import model.UserData;
import com.google.gson.Gson;
import services.DeleteService;
import services.UserService;
import dataaccess.authdataaccess.AuthDataAccess;
import dataaccess.gamedataaccess.GameDataAccess;
import dataaccess.userdataaccess.UserDataAccess;

public class Server {

    private final Javalin javalin;

    private final Gson serializer = new Gson();

    public Server() {
        javalin = Javalin.create(config -> config.staticFiles.add("web"));

        javalin.delete("/db", (ctx)-> {
            DeleteService.deleteService();
            ctx.status(200);
            ctx.result("");
        });

        javalin.post("/user", (ctx)-> {
            UserData user = serializer.fromJson(ctx.body(), UserData.class);
            String newUserResult = UserService.registerUser(user);
            ctx.status(200);
            ctx.result(newUserResult);
        });

        javalin.post("/session", (ctx)-> {
            UserData user = serializer.fromJson(ctx.body(), UserData.class);
            String loginResult = UserService.loginUser(user);
            ctx.status(200);
            ctx.result(loginResult);
        });
// ctx.header("authorization"),
        //Get authToken, not AuthData


        //AuthDataAccess AuthDA = new AuthDataAccess();
        //GameDataAccess GameDA = new GameDataAccess();
        //UserDataAccess UserDA = new UserDataAccess();

        // Register your endpoints and exception handlers here.
        //ctx.attribute("auth",authData)
        //ctx.header("authorization")
        //ctx.body()

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
