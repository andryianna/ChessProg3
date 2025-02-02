public class Rook extends Piece {
    public Rook(int color, char x, int y) {
        super(color,x,y);
    }

    private Boolean hasMoved = false;

    protected Boolean hasMoved() {
        return hasMoved;
    }

    @Override
    public Boolean isValidMove(char File, int Rank,Piece[][] board) {
        try{
            int fileInd = File - 'a';
            Piece target = board[Rank - 1][fileInd];
            if (getFile() == File || getRank() == Rank || target == null || target.getColor() != getColor()) {
                System.out.println("halo");
                return isPathClear(File,Rank,board);
            }
            return false;
        }
        catch(Exception e){
            System.out.println(e);
            return false;
        }
    }

    public boolean isPathClear(char targetFile, int targetRank, Piece[][] board) {
        try {
            int fileStep = (targetFile > getFile()) ? 1 : (targetFile < getFile()) ? -1 : 0;
            int rankStep = (targetRank > getRank()) ? 1 : (targetRank < getRank()) ? -1 : 0;

            char file = (char) (getFile() + fileStep);
            int rank = getRank() + rankStep;

            while (file != targetFile || rank != targetRank) {
                int fileIndex = targetFile - 'a';
                int rankIndex = targetRank - 1;

                // Ensure indices are within valid range
                if (fileIndex < 0 || fileIndex > 7 || rankIndex < 0 || rankIndex > 7) {
                    return false;
                }

                if (board[rankIndex][fileIndex] != null) {
                    System.err.println("Path blocked at: " + file + rank);
                    return false;
                }

                file += fileStep;
                rank += rankStep;
            }

            return true;
        } catch (Exception e) {
            return false;
        }
    }
}

