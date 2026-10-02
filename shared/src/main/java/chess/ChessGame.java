package chess;

import java.util.Collection;
import java.util.Objects;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

import static chess.ChessPiece.PieceType.*;
import static chess.ChessGame.TeamColor.*;

/**
 * A class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessGame chessGame = (ChessGame) o;
        return team == chessGame.team;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(team);
    }

    @Override
    public String toString() {
        return "ChessGame{}";
    }

    public TeamColor team;
    public ChessBoard board;

    public ChessGame() {
        board = new ChessBoard();
        board.resetBoard();
        setTeamTurn(TeamColor.WHITE);
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return team;
    }

    /**
     * Sets which teams turn it is
     //*
     * //@param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor color) {
        if (color == TeamColor.WHITE) {
            team = TeamColor.WHITE;
        } else {
            team = TeamColor.BLACK;
        }
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets all valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        if (board.getPiece(startPosition) == null) {
            return null;
        }
        ChessPiece piece = board.getPiece(startPosition);
        TeamColor teamColor = piece.getTeamColor();
        Collection<ChessMove> possibleMoves = piece.pieceMoves(board, startPosition);
        Collection<ChessMove> legalMoves = new ArrayList<>();
        //Check all moves
        for (ChessMove move : possibleMoves) {
            //Copy board
            ChessBoard boardCopy = new ChessBoard(board);
            //Applies move to boardCopy
            ChessPosition endPosition = move.getEndPosition();
            boardCopy.addPiece(endPosition, piece);
            boardCopy.removePiece(startPosition);
            //Move is valid if you do not end in check
            if (!isInCheckCopy(teamColor, boardCopy)) {
                legalMoves.add(move);
            }
        }
        return legalMoves;
    }

    /**
     * Makes a move in the chess game
     *
     * @param move chess move to perform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        ChessPosition startPosition = move.getStartPosition();
        ChessPosition endPosition = move.getEndPosition();
        //Get all valid moves for piece
        Collection<ChessMove> legalMoves = validMoves(new ChessPosition(startPosition.getRow(), startPosition.getColumn()));
        ChessPiece piece = board.getPiece(new ChessPosition(startPosition.getRow(), startPosition.getColumn()));
        if (legalMoves == null) {
            throw new InvalidMoveException("Move is not valid");
        }
        //For every valid move
        for (ChessMove legalMove : legalMoves) {
            TeamColor teamColor = piece.getTeamColor();
            TeamColor teamTurn = getTeamTurn();
            //Piece exists, the current move is a valid move, and it is the correct team's turn
            if (Objects.equals(legalMove, move)
                    && teamColor == teamTurn) {
                //Execute move
                board.addPiece(endPosition, piece);
                board.removePiece(startPosition);
                //Change team turn to other color
                if (teamColor == WHITE) {
                    setTeamTurn(BLACK);
                } else {
                    setTeamTurn(WHITE);
                }
                return;
            }
        }
        //If move is not valid throw error
        throw new InvalidMoveException("Move is not valid");
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        //Find king
        ChessPosition kingPosition = findKing(teamColor);
        //Can the king be taken?
        return canBeTaken(teamColor, kingPosition);
    }

    public boolean isInCheckCopy(TeamColor teamColor, ChessBoard boardCopy) {
        //Find king
        ChessPosition kingPosition = findKingCopy(teamColor, boardCopy);
        //Can the king be taken?
        return canBeTakenCopy(teamColor, kingPosition, boardCopy);
    }

    //Find the king of the specified color
    public ChessPosition findKing(TeamColor teamColor) {
        for (int row=1; row<9; row++) {
            for (int column=1; column<9; column++) {
                ChessPiece piece = board.getPiece(new ChessPosition(row, column));
                if (piece != null && piece.getPieceType() == ChessPiece.PieceType.KING
                        && piece.getTeamColor() == teamColor) {
                    return new ChessPosition(row, column);
                }
            }
        }
        throw new Error("King not found");
    }

    //Find the king of the specified color in boardCopy
    public ChessPosition findKingCopy(TeamColor teamColor, ChessBoard boardCopy) {
        for (int row=1; row<9; row++) {
            for (int column=1; column<9; column++) {
                ChessPiece piece = boardCopy.getPiece(new ChessPosition(row, column));
                if (piece != null && piece.getPieceType() == ChessPiece.PieceType.KING
                        && piece.getTeamColor() == teamColor) {
                    return new ChessPosition(row, column);
                }
            }
        }
        throw new Error("King not found");
    }

    //Boolean for if the given piece can be taken
    public boolean canBeTaken(TeamColor teamColor, ChessPosition myPosition) {
        //Check every square on the board
        for (int row=1; row<9; row++) {
            for (int column=1; column<9; column++) {
                ChessPosition enemyPosition = new ChessPosition(row, column);
                ChessPiece piece = board.getPiece(enemyPosition);
                //If piece is an opponent
                if (piece != null && piece.getTeamColor() != teamColor) {
                    //Get all of opponent's moves
                    Collection<ChessMove> possibleMoves = piece.pieceMoves(board, enemyPosition);
                    //If piece enemy is a pawn
                    if (piece.getPieceType() == PAWN) {
                        int enemyRow = enemyPosition.getRow();
                        int enemyColumn = enemyPosition.getColumn();
                        //Enemy is a black pawn
                        if (teamColor == WHITE) {
                            //Pawn can take myPosition by going down left
                            if (row > 2 && column > 1 && Objects.equals(new ChessPosition(enemyRow-1, enemyColumn-1), myPosition)) {
                                return true;
                            }
                            //Pawn can take myPosition by going down right
                            if (row > 2 && column < 8 && Objects.equals(new ChessPosition(enemyRow-1, enemyColumn+1), myPosition)) {
                                return true;
                            }
                            //Enemy is a white pawn
                        } else {
                            //Pawn can take myPosition by going up left
                            if (row < 8 && column > 1 && Objects.equals(new ChessPosition(enemyRow+1, enemyColumn-1), myPosition)) {
                                return true;
                            }
                            //Pawn can take myPosition by going up right
                            if (row < 8 && column < 8 && Objects.equals(new ChessPosition(enemyRow+1, enemyColumn+1), myPosition)) {
                                return true;
                            }
                        }
                    }
                    //Check all enemy moves
                    for (ChessMove move : possibleMoves) {
                        ChessPosition endPosition = move.getEndPosition();
                        //If opponent can take my piece
                        if (Objects.equals(endPosition, myPosition)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    //Boolean for if the given piece can be taken on the copy board
    public boolean canBeTakenCopy(TeamColor teamColor, ChessPosition myPosition, ChessBoard boardCopy) {
        //Check every square on the board
        for (int row=1; row<9; row++) {
            for (int column=1; column<9; column++) {
                ChessPosition enemyPosition = new ChessPosition(row, column);
                ChessPiece piece = boardCopy.getPiece(enemyPosition);
                //If piece is an opponent
                if (piece != null && piece.getTeamColor() != teamColor) {
                    //Get all of opponent's moves
                    Collection<ChessMove> possibleMoves = piece.pieceMoves(boardCopy, enemyPosition);
                    //If piece enemy is a pawn
                    if (piece.getPieceType() == PAWN) {
                        int enemyRow = enemyPosition.getRow();
                        int enemyColumn = enemyPosition.getColumn();
                        //Enemy is a black pawn
                        if (teamColor == WHITE) {
                            //Pawn can take myPosition by going down left
                            if (row > 2 && column > 1 && Objects.equals(new ChessPosition(enemyRow-1, enemyColumn-1), myPosition)) {
                                return true;
                            }
                            //Pawn can take myPosition by going down right
                            if (row > 2 && column < 8 && Objects.equals(new ChessPosition(enemyRow-1, enemyColumn+1), myPosition)) {
                                return true;
                            }
                            //Enemy is a white pawn
                        } else {
                            //Pawn can take myPosition by going up left
                            if (row < 8 && column > 1 && Objects.equals(new ChessPosition(enemyRow+1, enemyColumn-1), myPosition)) {
                                return true;
                            }
                            //Pawn can take myPosition by going up right
                            if (row < 8 && column < 8 && Objects.equals(new ChessPosition(enemyRow+1, enemyColumn+1), myPosition)) {
                                return true;
                            }
                        }
                    }
                    //Check all enemy moves
                    for (ChessMove move : possibleMoves) {
                        ChessPosition endPosition = move.getEndPosition();
                        //If opponent can take my piece
                        if (Objects.equals(endPosition, myPosition)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    /*Call valid moves on all pieces of teamColor
    If no valid moves for teamColor, teamCOlor is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        if (isInCheck(teamColor)) {
            ChessPosition kingPosition = findKing(teamColor);
            int kingRow = kingPosition.getRow();
            int kingColumn = kingPosition.getColumn();
            boolean kingCanUp = false;
            boolean kingCanDown = false;
            boolean kingCanRight = false;
            boolean kingCanLeft = false;
            if (kingRow < 8) {kingCanUp = true;}
            if (kingRow > 1) {kingCanDown = true;}
            if (kingColumn < 8) {kingCanRight = true;}
            if (kingColumn > 1) {kingCanLeft = true;}
            //If down is in bounds
            if (kingCanDown) {
                //If down is safe
                if (!canBeTaken(teamColor, new ChessPosition(kingRow-1, kingColumn))) {
                    return false;
                }
                //If down left is in bounds and safe
                if (kingCanLeft && !canBeTaken(teamColor, new ChessPosition(kingRow-1, kingColumn-1))) {
                    //This is the error
                    return false;
                }
                //If down right is in bounds and safe
                if (kingCanRight && !canBeTaken(teamColor, new ChessPosition(kingRow-1, kingColumn+1))) {
                    return false;
                }
            }
            //If up is in bounds
            if (kingCanUp) {
                //If up is safe
                if (!canBeTaken(teamColor, new ChessPosition(kingRow+1, kingColumn))) {
                    return false;
                }
                //If up left is in bounds and safe
                if (kingCanLeft && !canBeTaken(teamColor, new ChessPosition(kingRow+1, kingColumn-1))) {
                    return false;
                }
                //If up right is in bounds and safe
                if (kingCanRight && !canBeTaken(teamColor, new ChessPosition(kingRow+1, kingColumn+1))) {
                    return false;
                }
            }
            //If left is in bounds and safe
            if (kingCanLeft && !canBeTaken(teamColor, new ChessPosition(kingRow, kingColumn-1))) {
                return false;
            }
            //If right is in bounds and safe
            if (kingCanRight && !canBeTaken(teamColor, new ChessPosition(kingRow, kingColumn+1))) {
                return false;
            }
            //If no moves in bounds are safe, return isInCheckmate true
            return true;
        }
        //If not in check, return false
        return false;
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves while not in check.
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    /*Call valid moves on all pieces of teamColor
    If no valid moves for teamColor and NOT in check, stalemate
     */
    public boolean isInStalemate(TeamColor teamColor) {
        throw new RuntimeException("isInStalemate not implemented");
    }

    /**
     * Sets this game's chessboard to a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() { return board; }
}
