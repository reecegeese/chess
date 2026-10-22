package server;

import io.javalin.*;
import model.UserData;
import services.DeleteService;
import services.UserService;
import dataaccess.authdataaccess.AuthDataAccess;
import dataaccess.gamedataaccess.GameDataAccess;
import dataaccess.userdataaccess.UserDataAccess;

public class Server {

    private final Javalin javalin;

    public Server() {
        javalin = Javalin.create(config -> config.staticFiles.add("web"));

        javalin.delete("/db", (ctx)-> {
            DeleteService.deleteService();
        });

        javalin.post("/user", (ctx)-> ctx.attribute("userData",UserData(String(username), password, email)) {
            UserService.registerUser(username, password, email);
        });

        AuthDataAccess AuthDA = new AuthDataAccess();
        GameDataAccess GameDA = new GameDataAccess();
        UserDataAccess UserDA = new UserDataAccess();

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
