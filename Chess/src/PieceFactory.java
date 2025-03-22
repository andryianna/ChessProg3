import Pieces.*;

public class PieceFactory {
    public static Piece createPiece(String type, String color) {
        String image = "";
        try {
            image += "images/" + color + "/"+ type.toLowerCase() + ".png";
        }
        catch (Exception e) {
            e.getMessage();
        }
         return switch (type.toLowerCase()){
            case "pawn" -> new Pawn(color,image);
            case "knight" -> new Knight(color,image);
            case "bishop" -> new Bishop(color,image);
            case "rook" -> new Rook(color,image);
            case "king" -> new King(color,image);
            case "queen" -> new Queen(color,image);
            default -> throw new IllegalArgumentException("Invalid type: " + type);
        };
    }
}