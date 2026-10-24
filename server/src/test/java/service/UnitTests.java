package service;

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
public class UnitTests {

    private static UserData existingUser;
    private static Server server;

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
        UserService.registerUser(existingUser);
    }

    @Test
    @Order(1)
    @DisplayName("Normal User Login")
    public void loginSuccess() throws UnauthorizedException, BadRequestException {
        String loginResult = UserService.loginUser(existingUser);
        String[] parts = loginResult.split(",");
        String usernameToTrim = parts[0].trim();
        String username = usernameToTrim.replaceAll("[\":]", "");
        String authTokenToTrim = parts[1].trim();
        String authToken = authTokenToTrim.replaceAll("[\":]", "");
        String existingAuthTokenToTrim = parts[1].trim();
        String existingAuthToken = existingAuthTokenToTrim.replaceAll("[\":]", "");
        Assertions.assertEquals("ExistingUser", username);
        Assertions.assertNotNull(loginResult, "Response did not return correct String");
    }

    @Test
    @Order(2)
    @DisplayName("Normal User Login")
    public void loginFail() throws UnauthorizedException, BadRequestException {
        String loginResult = UserService.loginUser(existingUser);
        String[] parts = loginResult.split(",");
        String usernameToTrim = parts[0].trim();
        String username = usernameToTrim.replaceAll("[\":]", "");
        String authTokenToTrim = parts[1].trim();
        String authToken = authTokenToTrim.replaceAll("[\":]", "");
        String existingAuthTokenToTrim = parts[1].trim();
        String existingAuthToken = existingAuthTokenToTrim.replaceAll("[\":]", "");
        UnauthorizedException unauthorizedException = assertThrows(
                UnauthorizedException.class,
                () -> {
                    UserService.loginUser(new UserData("IDontExist", "FakePassword", "fake@emai.com"));
                }
        );
    }

    @Test
    @Order(3)
    @DisplayName("Normal Create Game")
    public void createGameSuccess() throws UnauthorizedException, BadRequestException {
        String loginResult = UserService.loginUser(UnitTests.existingUser);
        String[] parts = loginResult.split(",");
        String authTokenToTrim = parts[1].trim();
        String authToken = authTokenToTrim.replaceAll("[\":]", "");
        GameData createResult = GameService.createGame("testGame", authToken);
        Assertions.assertEquals(null, createResult.whiteUsername());
        Assertions.assertEquals(null, createResult.blackUsername());
        Assertions.assertEquals("testGame", createResult.gameName());
        Assertions.assertEquals(null, createResult.game());
    }

    @Test
    @Order(4)
    @DisplayName("Bad Create Game")
    public void createGameFail() throws UnauthorizedException, BadRequestException {
        String loginResult = UserService.loginUser(UnitTests.existingUser);
        String[] parts = loginResult.split(",");
        String authTokenToTrim = parts[1].trim();
        String authToken = authTokenToTrim.replaceAll("[\":]", "");
        GameService.createGame("testGame", authToken);
        BadRequestException badRequestException = assertThrows(
                BadRequestException.class,
                () -> {
                    GameService.createGame(null, authToken);
                }
        );
    }

    @Test
    @Order(5)
    @DisplayName("Bad Join Game")
    public void joinGameWhiteTaken() throws UnauthorizedException, BadRequestException, AlreadyTakenException {
        String loginResult = UserService.loginUser(UnitTests.existingUser);
        String[] parts = loginResult.split(",");
        String authTokenToTrim = parts[1].trim();
        String authToken = authTokenToTrim.replaceAll("[\":]", "");
        GameData createResult = GameService.createGame("testGame", authToken);
        GameService.joinGame(createResult.gameID(), "WHITE", "iAmWhiteTeam", authToken);
        AlreadyTakenException alreadyTakenException = assertThrows(
                AlreadyTakenException.class,
                () -> {
                    GameService.joinGame(createResult.gameID(), "WHITE", "whiteTeamAgain", authToken);
                }
        );
    }

    @Test
    @Order(6)
    @DisplayName("Good Join Game")
    public void joinGameSuccess() throws UnauthorizedException, BadRequestException, AlreadyTakenException {
        String loginResult = UserService.loginUser(UnitTests.existingUser);
        String[] parts = loginResult.split(",");
        String authTokenToTrim = parts[1].trim();
        String authToken = authTokenToTrim.replaceAll("[\":]", "");
        GameData createResult = GameService.createGame("testGame", authToken);
        GameService.joinGame(createResult.gameID(), "WHITE", "iAmWhiteTeam", authToken);
        GameService.joinGame(createResult.gameID(), "BLACK", "whiteTeamAgain", authToken);
    }

    @Test
    @Order(7)
    @DisplayName("Good List Games")
    public void listGamesAuthorized() throws UnauthorizedException, BadRequestException {
        String loginResult = UserService.loginUser(UnitTests.existingUser);
        String[] parts = loginResult.split(",");
        String authTokenToTrim = parts[1].trim();
        String authToken = authTokenToTrim.replaceAll("[\":]", "");
        GameService.listGames(authToken);
    }

    @Test
    @Order(8)
    @DisplayName("Bad List Games")
    public void listGamesUnauthorized() throws UnauthorizedException, BadRequestException {
        String loginResult = UserService.loginUser(UnitTests.existingUser);
        String[] parts = loginResult.split(",");
        String authTokenToTrim = parts[1].trim();
        UnauthorizedException unauthorizedException = assertThrows(
                UnauthorizedException.class,
                () -> {
                    GameService.listGames("wrongAuthToken");
                }
        );
    }

    @Test
    @Order(9)
    @DisplayName("Normal Logout")
    public void logoutSuccess() throws UnauthorizedException, BadRequestException {
        String loginResult = UserService.loginUser(UnitTests.existingUser);
        String[] parts = loginResult.split(",");
        String authTokenToTrim = parts[1].trim();
        String authToken = authTokenToTrim.replaceAll("[\":]", "");
        UserService.logoutUser(authToken);
    }

    @Test
    @Order(10)
    @DisplayName("Bad Logout")
    public void logoutFail() throws UnauthorizedException, BadRequestException {
        String loginResult = UserService.loginUser(UnitTests.existingUser);
        String[] parts = loginResult.split(",");
        String authTokenToTrim = parts[1].trim();
        UnauthorizedException unauthorizedException = assertThrows(
                UnauthorizedException.class,
                () -> {
                    UserService.logoutUser("Bad AuthToken");
                }
        );
    }
}