package client.model;

public record JoinGameRequest(int gameID, String playerColor, String username, String authToken) {}