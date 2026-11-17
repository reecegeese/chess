package client.model;

import chess.ChessBoard;

public record CreateResult(ChessBoard board, String gameName, int gameID) {}