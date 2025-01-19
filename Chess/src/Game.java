import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

class Game extends JPanel {
    private final JButton[][] square = new JButton[8][8];
    private final Piece[][] board = new Piece[8][8];
    private Piece selectedPiece = null;
    private int selectedCol = -1;
    private int selectedRow = -1;

    public Game() {

        // Layout della scacchiera
        setLayout(new BorderLayout());

        // Pannello della scacchiera
        JPanel chessBoard = new JPanel();
        chessBoard.setLayout(new GridLayout(8, 8));

        // Colori per le caselle
        Color lightColor = new Color(240, 217, 181);
        Color darkColor = new Color(181, 136, 99);

        // Genera la scacchiera
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                JButton cell = new JButton ();
                cell.setFocusPainted(false);
                cell.setBorderPainted(false);
                if ((row + col) % 2 == 0) {
                    cell.setBackground(lightColor);
                } else {
                    cell.setBackground(darkColor);
                }
                final int currentRow = row;
                final int currentCol = col;
                cell.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        handleCellClick(currentRow,currentCol);
                    }
                });
                square[row][col] = cell;
                chessBoard.add(cell);
            }
        }

        // Aggiungi la scacchiera al pannello principale
        add(chessBoard, BorderLayout.CENTER);

        //Pezzi Bianchi
        addPiece(new Rook(0,'a',1));
        addPiece(new Knight(0,'b',1));
        addPiece(new Bishop(0,'c',1));
        addPiece(new Queen(0,'d',1));
        addPiece(new King(0,'e',1));
        addPiece(new Bishop(0,'f',1));
        addPiece(new Knight(0,'g',1));
        addPiece(new Rook(0,'h',1));
        for (char i = 'a'; i < 'i'; i++) {
            addPiece(new Pawn(0,i,2));
        }

        renderPieces();
        //Pezzi Neri
        addPiece(new Rook(1,'a',8));
        addPiece(new Knight(1,'b',8));
        addPiece(new Bishop(1,'c',8));
        addPiece(new Queen(1,'d',8));
        addPiece(new King(1,'e',8));
        addPiece(new Bishop(1,'f',8));
        addPiece(new Knight(1,'g',8));
        addPiece(new Rook(1,'h',8));
        for (char i = 'a'; i < 'i'; i++) {
            addPiece(new Pawn(1,i,7));
        }

        renderPieces();
    }

    public void addPiece(Piece piece) {
        int row = 8-piece.getRank();
        int col = piece.getFile() - 'a';
        board[row][col] = piece;
    }

    public void renderPieces() {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                square[row][col].removeAll();
                Piece piece = board[row][col];
                if (piece != null) {
                    JLabel pieceLabel = new JLabel(getPieceIcon(piece));
                    square[row][col].add(pieceLabel);
                }
            }
        }
        revalidate();
        repaint();
    }

    private void handleCellClick(int row, int col) {
        Piece clickedPiece = board[row][col];

        if (selectedPiece == null) {
            if (clickedPiece != null) {
                selectedPiece = clickedPiece;
                selectedRow = row;
                selectedCol = col;
                square[row][col].setBackground(Color.RED);
            }
        }
        else {
            if (selectedPiece.isValidMove((char) ('a' + col), 8 - row)){
                board[selectedRow][selectedCol] = null;
                board[row][col] = selectedPiece;
                selectedPiece.setFile((char) ('a' + col));
                selectedPiece.setRank(8 - row);
                renderPieces();
            }
            resetCellColor(selectedRow,selectedCol);
            selectedPiece = null;
            selectedRow = -1;
            selectedCol = -1;
        }
    }

    private void resetCellColor(int row, int col) {
        Color lightColor = new Color(240, 217, 181);
        Color darkColor = new Color(181, 136, 99);
        square[row][col].setBackground((row + col) % 2 == 0 ? lightColor : darkColor);
    }


    private ImageIcon getPieceIcon(Piece piece) {
        String color = piece.getColor() == 0 ? "white" : "black";
        String type = piece.getClass().getSimpleName().toLowerCase();
        String path = "res/images/" + color + "/" + type + ".png";

        ImageIcon icon = new ImageIcon(path);
        Image scaled = icon.getImage().getScaledInstance(60, 60, java.awt.Image.SCALE_SMOOTH);
        return new ImageIcon(scaled);
    }
}