package chess;

import java.util.Collection;
import java.util.Objects;
import java.util.List;
import java.util.ArrayList;

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
        return "ChessPiece{" +
                "pieceColor=" + pieceColor +
                ", pieceType=" + pieceType +
                '}';
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
        List<ChessMove> movesList = new ArrayList<>();
        ChessPiece piece = board.getPiece(myPosition);
        if (piece.getPieceType() == PieceType.PAWN) {
            pawnMoves(board, myPosition, movesList, piece);
        }
        return movesList;
    }

    public Collection<ChessMove> pawnMoves(ChessBoard board, ChessPosition myPosition,
                                           List<ChessMove> movesList, ChessPiece piece) {
        if (piece.getPieceType() == PieceType.PAWN) {
            if /*White team*/ (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
                if /*In bounds move*/ (myPosition.getRow() < 7) {
                    if /*Not blocked*/ (board.getPiece(new ChessPosition(myPosition.getRow()+1,myPosition.getColumn())) == null) {
                        movesList.add(new ChessMove(myPosition,new ChessPosition(myPosition.getRow()+1,myPosition.getColumn()), null));
                    }
                }
                if /*Initial move*/ (myPosition.getRow() == 2) {
                    if /*Not blocked*/ (board.getPiece(new ChessPosition(myPosition.getRow()+1,myPosition.getColumn())) == null
                            && board.getPiece(new ChessPosition(myPosition.getRow()+2,myPosition.getColumn())) == null) {
                        movesList.add(new ChessMove(myPosition,new ChessPosition(myPosition.getRow()+2,myPosition.getColumn()), null));
                    }
                } else if /*Promotion*/ (myPosition.getRow() == 7) {
                    if /*Not blocked*/ (board.getPiece(new ChessPosition(myPosition.getRow()+1,myPosition.getColumn())) == null) {
                        movesList.add(new ChessMove(myPosition,new ChessPosition(myPosition.getRow()+1,myPosition.getColumn()), PieceType.QUEEN));
                        movesList.add(new ChessMove(myPosition,new ChessPosition(myPosition.getRow()+1,myPosition.getColumn()), PieceType.BISHOP));
                        movesList.add(new ChessMove(myPosition,new ChessPosition(myPosition.getRow()+1,myPosition.getColumn()), PieceType.KNIGHT));
                        movesList.add(new ChessMove(myPosition,new ChessPosition(myPosition.getRow()+1,myPosition.getColumn()), PieceType.ROOK));
                    }

                    if /*Left may be out of bounds*/ (myPosition.getColumn() != 1) {
                        if /*Capture left promote*/ (board.getPiece(new ChessPosition(myPosition.getRow()+1, myPosition.getColumn()-1)) != null) {
                            ChessPiece capturePiece = board.getPiece(new ChessPosition(myPosition.getRow()+1, myPosition.getColumn()-1));
                            if /*Capture is black*/ (capturePiece.getTeamColor() == ChessGame.TeamColor.BLACK) {
                                movesList.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow()+1, myPosition.getColumn()-1), PieceType.QUEEN));
                                movesList.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow()+1, myPosition.getColumn()-1), PieceType.BISHOP));
                                movesList.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow()+1, myPosition.getColumn()-1), PieceType.KNIGHT));
                                movesList.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow()+1, myPosition.getColumn()-1), PieceType.ROOK));
                            }
                        }
                    }
                    if /*Right may be out of bounds*/ (myPosition.getColumn() != 8) {
                        if /*Capture right promote*/ (board.getPiece(new ChessPosition(myPosition.getRow()+1, myPosition.getColumn()+1)) != null) {
                            ChessPiece capturePiece = board.getPiece(new ChessPosition(myPosition.getRow()+1, myPosition.getColumn()+1));
                            if /*Capture is black*/ (capturePiece.getTeamColor() == ChessGame.TeamColor.BLACK) {
                                movesList.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow()+1, myPosition.getColumn()+1), PieceType.QUEEN));
                                movesList.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow()+1, myPosition.getColumn()+1), PieceType.BISHOP));
                                movesList.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow()+1, myPosition.getColumn()+1), PieceType.KNIGHT));
                                movesList.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow()+1, myPosition.getColumn()+1), PieceType.ROOK));
                            }
                        }
                    }
                }
                if /*Left may be out of bounds*/ (myPosition.getColumn() != 1 && myPosition.getRow() != 7) {
                    if /*Capture left*/ (board.getPiece(new ChessPosition(myPosition.getRow()+1, myPosition.getColumn()-1)) != null) {
                        ChessPiece capturePiece = board.getPiece(new ChessPosition(myPosition.getRow()+1, myPosition.getColumn()-1));
                        if /* Capture is black*/ (capturePiece.getTeamColor() == ChessGame.TeamColor.BLACK) {
                            movesList.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow()+1, myPosition.getColumn()-1), null));
                        }
                    }
                }
                if /*Right may be out of bounds*/ (myPosition.getColumn() != 8 && myPosition.getRow() != 7) {
                    if /*Capture right*/ (board.getPiece(new ChessPosition(myPosition.getRow()+1, myPosition.getColumn()+1)) != null) {
                        ChessPiece capturePiece = board.getPiece(new ChessPosition(myPosition.getRow()+1, myPosition.getColumn()+1));
                        if /* Capture is black*/ (capturePiece.getTeamColor() == ChessGame.TeamColor.BLACK) {
                            movesList.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow()+1, myPosition.getColumn()+1), null));
                        }
                    }
                }
            }
            else if /*Black team*/ (piece.getTeamColor() == ChessGame.TeamColor.BLACK) {
                if /*In bounds move*/ (myPosition.getRow() > 2) {
                    if /*Not blocked*/ (board.getPiece(new ChessPosition(myPosition.getRow()-1,myPosition.getColumn())) == null) {
                        movesList.add(new ChessMove(myPosition,new ChessPosition(myPosition.getRow()-1,myPosition.getColumn()), null));
                    }
                }
                if /*Initial move*/ (myPosition.getRow() == 7) {
                    if /*Not blocked*/ (board.getPiece(new ChessPosition(myPosition.getRow()-1,myPosition.getColumn())) == null
                            && board.getPiece(new ChessPosition(myPosition.getRow()-2,myPosition.getColumn())) == null) {
                        movesList.add(new ChessMove(myPosition,new ChessPosition(myPosition.getRow()-2,myPosition.getColumn()), null));
                    }
                } else if /*Promotion*/ (myPosition.getRow() == 2) {
                    if /*Not blocked*/ (board.getPiece(new ChessPosition(myPosition.getRow()-1,myPosition.getColumn())) == null) {
                        movesList.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn()), PieceType.QUEEN));
                        movesList.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn()), PieceType.BISHOP));
                        movesList.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn()), PieceType.KNIGHT));
                        movesList.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn()), PieceType.ROOK));
                    }
                    if /*Left may be out of bounds*/ (myPosition.getColumn() != 1) {
                        if /*Capture left promote*/ (board.getPiece(new ChessPosition(myPosition.getRow()-1, myPosition.getColumn()-1)) != null) {
                            ChessPiece capturePiece = board.getPiece(new ChessPosition(myPosition.getRow()-1, myPosition.getColumn()-1));
                            if /*Capture is white*/ (capturePiece.getTeamColor() == ChessGame.TeamColor.WHITE) {
                                movesList.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow()-1, myPosition.getColumn()-1), PieceType.QUEEN));
                                movesList.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow()-1, myPosition.getColumn()-1), PieceType.BISHOP));
                                movesList.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow()-1, myPosition.getColumn()-1), PieceType.KNIGHT));
                                movesList.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow()-1, myPosition.getColumn()-1), PieceType.ROOK));
                            }
                        }
                    }
                    if /*Right may be out of bounds*/ (myPosition.getColumn() != 8) {
                        if /*Capture right promote*/ (board.getPiece(new ChessPosition(myPosition.getRow()-1, myPosition.getColumn()+1)) != null) {
                            ChessPiece capturePiece = board.getPiece(new ChessPosition(myPosition.getRow()-1, myPosition.getColumn()+1));
                            if /*Capture is white*/ (capturePiece.getTeamColor() == ChessGame.TeamColor.WHITE) {
                                movesList.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow()-1, myPosition.getColumn()+1), PieceType.QUEEN));
                                movesList.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow()-1, myPosition.getColumn()+1), PieceType.BISHOP));
                                movesList.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow()-1, myPosition.getColumn()+1), PieceType.KNIGHT));
                                movesList.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow()-1, myPosition.getColumn()+1), PieceType.ROOK));
                            }
                        }
                    }
                }
                if /*Left may be out of bounds*/ (myPosition.getColumn() != 1 && myPosition.getRow() != 2) {
                    if /*Capture left*/ (board.getPiece(new ChessPosition(myPosition.getRow()-1, myPosition.getColumn()-1)) != null) {
                        ChessPiece capturePiece = board.getPiece(new ChessPosition(myPosition.getRow()-1, myPosition.getColumn()-1));
                        if /* Capture is white*/ (capturePiece.getTeamColor() == ChessGame.TeamColor.WHITE) {
                            movesList.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow()-1, myPosition.getColumn()-1), null));
                        }
                    }
                }
                if /*Right may be out of bounds*/ (myPosition.getColumn() != 8 && myPosition.getRow() != 2) {
                    if /*Capture right*/ (board.getPiece(new ChessPosition(myPosition.getRow()-1, myPosition.getColumn()+1)) != null) {
                        ChessPiece capturePiece = board.getPiece(new ChessPosition(myPosition.getRow()-1, myPosition.getColumn()+1));
                        if /* Capture is white*/ (capturePiece.getTeamColor() == ChessGame.TeamColor.WHITE) {
                            movesList.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow()-1, myPosition.getColumn()+1), null));
                        }
                    }
                }
            }
        }
        return movesList;
    }
}
