package chess;

import java.util.Arrays;
import java.util.Objects;

import static chess.ChessPiece.PieceType.*;
import static chess.ChessGame.TeamColor.*;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessBoard that = (ChessBoard) o;
        return Objects.deepEquals(boardArray, that.boardArray);
    }

    @Override
    public int hashCode() {
        return Objects.hash(Arrays.deepHashCode(boardArray));
    }

    @Override
    public String toString() {
        return "ChessBoard{" + "boardArray=" + Arrays.deepToString(boardArray) + '}';
    }

    //do not make static
    public ChessPiece[][] boardArray;
    ChessPiece.PieceType[] backRowOrder = {
            ROOK, KNIGHT, BISHOP, QUEEN, KING, BISHOP, KNIGHT, ROOK
    };


    public ChessBoard() {
        boardArray = new ChessPiece[8][8];
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        boardArray[position.getRow()-1][position.getColumn()-1] = piece;
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        if (boardArray[position.getRow()-1][position.getColumn()-1] == null ||
                position.getRow()-1 < 1 || position.getRow()-1 > 8 ||
                position.getColumn()-1 < 1 || position.getColumn()-1 > 8) {
            return null;
        }
        return boardArray[position.getRow()-1][position.getColumn()-1];
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */

    public void resetBoard() {
        boardArray = new ChessPiece[8][8];
        for (int column=1; column<=8; column++) {
            addPiece(new ChessPosition(1, column),
                    new ChessPiece(WHITE, backRowOrder[column-1]));
            addPiece(new ChessPosition(2, column), new ChessPiece(WHITE, PAWN));
            addPiece(new ChessPosition(7, column), new ChessPiece(BLACK, PAWN));
            addPiece(new ChessPosition(8, column),
                    new ChessPiece(BLACK, backRowOrder[column-1]));
        }
    }
}
