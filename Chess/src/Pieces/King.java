package Pieces;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

public class King implements Piece {
    private final String color;
    private final BufferedImage image;
    private boolean hasMoved;

    public King(String color, String imagePath) {
        this.color = color;
        this.image = loadImage(imagePath);
        this.hasMoved = false;
    }
    private BufferedImage loadImage(String path) {
        try {
            return ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream(path)));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    @Override
    public String getColor() {
        return color;
    }

    @Override
    public BufferedImage getImage() {
        return image;
    }

    @Override
    public boolean isValidMove(int startRank, char startCol, int endRank, char endCol, Piece[][] board) {
        int rankDiff = Math.abs(endRank - startRank);
        int fileDiff = Math.abs(endCol - startCol);

        /// Movimento normale: massimo 1 casella in ogni direzione
        if (rankDiff <= 1 && fileDiff <= 1) {
            return isDestinationValid(endRank, endCol, board);
        }

        /// Controllo Arrocco
        if (!hasMoved && rankDiff == 0 && (fileDiff == 2)) {
            return isValidCastling(startRank, startCol, endCol, board);
        }

        return false;
    }

    private boolean isDestinationValid(int endRank, char endCol, Piece[][] board) {
        Piece destinationPiece = board[endRank][endCol];
        return destinationPiece == null || !destinationPiece.getColor().equals(this.color);
    }

    private boolean isValidCastling(int rank, char startCol, char endCol, Piece[][] board) {
        /// Determina se è arrocco corto o lungo
        boolean isShortCastle = endCol > startCol;
        char rookCol = isShortCastle ? 'h' : 'a';
        char middleCol = isShortCastle ? 'f' : 'd';
        char castleCol = isShortCastle ? 'g' : 'c';

        /// Controllo se la Torre esiste e non si è mossa
        Piece rook = board[rank][rookCol];
        if (!(rook instanceof Rook) || ((Rook) rook).hasMovedState()) {
            return false;
        }

        /// Controllo se ci sono pezzi tra Re e Torre
        for (char col = (char) (Math.min(startCol, rookCol) + 1); col < Math.max(startCol, rookCol); col++) {
            if (board[rank][col] != null) {
                return false;
            }
        }

        /// Controllo che il Re non sia sotto scacco e non attraversi caselle attaccate
        if (isSquareAttacked(rank, startCol, board) || isSquareAttacked(rank, middleCol, board) || isSquareAttacked(rank, castleCol, board)) {
            return false;
        }

        return true;
    }

    private boolean isSquareAttacked(int rank, char file, Piece[][] board) {
        // Controllo attacchi di pedoni
        int direction = this.color.equals("white") ? -1 : 1;
        if (isValidPosition(rank + direction, (char) (file - 1)) && board[rank + direction][file - 1] instanceof Pawn &&
                !board[rank + direction][file - 1].getColor().equals(this.color)) {
            return true;
        }
        if (isValidPosition(rank + direction, (char) (file + 1)) && board[rank + direction][file + 1] instanceof Pawn &&
                !board[rank + direction][file + 1].getColor().equals(this.color)) {
            return true;
        }

        // Controllo attacchi del cavallo
        int[] knightMoves = {-2, -1, 1, 2};
        for (int dr : knightMoves) {
            for (int df : knightMoves) {
                if (Math.abs(dr) != Math.abs(df)) {
                    int newRank = rank + dr;
                    char newFile = (char) (file + df);
                    if (isValidPosition(newRank, newFile) && board[newRank][newFile] instanceof Knight &&
                            !board[newRank][newFile].getColor().equals(this.color)) {
                        return true;
                    }
                }
            }
        }

        // Controllo attacchi di Torre, Alfiere e Regina
        int[][] directions = {
                {-1, 0}, {1, 0}, {0, -1}, {0, 1},  // Torre (verticale e orizzontale)
                {-1, -1}, {-1, 1}, {1, -1}, {1, 1} // Alfiere (diagonali)
        };

        for (int[] dir : directions) {
            int r = rank + dir[0];
            char f = (char) (file + dir[1]);

            while (isValidPosition(r, f)) {
                Piece p = board[r][f];
                if (p != null) {
                    if (!p.getColor().equals(this.color) &&
                            ((p instanceof Rook && (dir[0] == 0 || dir[1] == 0)) ||
                                    (p instanceof Bishop && Math.abs(dir[0]) == Math.abs(dir[1])) ||
                                    (p instanceof Queen))) {
                        return true;
                    }
                    break; // C'è un pezzo, quindi blocca il percorso
                }
                r += dir[0];
                f = (char) (f + dir[1]);
            }
        }

        // Controllo attacchi del Re
        for (int dr = -1; dr <= 1; dr++) {
            for (int df = -1; df <= 1; df++) {
                if (dr != 0 || df != 0) {
                    int newRank = rank + dr;
                    char newFile = (char) (file + df);
                    if (isValidPosition(newRank, newFile) && board[newRank][newFile] instanceof King &&
                            !board[newRank][newFile].getColor().equals(this.color)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    private boolean isValidPosition(int rank, char file) {
        return rank >= 0 && rank < 8 && file >= 'a' && file <= 'h';
    }


    public void setMoved() {
        this.hasMoved = true;
    }

    public boolean isKingInCheck(String color, Piece[][] board) {
        /// Trova la posizione del Re
        int kingRank = -1;
        char kingFile = ' ';

        for (int r = 0; r < 8; r++) {
            for (char f = 'a'; f <= 'h'; f++) {
                Piece piece = board[r][f];
                if (piece instanceof King && piece.getColor().equals(color)) {
                    kingRank = r;
                    kingFile = f;
                    break;
                }
            }
        }

        if (kingRank == -1) {
            return false; /// Se il Re non viene trovato (non dovrebbe mai accadere)
        }

        /// Usa il metodo isSquareAttacked per controllare se il Re è sotto scacco
        return isSquareAttacked(kingRank, kingFile, board);
    }

    public boolean isCheckmate(String color, Piece[][] board) {
        if (!isKingInCheck(color, board)) {
            return false; /// Non è scacco, quindi non può essere scacco matto
        }

        /// Scansiona tutti i pezzi del colore dato e prova ogni mossa legale
        for (int r = 0; r < 8; r++) {
            for (char f = 'a'; f <= 'h'; f++) {
                Piece piece = board[r][f];
                if (piece != null && piece.getColor().equals(color)) {
                    for (int newRank = 0; newRank < 8; newRank++) {
                        for (char newFile = 'a'; newFile <= 'h'; newFile++) {
                            if (piece.isValidMove(r, f, newRank, newFile, board)) {
                                // Simula la mossa
                                Piece temp = board[newRank][newFile];
                                board[newRank][newFile] = piece;
                                board[r][f] = null;

                                boolean stillInCheck = isKingInCheck(color, board);

                                /// Ripristina la scacchiera
                                board[r][f] = piece;
                                board[newRank][newFile] = temp;

                                if (!stillInCheck) {
                                    return false; /// Almeno una mossa può salvare il Re
                                }
                            }
                        }
                    }
                }
            }
        }

        return true; /// Nessuna mossa può togliere lo scacco → Scacco Matto!
    }

}
