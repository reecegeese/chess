package client;

import client.model.*;
import org.junit.jupiter.api.*;
import server.Server;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class ServerFacadeTests {
    private static Server server;
    static ServerFacade sf;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        sf = new ServerFacade("http://localhost:" + port);
        System.out.println("Started test HTTP server on " + port);
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }


    @Test
    @Order(1)
    @DisplayName("Easy Test")
    public void sampleTest() {
        assertTrue(true);
    }

    @Test
    @Order(2)
    @DisplayName("Good Register User")
    void register() throws Exception {
        RegisterResult result = sf.register(new RegisterRequest("player1", "password", "p1@email.com"));
        System.out.println(result);
        //assertTrue(result.authToken().length() > 10);
        assertEquals("player1", result.username());
    }

}