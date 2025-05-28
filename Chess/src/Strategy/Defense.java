package Strategy;

import GUI.ChessBoard;
import GUI.Game;
import Pieces.*;
import TurnObserver.TurnManager;

import java.util.Random;

public class Defense implements ComputerStrategy{

    /**
     * @param game -La partita attuale
     * @param board -La scacchiera in uso
     * @param color -Il colore del computer
     * @param turnManager -Il manager dei turni corrente
     * */
    @Override
    public Move chooseMove(Game game, ChessBoard board, String color, TurnManager turnManager){
        //1. Cattura se puo catturare
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                for (int toRow = 0; toRow < 8; toRow++) {
                    for (int toCol = 0; toCol < 8; toCol++) {
                        if (board.isUnderAttack(toRow,toCol,color) && game.movePiece(row,(char)(col+'a'),toRow,(char)(toCol+'a')))
                            return new Move(row, (char)(col + 'a'), toRow, (char)(toCol + 'a'));
                    }
                }
            }
        }

        //2. Nel 70% dei casi muove un pezzo che non puo essere catturato, nel 30% dei casi muove casualmente
        Random rand = new Random();
        int strategy = rand.nextInt(0,100);
        if (strategy <= 70){
            for (int row = 0; row < 8; row++) {
                for (int col = 0; col < 8; col++) {
                    Piece piece = board.getPiece(row,col);
                    for (int toRow = 0; toRow < 8; toRow++) {
                        for (int toCol = 0; toCol < 8; toCol++) {
                            if (piece.isValidMove(row,(char)(col+'a'),toRow,(char)(toCol+'a'),board)
                                && !isUnderAttackAfterMove(board,game,row,col,toRow,toCol,color))
                                return new Move(row, (char)(col + 'a'), toRow, (char)(toCol + 'a')) ;
                        }
                    }
                }
            }
        }
        return makeAMove(board,color);

    }

    private boolean isUnderAttack(ChessBoard board, int targetRow, int targetCol,String color){
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Piece attacker = board.getPiece(row, col);
                if (!(attacker instanceof Null)  && !attacker.color().equals(color)){
                    if (attacker.isValidMove(row, (char)(col + 'a'), targetRow, (char)(targetCol + 'a'), board)){
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean isUnderAttackAfterMove(ChessBoard board,Game game, int fromRow, int fromCol, int toRow, int toCol, String color){
        Piece[][] backup = board.getBoard();

        game.movePiece(fromRow, (char)(fromCol + 'a'), toRow, (char)(toCol + 'a'));

        boolean result = isUnderAttack(board,toRow,toCol,color);
        board.setBoard(backup);
        return result;
    }

    public Move makeAMove(ChessBoard board, String color){
        for (int fromRow = 0; fromRow < 8; fromRow++) {
            for (int fromCol = 0; fromCol < 8; fromCol++) {
                Piece piece = board.getPiece(fromRow, fromCol);
                if (!(piece instanceof Null) && piece.color().equals(color)){
                    for (int toRow = 0; toRow < 8; toRow++) {
                        for (int toCol = 0; toCol < 8; toCol++) {
                            if (piece.isValidMove(fromRow, (char)(fromCol + 'a'), toRow, (char)(toCol + 'a'), board)){
                                return new Move(fromRow, (char)(fromCol + 'a'), toRow, (char)(toCol + 'a'));
                            }
                        }
                    }
                }
            }
        }
        return null;
    }
}
