package client;

import client.exceptions.ResponseException;
import client.model.*;
import model.*;
import dataaccess.AlreadyTakenException;
import dataaccess.BadRequestException;
import dataaccess.UnauthorizedException;
import org.junit.jupiter.api.*;
import server.Server;
import services.DeleteService;
import services.UserService;

import static org.junit.jupiter.api.Assertions.*;


public class ServerFacadeTests {
    private static Server server;
    static ServerFacade sf;
    private static RegisterRequest existingUser;
    private static RegisterRequest existingResult;
    private static String existingAuthToken;

    @BeforeAll
    public static void init() throws ResponseException {
        server = new Server();
        var port = server.run(0);
        sf = new ServerFacade("http://localhost:" + port);
        System.out.println("Started test HTTP server on " + port);
        existingUser = new RegisterRequest("player2", "1234", "p2@email.com");
    }

    @BeforeEach
    public void setup() throws ResponseException {
        DeleteService.deleteService();
        //one user already logged in
        sf.register(existingUser);
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
        assertEquals("player1", result.username());
    }

    @Test
    @Order(3)
    @DisplayName("Good Login User")
    void goodLogIn() throws Exception {
        RegisterResult result = sf.logIn(new LogInRequest("player2", "1234"));
        assertEquals("player2", result.username());
    }

    @Test
    @Order(4)
    @DisplayName("Bad Login User")
    void badLogIn() {
        assertThrows(IllegalArgumentException.class,
                () -> sf.logIn(new LogInRequest("player2", "noThanks"))
        );
    }

    @Test
    @Order(5)
    @DisplayName("Good Logout")
    void goodLogOut() throws Exception {
        sf.logIn(new LogInRequest("player2", "1234"));
        sf.logOut();
    }

    @Test
    @Order(6)
    @DisplayName("Bad Logout")
    void badLogOut() throws Exception {
        sf.logIn(new LogInRequest("player2", "1234"));
        sf.logOut();
        assertThrows(IllegalArgumentException.class,
                () -> sf.logIn(new LogInRequest("player2", "noThanks"))
        );
    }

    @Test
    @Order(7)
    @DisplayName("Good Create Game")
    void goodCreateGame() throws Exception {
        RegisterResult logInResult = sf.logIn(new LogInRequest("player2", "1234"));
        GameData result = sf.createGame(new CreateGameRequest("testGame", logInResult.authToken()));
        System.out.println("Assert Equal");
        assertEquals("testGame", result.gameName());
    }
}