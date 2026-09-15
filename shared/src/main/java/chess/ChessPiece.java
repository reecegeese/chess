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
        PieceType pieceType = piece.getPieceType();
        int pieceRow = myPosition.getRow();
        int pieceColumn = myPosition.getColumn();
        ChessGame.TeamColor teamColor = piece.getTeamColor();
        if (pieceType == PieceType.PAWN) {
            pawnMoves(board, myPosition, movesList, piece, pieceRow, pieceColumn, teamColor);
        } else if (pieceType == PieceType.ROOK) {
            rookMoves(board, myPosition, movesList, piece, pieceRow, pieceColumn, teamColor);
        }
        return movesList;
    }

    public Collection<ChessMove> rookMoves(ChessBoard board, ChessPosition myPosition,
                                           List<ChessMove> movesList, ChessPiece piece,
                                           int pieceRow, int pieceColumn,
                                           ChessGame.TeamColor teamColor) {
        for /*Rook moving up*/ (int n = pieceRow; n < 8; n++) {
            if /*Not blocked*/ (board.getPiece(new ChessPosition(n+1,pieceColumn)) == null) {
                movesList.add(new ChessMove(myPosition,new ChessPosition(n+1,pieceColumn), null));
            } else if /*Blocked by opposite color*/ ((board.getPiece(new ChessPosition(n+1,pieceColumn))).getTeamColor() != teamColor) {
                movesList.add(new ChessMove(myPosition,new ChessPosition(n+1,pieceColumn), null));
            } else /*Blocked by same color*/ {
                break;
            }
        }
        for /*Rook moving down*/ (int n = pieceRow; n > 1; n--) {
            if /*Not blocked*/ (board.getPiece(new ChessPosition(n-1,pieceColumn)) == null) {
                movesList.add(new ChessMove(myPosition,new ChessPosition(n-1,pieceColumn), null));
            } else if /*Blocked by opposite color*/ ((board.getPiece(new ChessPosition(n-1,pieceColumn))).getTeamColor() != teamColor) {
                movesList.add(new ChessMove(myPosition,new ChessPosition(n-1,pieceColumn), null));
            } else /*Blocked by same color*/ {
                break;
            }
        }
        for /*Rook moving right*/ (int n = pieceColumn; n < 8; n++) {
            if /*Not blocked*/ (board.getPiece(new ChessPosition(pieceRow,n+1)) == null) {
                movesList.add(new ChessMove(myPosition,new ChessPosition(pieceRow,n+1), null));
            } else if /*Blocked by opposite color*/ ((board.getPiece(new ChessPosition(pieceRow,n+1))).getTeamColor() != teamColor) {
                movesList.add(new ChessMove(myPosition,new ChessPosition(pieceRow,n+1), null));
            } else /*Blocked by same color*/ {
                break;
            }
        }
        for /*Rook moving left*/ (int n = pieceColumn; n > 1; n--) {
            if /*Not blocked*/ (board.getPiece(new ChessPosition(pieceRow,n-1)) == null) {
                movesList.add(new ChessMove(myPosition,new ChessPosition(pieceRow,n-1), null));
            } else if /*Blocked by opposite color*/ ((board.getPiece(new ChessPosition(pieceRow,n-1))).getTeamColor() != teamColor) {
                movesList.add(new ChessMove(myPosition,new ChessPosition(pieceRow,n-1), null));
            } else /*Blocked by same color*/ {
                break;
            }
        }
        return movesList;
    }

    public Collection<ChessMove> pawnMoves(ChessBoard board, ChessPosition myPosition,
                                           List<ChessMove> movesList, ChessPiece piece,
                                           int pieceRow, int pieceColumn,
                                           ChessGame.TeamColor teamColor) {


        if /*White team*/ (teamColor == ChessGame.TeamColor.WHITE) {
            if /*In bounds move*/ (pieceRow < 7) {
                if /*Not blocked*/ (board.getPiece(new ChessPosition(pieceRow+1,pieceColumn)) == null) {
                    movesList.add(new ChessMove(myPosition,new ChessPosition(pieceRow+1,pieceColumn), null));
                }
            }
            if /*Initial move*/ (pieceRow == 2) {
                if /*Not blocked*/ (board.getPiece(new ChessPosition(pieceRow+1,pieceColumn)) == null
                        && board.getPiece(new ChessPosition(pieceRow+2,pieceColumn)) == null) {
                    movesList.add(new ChessMove(myPosition,new ChessPosition(pieceRow+2,pieceColumn), null));
                }
            } else if /*Promotion*/ (pieceRow == 7) {
                if /*Not blocked*/ (board.getPiece(new ChessPosition(pieceRow+1,pieceColumn)) == null) {
                    movesList.add(new ChessMove(myPosition,new ChessPosition(pieceRow+1,pieceColumn), PieceType.QUEEN));
                    movesList.add(new ChessMove(myPosition,new ChessPosition(pieceRow+1,pieceColumn), PieceType.BISHOP));
                    movesList.add(new ChessMove(myPosition,new ChessPosition(pieceRow+1,pieceColumn), PieceType.KNIGHT));
                    movesList.add(new ChessMove(myPosition,new ChessPosition(pieceRow+1,pieceColumn), PieceType.ROOK));
                }
                if /*Left may be out of bounds*/ (pieceColumn != 1) {
                    if /*Capture left promote*/ (board.getPiece(new ChessPosition(pieceRow+1, pieceColumn-1)) != null) {
                        ChessPiece capturePiece = board.getPiece(new ChessPosition(pieceRow+1, pieceColumn-1));
                        if /*Capture is black*/ (capturePiece.getTeamColor() == ChessGame.TeamColor.BLACK) {
                            movesList.add(new ChessMove(myPosition, new ChessPosition(pieceRow+1, pieceColumn-1), PieceType.QUEEN));
                            movesList.add(new ChessMove(myPosition, new ChessPosition(pieceRow+1, pieceColumn-1), PieceType.BISHOP));
                            movesList.add(new ChessMove(myPosition, new ChessPosition(pieceRow+1, pieceColumn-1), PieceType.KNIGHT));
                            movesList.add(new ChessMove(myPosition, new ChessPosition(pieceRow+1, pieceColumn-1), PieceType.ROOK));
                        }
                    }
                }
                if /*Right may be out of bounds*/ (pieceColumn != 8) {
                    if /*Capture right promote*/ (board.getPiece(new ChessPosition(pieceRow+1, pieceColumn+1)) != null) {
                        ChessPiece capturePiece = board.getPiece(new ChessPosition(pieceRow+1, pieceColumn+1));
                        if /*Capture is black*/ (capturePiece.getTeamColor() == ChessGame.TeamColor.BLACK) {
                            movesList.add(new ChessMove(myPosition, new ChessPosition(pieceRow+1, pieceColumn+1), PieceType.QUEEN));
                            movesList.add(new ChessMove(myPosition, new ChessPosition(pieceRow+1, pieceColumn+1), PieceType.BISHOP));
                            movesList.add(new ChessMove(myPosition, new ChessPosition(pieceRow+1, pieceColumn+1), PieceType.KNIGHT));
                            movesList.add(new ChessMove(myPosition, new ChessPosition(pieceRow+1, pieceColumn+1), PieceType.ROOK));
                        }
                    }
                }
            }
            if /*Left may be out of bounds*/ (pieceColumn != 1 && pieceRow != 7) {
                if /*Capture left*/ (board.getPiece(new ChessPosition(pieceRow+1, pieceColumn-1)) != null) {
                    ChessPiece capturePiece = board.getPiece(new ChessPosition(pieceRow+1, pieceColumn-1));
                    if /* Capture is black*/ (capturePiece.getTeamColor() == ChessGame.TeamColor.BLACK) {
                        movesList.add(new ChessMove(myPosition, new ChessPosition(pieceRow+1, pieceColumn-1), null));
                    }
                }
            }
            if /*Right may be out of bounds*/ (pieceColumn != 8 && pieceRow != 7) {
                if /*Capture right*/ (board.getPiece(new ChessPosition(pieceRow+1, pieceColumn+1)) != null) {
                    ChessPiece capturePiece = board.getPiece(new ChessPosition(pieceRow+1, pieceColumn+1));
                    if /* Capture is black*/ (capturePiece.getTeamColor() == ChessGame.TeamColor.BLACK) {
                        movesList.add(new ChessMove(myPosition, new ChessPosition(pieceRow+1, pieceColumn+1), null));
                    }
                }
            }
        }
        else if /*Black team*/ (piece.getTeamColor() == ChessGame.TeamColor.BLACK) {
            if /*In bounds move*/ (pieceRow > 2) {
                if /*Not blocked*/ (board.getPiece(new ChessPosition(pieceRow-1,pieceColumn)) == null) {
                    movesList.add(new ChessMove(myPosition,new ChessPosition(pieceRow-1,pieceColumn), null));
                }
            }
            if /*Initial move*/ (pieceRow == 7) {
                if /*Not blocked*/ (board.getPiece(new ChessPosition(pieceRow-1,pieceColumn)) == null
                        && board.getPiece(new ChessPosition(pieceRow-2,pieceColumn)) == null) {
                    movesList.add(new ChessMove(myPosition,new ChessPosition(pieceRow-2,pieceColumn), null));
                }
            } else if /*Promotion*/ (pieceRow == 2) {
                if /*Not blocked*/ (board.getPiece(new ChessPosition(pieceRow-1,pieceColumn)) == null) {
                    movesList.add(new ChessMove(myPosition, new ChessPosition(pieceRow-1, pieceColumn), PieceType.QUEEN));
                    movesList.add(new ChessMove(myPosition, new ChessPosition(pieceRow-1, pieceColumn), PieceType.BISHOP));
                    movesList.add(new ChessMove(myPosition, new ChessPosition(pieceRow-1, pieceColumn), PieceType.KNIGHT));
                    movesList.add(new ChessMove(myPosition, new ChessPosition(pieceRow-1, pieceColumn), PieceType.ROOK));
                }
                if /*Left may be out of bounds*/ (pieceColumn != 1) {
                    if /*Capture left promote*/ (board.getPiece(new ChessPosition(pieceRow-1, pieceColumn-1)) != null) {
                        ChessPiece capturePiece = board.getPiece(new ChessPosition(pieceRow-1, pieceColumn-1));
                        if /*Capture is white*/ (capturePiece.getTeamColor() == ChessGame.TeamColor.WHITE) {
                            movesList.add(new ChessMove(myPosition, new ChessPosition(pieceRow-1, pieceColumn-1), PieceType.QUEEN));
                            movesList.add(new ChessMove(myPosition, new ChessPosition(pieceRow-1, pieceColumn-1), PieceType.BISHOP));
                            movesList.add(new ChessMove(myPosition, new ChessPosition(pieceRow-1, pieceColumn-1), PieceType.KNIGHT));
                            movesList.add(new ChessMove(myPosition, new ChessPosition(pieceRow-1, pieceColumn-1), PieceType.ROOK));
                        }
                    }
                }
                if /*Right may be out of bounds*/ (pieceColumn != 8) {
                    if /*Capture right promote*/ (board.getPiece(new ChessPosition(pieceRow-1, pieceColumn+1)) != null) {
                        ChessPiece capturePiece = board.getPiece(new ChessPosition(pieceRow-1, pieceColumn+1));
                        if /*Capture is white*/ (capturePiece.getTeamColor() == ChessGame.TeamColor.WHITE) {
                            movesList.add(new ChessMove(myPosition, new ChessPosition(pieceRow-1, pieceColumn+1), PieceType.QUEEN));
                            movesList.add(new ChessMove(myPosition, new ChessPosition(pieceRow-1, pieceColumn+1), PieceType.BISHOP));
                            movesList.add(new ChessMove(myPosition, new ChessPosition(pieceRow-1, pieceColumn+1), PieceType.KNIGHT));
                            movesList.add(new ChessMove(myPosition, new ChessPosition(pieceRow-1, pieceColumn+1), PieceType.ROOK));
                        }
                    }
                }
            }
            if /*Left may be out of bounds*/ (pieceColumn != 1 && pieceRow != 2) {
                if /*Capture left*/ (board.getPiece(new ChessPosition(pieceRow-1, pieceColumn-1)) != null) {
                    ChessPiece capturePiece = board.getPiece(new ChessPosition(pieceRow-1, pieceColumn-1));
                    if /* Capture is white*/ (capturePiece.getTeamColor() == ChessGame.TeamColor.WHITE) {
                        movesList.add(new ChessMove(myPosition, new ChessPosition(pieceRow-1, pieceColumn-1), null));
                    }
                }
            }
            if /*Right may be out of bounds*/ (pieceColumn != 8 && pieceRow != 2) {
                if /*Capture right*/ (board.getPiece(new ChessPosition(pieceRow-1, pieceColumn+1)) != null) {
                    ChessPiece capturePiece = board.getPiece(new ChessPosition(pieceRow-1, pieceColumn+1));
                    if /* Capture is white*/ (capturePiece.getTeamColor() == ChessGame.TeamColor.WHITE) {
                        movesList.add(new ChessMove(myPosition, new ChessPosition(pieceRow-1, pieceColumn+1), null));
                    }
                }
            }
        }
        return movesList;
    }
}
