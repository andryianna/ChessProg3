package GUI;

import GameState.CheckState;
import GameState.CheckmateState;
import Pieces.Pawn;
import Pieces.Piece;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class GameLog {
    private final Game game;
    private final StringBuilder header = new StringBuilder();
    private final List<String> moves = new ArrayList<>();
    private final String fileName;
    private final LocalDate date = LocalDate.now();
    private int moveCount = 1;

    public GameLog(Game game, String fileName) {
        this.game = game;
        this.fileName = fileName.endsWith(".pgn") ? fileName : fileName + ".pgn";
        createHeader();
    }

    private void createHeader() {
        header.append("[Event \"").append(fileName).append("\"]\n");
        header.append("[Site \"localhost\"]\n");
        header.append("[Date \"").append(this.date).append("\"]\n");
        header.append("[Round \"1\"]\n");
        header.append("[White \"White\"]\n");
        header.append("[Black \"Black\"]\n");
        header.append("[Result \"*\"]\n\n");
    }

    /**
     * Aggiunge una mossa al log.
     */

    public String getAlgebraicNotation(ChessBoard board,Piece piece, int pieceCount,int fromX, char fromY, int toX, char toY) {
        String move = getPieceSymbol(piece);
        System.out.println("Mossa: " + move);
        /// Se trovo un altro pezzo nella stessa riga o colonna aggiungo riga o colonna di partenza
        if (board.foundOtherPieceinSameFile(fromX,fromY,piece))
            move += fromX;
        else if (board.foundOtherPieceinSameRank(fromX,fromY,piece))
            move += fromY;
        /// Se la scacchiera ha un pezzo in meno rispetto a prima, la mossa é una cattura
        else if (board.piecesCount() + 1 == pieceCount)
            move += "x";
        /// Altrimenti prendo solo casella di destinazione
        else
            move += toX + toY;
        if (((Pawn) piece).promoted())
        /// Se il re avversario viene messo sotto scacco aggiungi il +
        if (game.getState() instanceof CheckState)
            move += "+";

        /// Se il gioco finisce con scacco matto aggiungi # invece di +
        if (game.getState() instanceof CheckmateState)
            move += "#";

        return move;
    }
    public void addMove(ChessBoard board, Piece piece, int fromX, char fromY, int toX, char toY, int pieceCount) {
        String move = getAlgebraicNotation(board,piece,pieceCount,fromX,fromY,toX,toY);

        if (game.getTurnManager().isWhiteTurn()) {
            moves.add(moveCount++ + ". " + move);
        } else {
            int lastIndex = moves.size() - 1; // L'ultimo indice valido
            if (lastIndex >= 0) {
                String updated = moves.get(lastIndex) + " " + move;
                moves.set(lastIndex, updated); // Aggiorna l'ultima mossa
            } else {
                // Se non c'è ancora una mossa, inizializza e aggiungi
                moves.add(moveCount++ + ". " + move);
            }
        }
        System.out.println("Mossa aggiunta: " + move);

    }

    private String getPieceSymbol(Piece piece) {
        return switch (piece.getClass().getSimpleName()) {
            case "Knight" -> "N";
            case "Bishop" -> "B";
            case "Rook" -> "R";
            case "Queen" -> "Q";
            case "King" -> "K";
            default -> "";
        };
    }
    /**
     * Salva il log delle mosse e l’intestazione in un file .pgn
     */
    public void saveToFile() {
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(fileName))) {
            writer.write(header.toString());

            // Scrittura mossa per mossa
            int count = 0;
            for (String move : moves) {
                writer.write(move + " ");
                count++;
                if (count % 5 == 0) writer.newLine(); // Vai a capo ogni 5 mosse per leggibilità
            }

            writer.write("\n\n*"); // Risultato non ancora deciso
            System.out.println("Partita salvata in " + fileName);
        } catch (IOException e) {
            System.err.println("Errore nel salvataggio del file: " + e.getMessage());
        }
    }

    public List<String> loadFromFile(String pgnFilePath) {
        List<String> loadedMoves = new ArrayList<>();
        try {
            List<String> lines = Files.readAllLines(Paths.get(pgnFilePath));
            for (String line : lines) {
                // Salta l'header
                if (line.startsWith("[")) continue;
                if (line.trim().isEmpty()) continue;

                // Rimuove numeri di mossa e asterischi, split sui pezzi
                String[] tokens = line.trim().split("\\s+");
                for (String token : tokens) {
                    if (token.matches(".*\\d+\\..*")) continue; // es: "1.", "2."
                    if (token.equals("*")) continue;
                    loadedMoves.add(token);
                }
            }
            System.out.println("PGN caricato con successo: " + pgnFilePath);
        } catch (IOException e) {
            System.err.println("Errore durante il caricamento del file PGN: " + e.getMessage());
        }

        return loadedMoves;
    }

}
