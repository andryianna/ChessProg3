package Strategy;

import GUI.ChessBoard;
import GUI.Game;
import Pieces.*;
import TurnObserver.TurnManager;

import java.util.Random;

public class Attack implements ComputerStrategy{

    @Override
    public boolean chooseMove(Game game, ChessBoard board, String color, TurnManager turnManager) {
        //1. Cattura se puo catturare
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                for (int toRow = 0; toRow < 8; toRow++) {
                    for (int toCol = 0; toCol < 8; toCol++) {
                        if (isUnderAttack(board,toRow,toCol,color))
                            return game.movePiece(row, (char)(col + 'a'), toRow, (char)(toCol + 'a'));
                    }
                }
            }
        }

        //2. Nel 70% dei casi muove un pezzo che si avvicina al re avversario, nel 30% muove un pezzo che si avvicina alla regina
        Random rand = new Random();
        int strategy = rand.nextInt(0,100);
        King king = (King) board.findKing(color);
        Queen queen = (Queen) board.findQueen(color);
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                int distanceToKing = calculateDistanceToKing(king,board,row,col),distanceToQueen = calculateDistanceToQueen(queen,board,row,col);
                for (int toRow = 0; toRow < 8; toRow++) {
                    for (int toCol = 0; toCol < 8; toCol++) {
                        if (strategy <= 70 && !!simulateMove(row,col,toRow,toCol,board)){
                            return game.movePiece(row, (char)(col + 'a'), toRow, (char)(toCol + 'a'));
                        }
                        else if (!simulateMove(row,col,toRow,toCol,board)){
                            return game.movePiece(row, (char)(col + 'a'), toRow, (char)(toCol + 'a'));
                        }
                    }
                }
            }
        }
        return false;
    }

    private int calculateDistanceToKing(King king, ChessBoard board, int row, int col){
        int distance = 0;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (board.getPiece(i,j) instanceof King && board.getPiece(i,j).color().equals(king.color())){
                    distance = Math.max(distance,Math.abs(i-row)+Math.abs(j-col));
                }
            }
        }
        return distance;
    }

    private boolean simulateMove(int row, int col, int toRow, int toCol, ChessBoard board){
        return board.getPiece(row,col).isValidMove(row, (char)(col + 'a'), toRow, (char)(toCol + 'a'), board);
    }

    private int calculateDistanceToQueen(Queen queen,ChessBoard board,int row,int col){
        int distance = 0;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (board.getPiece(i,j) instanceof Queen && board.getPiece(i,j).color().equals(queen.color())){
                    distance = Math.max(distance,Math.abs(i-row)+Math.abs(j-col));
                }
            }
        }
        return distance;
    }

    private boolean isUnderAttack(ChessBoard board, int targetRow, int targetCol,String color){
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Piece attacker = board.getPiece(row, col);
                if (!(attacker instanceof Null) && !attacker.color().equals(color)) {
                    if (attacker.isValidMove(row, (char) (col + 'a'), targetRow, (char) (targetCol + 'a'), board)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
