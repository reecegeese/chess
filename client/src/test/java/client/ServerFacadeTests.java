package client;

import client.model.*;
import model.*;
import dataaccess.*;
import services.*;
import org.junit.jupiter.api.*;
import server.Server;

import static org.junit.jupiter.api.Assertions.*;

public class ServerFacadeTests {
    private static Server server;
    static ServerFacade sf;
    private static RegisterRequest existingUser;
    private static RegisterResult loggedInUser;

    @BeforeAll
    public static void init() throws Exception {
        server = new Server();
        var port = server.run(0);
        sf = new ServerFacade("http://localhost:" + port);
        System.out.println("Started test HTTP server on " + port);
        existingUser = new RegisterRequest("player2", "1234", "p2@email.com");
    }

    @BeforeEach
    public void setup() throws Exception {
        DeleteService.deleteService();
        //one user already logged in
        loggedInUser = sf.register(existingUser);
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
        assertThrows(
                UnauthorizedException.class,
                () -> {
                    sf.logIn(new LogInRequest("player2", "noThanks"));
                }
        );
    }

    @Test
    @Order(5)
    @DisplayName("Good Logout")
    void goodLogOut() throws Exception {
        sf.logOut(loggedInUser);
    }

    @Test
    @Order(6)
    @DisplayName("Bad Logout")
    void badLogOut() throws Exception {
        sf.logOut(loggedInUser);
        assertThrows(UnauthorizedException.class,
                () -> sf.logOut(loggedInUser)
        );
    }

    @Test
    @Order(7)
    @DisplayName("Good Create Game")
    void goodCreateGame() throws Exception {
        RegisterResult logInResult = sf.logIn(new LogInRequest("player2", "1234"));
        GameData result = sf.createGame(new CreateGameRequest("testGame", logInResult.authToken()));
        assertEquals("testGame", result.gameName());
    }

    @Test
    @Order(8)
    @DisplayName("Bad Create Game")
    void badCreateGame() {
        assertThrows(UnauthorizedException.class,
                () -> sf.createGame(new CreateGameRequest("testGame", "bad token"))
        );
    }

    @Test
    @Order(8)
    @DisplayName("Good List Games")
    void goodListGames() throws Exception {
        RegisterResult logInResult = sf.logIn(new LogInRequest("player2", "1234"));
        CreateGameRequest createRequest = new CreateGameRequest("testGame", logInResult.authToken());
        sf.createGame(createRequest);
        sf.listGames(createRequest.authToken());
    }

    @Test
    @Order(9)
    @DisplayName("Bad List Games")
    void badListGames() throws Exception {
        RegisterResult logInResult = sf.logIn(new LogInRequest("player2", "1234"));
        CreateGameRequest createRequest = new CreateGameRequest("testGame", logInResult.authToken());
        sf.createGame(createRequest);
        assertThrows(UnauthorizedException.class,
                () -> sf.listGames("Bad authToken")
        );
    }

    @Test
    @Order(10)
    @DisplayName("Good Join Game")
    void goodJoinGames() throws Exception {
        RegisterResult logInResult = sf.logIn(new LogInRequest("player2", "1234"));
        GameData result = sf.createGame(new CreateGameRequest("testGame", logInResult.authToken()));
        JoinGameRequest joinGameRequest = new JoinGameRequest(result.gameID(), "white", "player2", logInResult.authToken());
        sf.joinGame(joinGameRequest);
    }

    @Test
    @Order(11)
    @DisplayName("Bad Join Game")
    void badJoinGames() throws Exception {
        RegisterResult logInResult = sf.logIn(new LogInRequest("player2", "1234"));
        GameData result = sf.createGame(new CreateGameRequest("testGame", logInResult.authToken()));
        JoinGameRequest joinGameRequest = new JoinGameRequest(result.gameID(), null, "player2", logInResult.authToken());
        assertThrows(BadRequestException.class,
                () -> sf.joinGame(joinGameRequest)
        );
    }

    @Test
    @Order(10)
    @DisplayName("Good Observe Game")
    void goodObserveGames() throws Exception {
        RegisterResult logInResult = sf.logIn(new LogInRequest("player2", "1234"));
        GameData result = sf.createGame(new CreateGameRequest("testGame", logInResult.authToken()));
        JoinGameRequest joinGameRequest = new JoinGameRequest(result.gameID(), "watch", "player2", logInResult.authToken());
        sf.joinGame(joinGameRequest);
    }

    @Test
    @Order(11)
    @DisplayName("Bad Observe Game")
    void badObserveGames() throws Exception {
        RegisterResult logInResult = sf.logIn(new LogInRequest("player2", "1234"));
        GameData result = sf.createGame(new CreateGameRequest("testGame", logInResult.authToken()));
        JoinGameRequest joinGameRequest = new JoinGameRequest(result.gameID(), "watch", "player2", "no authToken");
        assertThrows(UnauthorizedException.class,
                () -> sf.joinGame(joinGameRequest)
        );
    }
}