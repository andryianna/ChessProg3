package Pieces;

public class PieceFactory {
    public static void createPiece(String type, String color,int rank,char file,Piece [][]board) {
        if (rank <= 0 || rank > board.length || file < 'a' || file >= 'a' + board[0].length) {
            throw new IllegalArgumentException("Posizione fuori dai limiti: " + type + file + rank);
        }
        Piece piece = switch (type.toLowerCase()){
            case "pawn" -> new Pawn(color,rank,file);
            case "knight" -> new Knight(color,rank,file);
            case "bishop" -> new Bishop(color,rank,file);
            case "rook" -> new Rook(color,rank,file);
            case "king" -> new King(color,rank,file);
            case "queen" -> new Queen(color,rank,file);
            default -> throw new IllegalArgumentException("Invalid type: " + type);
        };
        board[rank - 1][file - 'a'] = piece;
    }
}