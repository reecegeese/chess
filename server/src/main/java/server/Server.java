package server;

import io.javalin.*;
import services.DeleteService;
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
