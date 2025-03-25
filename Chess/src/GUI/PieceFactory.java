package GUI;

import Pieces.*;

public class PieceFactory {
    public static void createPiece(String type, String color,int rank,char file,Piece [][]board) {
        if (rank < 0 || rank >= board.length || file < 'a' || file >= 'a' + board[0].length) {
            throw new IllegalArgumentException("Posizione fuori dai limiti: " + rank + file);
        }
        String image = "";
        try {
            image += "images/" + color + "/"+ type.toLowerCase() + ".png";
        }
        catch (Exception e) {
            e.getMessage();
            return;
        }
        Piece piece = switch (type.toLowerCase()){
            case "pawn" -> new Pawn(color,image,rank,file);
            case "knight" -> new Knight(color,image,rank,file);
            case "bishop" -> new Bishop(color,image,rank,file);
            case "rook" -> new Rook(color,image,rank,file);
            case "king" -> new King(color,image,rank,file);
            case "queen" -> new Queen(color,image,rank,file);
            default -> throw new IllegalArgumentException("Invalid type: " + type);
        };
        board[rank - 1][file - 'a'] = piece;
    }
}