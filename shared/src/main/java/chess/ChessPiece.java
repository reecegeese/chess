package chess;

import java.util.Collection;
import java.util.Objects;
import java.util.List;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessPiece that = (ChessPiece) o;
        return pieceColor == that.pieceColor && pieceType == that.pieceType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceColor, pieceType);
    }

    @Override
    public String toString() {
        return "ChessPiece{" + "pieceColor=" + pieceColor +
                ", pieceType=" + pieceType + '}';
    }

    public ChessGame.TeamColor pieceColor;
    public ChessPiece.PieceType pieceType;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.pieceType = type;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return pieceType;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        ChessPiece piece = board.getPiece(myPosition);
        if (piece.getPieceType() == PieceType.PAWN) {
            if /*White team*/ (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
                if /*Initial move*/ (myPosition.getRow() == 2) {
                    return List.of((new ChessMove(myPosition,new ChessPosition(myPosition.getRow()+1,myPosition.getColumn()), null)),
                    new ChessMove(myPosition,new ChessPosition(myPosition.getRow()+2,myPosition.getColumn()), null));
                } else if /*Promotion*/ (myPosition.getRow()+1 == 8) {
                    return List.of((new ChessMove(myPosition,new ChessPosition(myPosition.getRow()+1,myPosition.getColumn()), PieceType.ROOK)),
                            (new ChessMove(myPosition,new ChessPosition(myPosition.getRow()+1,myPosition.getColumn()), PieceType.KNIGHT)),
                            (new ChessMove(myPosition,new ChessPosition(myPosition.getRow()+1,myPosition.getColumn()), PieceType.BISHOP)),
                            (new ChessMove(myPosition,new ChessPosition(myPosition.getRow()+1,myPosition.getColumn()), PieceType.QUEEN)));
                }
                return List.of(new ChessMove(myPosition,new ChessPosition(myPosition.getRow()+1,myPosition.getColumn()), null));
            }
        }
        return List.of();
    }
}
