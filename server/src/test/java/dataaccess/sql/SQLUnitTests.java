package dataaccess.sql;

import dataaccess.AlreadyTakenException;
import dataaccess.BadRequestException;
import dataaccess.UnauthorizedException;
import org.junit.jupiter.api.*;
import passoff.model.*;
import server.Server;
import io.javalin.*;
import model.GameData;
import model.UserData;
import services.DeleteService;
import services.GameService;
import services.UserService;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertThrows;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SQLUnitTests {

    private static UserData existingUser;
    private static Server server;
    public static String registeredUser;

    @AfterAll
    static void stopServer() {
        server.stop();
    }

    @BeforeAll
    public static void init() {
        server = new Server();
        existingUser = new UserData("ExistingUser", "existingUserPassword", "eu@mail.com");
        Javalin.create(config -> config.staticFiles.add("web"));
    }

    @BeforeEach
    public void setup() throws BadRequestException, AlreadyTakenException {
        DeleteService.deleteService();
        //one user already logged in
        registeredUser = UserService.registerUser(existingUser);
    }

    @Test
    @Order(1)
    @DisplayName("Good Login")
    public void loginSuccess() throws UnauthorizedException, BadRequestException {
        String authToken = UserService.loginUser(existingUser);
        Assertions.assertNotNull(authToken, "Response did not return correct String");
    }

    @Test
    @Order(2)
    @DisplayName("Bad Login")
    public void loginFail() {
        assertThrows(
                UnauthorizedException.class,
                () -> {
                    UserService.loginUser(new UserData("IDontExist", "FakePassword", "fake@emai.com"));
                }
        );
    }

    @Test
    @Order(3)
    @DisplayName("Normal Logout")
    public void logoutSuccess() throws UnauthorizedException, BadRequestException {
        String authToken = UserService.loginUser(SQLUnitTests.existingUser);
        UserService.logoutUser(authToken);
    }

    @Test
    @Order(4)
    @DisplayName("Bad Logout")
    public void logoutFail() {
        assertThrows(
                UnauthorizedException.class,
                () -> {
                    UserService.logoutUser("Bad AuthToken");
                }
        );
    }

    @Test
    @Order(5)
    @DisplayName("Good Create Game")
    public void createGameSuccess() throws UnauthorizedException, BadRequestException {
        String authToken = UserService.loginUser(SQLUnitTests.existingUser);
        GameData createResult = GameService.createGame("testGame", authToken);
        Assertions.assertNull(createResult.whiteUsername());
        Assertions.assertNull(createResult.blackUsername());
        Assertions.assertEquals("testGame", createResult.gameName());
    }

    @Test
    @Order(6)
    @DisplayName("Bad Create Game")
    public void createGameFail() throws UnauthorizedException, BadRequestException {
        String authToken = UserService.loginUser(SQLUnitTests.existingUser);
        GameService.createGame("testGame", authToken);
        assertThrows(
                BadRequestException.class,
                () -> {
                    GameService.createGame(null, authToken);
                }
        );
    }

    @Test
    @Order(7)
    @DisplayName("Good Join Game")
    public void joinGameSuccess() throws UnauthorizedException, BadRequestException, AlreadyTakenException {
        String authToken = UserService.loginUser(SQLUnitTests.existingUser);
        GameData createResult = GameService.createGame("testGame", authToken);
        GameService.joinGame(createResult.gameID(), "WHITE", "iAmWhiteTeam", authToken);
        GameService.joinGame(createResult.gameID(), "BLACK", "whiteTeamAgain", authToken);
    }

    @Test
    @Order(8)
    @DisplayName("Bad Join Game")
    public void joinGameWhiteTaken() throws UnauthorizedException, BadRequestException, AlreadyTakenException {
        String authToken = UserService.loginUser(SQLUnitTests.existingUser);
        GameData createResult = GameService.createGame("testGame", authToken);
        GameService.joinGame(createResult.gameID(), "WHITE", "iAmWhiteTeam", authToken);
        assertThrows(
                AlreadyTakenException.class,
                () -> {
                    GameService.joinGame(createResult.gameID(), "WHITE", "whiteTeamAgain", authToken);
                }
        );
    }

    @Test
    @Order(9)
    @DisplayName("Good List Games")
    public void listGamesAuthorized() throws UnauthorizedException, BadRequestException {
        String authToken = UserService.loginUser(SQLUnitTests.existingUser);
        GameService.listGames(authToken);
    }

    @Test
    @Order(10)
    @DisplayName("Good List Multiple Games")
    public void listGamesMultiple() throws UnauthorizedException, BadRequestException {
        String authToken = UserService.loginUser(SQLUnitTests.existingUser);
        GameService.createGame("secondGame", authToken);
        GameService.createGame("thirdGame", authToken);
        GameService.listGames(authToken);
    }

    @Test
    @Order(11)
    @DisplayName("Bad List Games")
    public void listGamesUnauthorized() {
        assertThrows(
                UnauthorizedException.class,
                () -> {
                    GameService.listGames("wrongAuthToken");
                }
        );
    }

    @Test
    @Order(12)
    @DisplayName("Good Clear Games")
    public void clearGames() throws RuntimeException, UnauthorizedException, BadRequestException {
        UserService.loginUser(existingUser);
        DeleteService.deleteService();
    }
}