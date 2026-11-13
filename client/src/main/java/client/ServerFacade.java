package client;

import client.exceptions.*;
import client.model.*;
import com.google.gson.Gson;

import java.net.*;
import java.net.http.*;
import java.net.http.HttpRequest.BodyPublisher;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse.BodyHandlers;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class ServerFacade {
    private final HttpClient client = HttpClient.newHttpClient();
    private final String serverUrl;

    public ServerFacade(String url) {
        serverUrl = url;
    }

    public RegisterResult register(RegisterRequest userData) throws ResponseException {
        var request = buildRequest("POST", "/user", userData);
        var response = sendRequest(request);
        return handleResponse(response, RegisterResult.class);
    }

    public void logIn(LogInRequest request) throws ResponseException {
        buildRequest("POST", "/session", request);
    }

    public void logOut() throws ResponseException {
        buildRequest("DELETE", "/session", "");
    }

    public void createGame(CreateRequest request) throws ResponseException {
        buildRequest("POST", "/game", request);
    }

    public void listGames() throws ResponseException {
        buildRequest("GET", "/game", "");
    }

    public void joinGame(JoinRequest request) throws ResponseException {
        buildRequest("PUT", "/game", request);
    }

    public void observeGame(ObserveRequest request) throws ResponseException {
        buildRequest("PUT", "/game", request);
    }

    private HttpRequest buildRequest(String method, String path, Object body) {
        var request = HttpRequest.newBuilder()
                .uri(URI.create(serverUrl + path))
                .method(method, makeRequestBody(body));
        if (body != null) {
            request.setHeader("Content-Type", "application/json");
        }
        return request.build();
    }

    private BodyPublisher makeRequestBody(Object request) {
        if (request != null) {
            return BodyPublishers.ofString(new Gson().toJson(request));
        } else {
            return BodyPublishers.noBody();
        }
    }

    private HttpResponse<String> sendRequest(HttpRequest request) throws ResponseException {
        try {
            return client.send(request, BodyHandlers.ofString());
        } catch (Exception ex) {
            throw new ResponseException(ResponseException.Code.ClientError, "Response Exception");
        }
    }

    private <T> T handleResponse(HttpResponse<String> response, Class<T> responseClass) throws ResponseException {
        var status = response.statusCode();
        if (!isSuccessful(status)) {
            var body = response.body();
            if (body != null) {
                throw ResponseException.fromJson(body);
            }
            throw new ResponseException(ResponseException.fromHttpStatusCode(status), "other failure: " + status);
        }
        if (responseClass != null) {
            return new Gson().fromJson(response.body(), responseClass);
        }
        return null;
    }

    private boolean isSuccessful(int status) {
        return status / 100 == 2;
    }
}