public class King extends Piece {
    private Boolean hasMoved = false;

    public King(int color, char x, int y) {
        super(color,x,y);
    }

    @Override
    public Boolean isValidMove(char File, int Rank){
        int newRank = Math.abs(Rank - getRank());
        int newFile = Math.abs(File - getFile());
        return (newRank <=1 && newFile <=1 && (newRank + newFile > 0));
    }
    public Boolean isInCheck(Piece[] opponents) {
        for(Piece p : opponents) {
            if (p.isValidMove(getFile(),getRank()))
                return true;
        }
        return false;
    }

    public Boolean CheckMate(Piece[] opponents, Piece[] pieces) {
        //Il re non é sotto scacco quindi non é scacco matto
        if (!isInCheck(opponents))
            return false;

        //Il re potrebbe muoversi su un altra cella
        for(int rankDiff = -1; rankDiff <= 1; rankDiff++) {
            for(int fileDiff = -1; fileDiff <= 1; fileDiff++) {
                if (rankDiff == 0 && fileDiff == 0)
                    continue;
                int newRank = getRank() + rankDiff;
                char newFile = (char) (getFile() + fileDiff);
                if (isValidMove(newFile,newRank)){
                    boolean isOccupied = false;
                    for (Piece p : pieces) {
                        if (p.getRank() == newRank && p.getFile() == newFile && p.getColor() == getColor()) {
                            isOccupied = true;
                            break;
                        }
                    }
                    if (!isOccupied)
                        return false;
                }
            }
        }

        //Dei pezzi possono bloccare l'attacco
        for (Piece p : pieces) {
            if (p.getColor() == getColor() && !(p instanceof King)){
                for (int rank = 1; rank <= 8; rank++) {
                    for (char file = 'a'; file <= 'h'; file++) {
                        if (isValidMove(file,rank)){
                            Piece[] simulatedMove = simulateMove(p, file, rank, pieces);
                            if (!isInCheck(getOpponentPieces(simulatedMove)))
                                return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    private Piece[] simulateMove(Piece p, char newFile, int newRank,Piece[] pieces) {
        Piece[] newboard = new Piece[pieces.length];
        for (int i = 0; i <= pieces.length; i++) {
            if (pieces[i] == p) {
                newboard[i] = p;
                newboard[i].setRank(newRank);
                newboard[i].setFile(newFile);
            }
            else
                newboard[i] = pieces[i];
        }
        return newboard;
    }

    private Piece[] getOpponentPieces(Piece[] pieces) {
        return java.util.Arrays.stream(pieces)
                .filter(p -> p.getColor() != getColor())
                .toArray(Piece[]::new);
    }

    //Condizioni Arrocco
    private Boolean isSquareAttacked(char file, int rank, Piece[] oppPieces) {
        for (Piece p : oppPieces) {
            if (p.isValidMove(file,rank))
                return true;
        }
        return false;
    }

    public Boolean canShortCastle(Piece rook, Piece[] pieces) {
        if (hasMoved || !(rook instanceof Rook) || ((Rook) rook).hasMoved()){
            return false;
        }

        int currRank = getRank();
        char kingFile = getFile();

        if (rook.getFile() < kingFile || Math.abs(rook.getFile() - kingFile) != 3){
            return false;
        }

        //Controlla le caselle tra la torre e il re se sono pulite
        for (char file = (char) (kingFile + 1); file <= rook.getFile(); file++){
            for (Piece p : pieces) {
                if (p.getFile() == file && p.getRank() == currRank)
                    return false;
                if (isSquareAttacked(file, currRank, pieces)) {
                    return false;
                }
            }
        }
        return true;
    }

    public Boolean canLongCastle(Piece rook, Piece[] pieces) {
        if (hasMoved || !(rook instanceof Rook) || ((Rook) rook).hasMoved()) {
            return false;
        }

        int currentRank = getRank();
        char kingFile = getFile();
        char rookFile = rook.getFile();

        if (rookFile > kingFile || Math.abs(rookFile - kingFile) != 4) {
            return false;
        }

        // Controlla le caselle tra la torre e il re se sono pulite
        for (char file = (char) (kingFile - 1); file > rookFile; file--) {
            for (Piece piece : pieces) {
                if (piece.getFile() == file && piece.getRank() == currentRank) {
                    return false;
                }
                if (isSquareAttacked(file, currentRank, pieces)) {
                    return false;
                }
            }
        }
        return true;
    }
}

