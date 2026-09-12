package chess;

import static chess.ChessPiece.PieceType.*;
import static chess.ChessGame.TeamColor.*;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {
    //private final?
    private ChessPiece[][] boardArray = new ChessPiece[8][8];
    ChessPiece.PieceType[] backRowOrderWhite = {
            ROOK, KNIGHT, BISHOP, QUEEN, KING, BISHOP, KNIGHT, ROOK
    };
    ChessPiece.PieceType[] backRowOrderBlack = {
            ROOK, KNIGHT, BISHOP, KING, QUEEN, BISHOP, KNIGHT, ROOK
    };

    public ChessBoard() {
        resetBoard();
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        boardArray[position.getRow()][position.getColumn()] = piece;
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        return boardArray[position.getRow()][position.getColumn()];
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */

    public void resetBoard() {
        boardArray = new ChessPiece[8][8];
        for (int column=1; column<8; column++) {
            addPiece(new ChessPosition(1, column),
                    new ChessPiece(WHITE, backRowOrderWhite[column]));
            addPiece(new ChessPosition(2, column), new ChessPiece(WHITE, PAWN));
            addPiece(new ChessPosition(7, column), new ChessPiece(BLACK, PAWN));
            addPiece(new ChessPosition(8, column),
                    new ChessPiece(BLACK, backRowOrderBlack[column]));
        }
    }
}
