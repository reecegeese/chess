package chess;

import java.util.Collection;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
        return piece.pieceMoves(board, startPosition);
    }

    /**
     * Makes a move in the chess game
     *
     * @param move chess move to perform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        throw new RuntimeException("makeMove not implemented");
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

    //Find the king of the specified color
    public ChessPosition findKing(TeamColor teamColor) {
        for (int i=1; i<9; i++) {
            for (int j=1; j<9; j++) {
                ChessPiece piece = board.getPiece(new ChessPosition(i, j));
                if (piece != null && piece.getPieceType() == ChessPiece.PieceType.KING
                        && piece.getTeamColor() == teamColor) {
                    return new ChessPosition(i, j);
                }
            }
        }
        throw new Error("King not found");
    }

    //Boolean for if the given piece can be taken
    public boolean canBeTaken(TeamColor teamColor, ChessPosition myPosition) {
        //Check every square on the board
        for (int i=1; i<9; i++) {
            for (int j=1; j<9; j++) {
                ChessPosition enemyPosition = new ChessPosition(i, j);
                ChessPiece piece = board.getPiece(enemyPosition);
                //If piece is an opponent
                if (piece != null && piece.getTeamColor() != teamColor) {
                    //Get all of opponent's moves
                    Collection<ChessMove> possibleMoves = validMoves(enemyPosition);
                    //Check all enemy moves
                    for (ChessMove position : possibleMoves) {
                        //If opponent can take my piece
                        if (Objects.equals(enemyPosition, myPosition)) {
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
            //If down is in bounds and safe
            if (kingCanDown && !canBeTaken(teamColor, new ChessPosition(kingRow-1, kingColumn))) {
                return false;
            }
            //If up is in bounds and safe
            if (kingCanUp && !canBeTaken(teamColor, new ChessPosition(kingRow+1, kingColumn))) {
                return false;
            }
            //If left is in bounds and safe
            if (kingCanLeft && !canBeTaken(teamColor, new ChessPosition(kingRow, kingColumn-1))) {
                return false;
            }
            //If right is in bounds and safe
            if (kingCanRight && !canBeTaken(teamColor, new ChessPosition(kingRow, kingColumn+1))) {
                return false;
            }
            return true;
        }
        return false;
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves while not in check.
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
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
