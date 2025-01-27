public class King extends Piece {
    private Boolean hasMoved = false;

    public King(int color, char x, int y) {
        super(color, x, y);
    }

    @Override
    public Boolean isValidMove(char File, int Rank, Piece[][] board) {
        int newRank = Math.abs(Rank - getRank());
        int newFile = Math.abs(File - getFile());

        if (newRank <= 1 && newFile <= 1 && (newRank + newFile > 0)) {
            Piece targetPiece = board[Rank - 1][File - 'a'];
            return targetPiece == null || targetPiece.getColor() != this.getColor();
        }

        return false;
    }

    public Boolean isInCheck(Piece[][] board) {
        for (Piece[] row : board) {
            for (Piece p : row) {
                if (p != null && p.getColor() != getColor() && p.isValidMove(getFile(), getRank(), board)) {
                    return true;
                }
            }
        }
        return false;
    }

    public Boolean CheckMate(Piece[][] board) {
        if (!isInCheck(board)) {
            return false;
        }

        for (int rankDiff = -1; rankDiff <= 1; rankDiff++) {
            for (int fileDiff = -1; fileDiff <= 1; fileDiff++) {
                if (rankDiff == 0 && fileDiff == 0) {
                    continue;
                }
                int newRank = getRank() + rankDiff;
                char newFile = (char) (getFile() + fileDiff);
                if (newRank >= 1 && newRank <= 8 && newFile >= 'a' && newFile <= 'h') {
                    if (isValidMove(newFile, newRank, board)) {
                        Piece targetPiece = board[newRank - 1][newFile - 'a'];
                        if (targetPiece == null || targetPiece.getColor() != getColor()) {
                            Piece[][] simulatedBoard = simulateMove(this, newFile, newRank, board);
                            if (!isInCheck(simulatedBoard)) {
                                return false;
                            }
                        }
                    }
                }
            }
        }

        return true;
    }

    private Piece[][] simulateMove(Piece p, char newFile, int newRank, Piece[][] board) {
        Piece[][] newBoard = new Piece[8][8];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                newBoard[i][j] = board[i][j];
            }
        }
        newBoard[p.getRank() - 1][p.getFile() - 'a'] = null;
        p.setRank(newRank);
        p.setFile(newFile);
        newBoard[newRank - 1][newFile - 'a'] = p;
        return newBoard;
    }

    private Boolean isSquareAttacked(char file, int rank, Piece[][] board) {
        for (Piece[] row : board) {
            for (Piece p : row) {
                if (p != null && p.getColor() != getColor() && p.isValidMove(file, rank, board)) {
                    return true;
                }
            }
        }
        return false;
    }

    public Boolean canShortCastle(Piece rook, Piece[][] board) {
        if (hasMoved || !(rook instanceof Rook) || ((Rook) rook).hasMoved()) {
            return false;
        }

        int currRank = getRank();
        char kingFile = getFile();

        if (rook.getFile() < kingFile || Math.abs(rook.getFile() - kingFile) != 3) {
            return false;
        }

        for (char file = (char) (kingFile + 1); file < rook.getFile(); file++) {
            if (board[currRank - 1][file - 'a'] != null || isSquareAttacked(file, currRank, board)) {
                return false;
            }
        }
        return true;
    }

    public Boolean canLongCastle(Piece rook, Piece[][] board) {
        if (hasMoved || !(rook instanceof Rook) || ((Rook) rook).hasMoved()) {
            return false;
        }

        int currentRank = getRank();
        char kingFile = getFile();
        char rookFile = rook.getFile();

        if (rookFile > kingFile || Math.abs(rookFile - kingFile) != 4) {
            return false;
        }

        for (char file = (char) (kingFile - 1); file > rookFile; file--) {
            if (board[currentRank - 1][file - 'a'] != null || isSquareAttacked(file, currentRank, board)) {
                return false;
            }
        }
        return true;
    }
}
