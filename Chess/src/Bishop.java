public class Bishop extends Piece {
    public Bishop(int color, char x, int y) {
        super(color,x,y);
    }

    @Override
    public Boolean isValidMove(char File, int Rank, Piece[][] board){
        return Math.abs(File - getFile()) == Math.abs(Rank - getRank()) && isPathClear(File,Rank,board);
    }

    public boolean isPathClear(char File, int Rank, Piece[][] board){
        int fileDir = Integer.signum(File - getFile());
        int rankDir = Integer.signum(Rank - getRank());

        char currentFile = (char) (getFile() + fileDir);
        char currentRank = (char) (getRank() + rankDir);

        while(currentFile != File || currentRank != Rank){
            Piece currentPiece = board[currentRank - 1][currentFile - 'a'];
            if(currentPiece != null){
                return false;
            }
            currentFile += fileDir;
            currentRank += rankDir;
        }
        return true;
    }


}
