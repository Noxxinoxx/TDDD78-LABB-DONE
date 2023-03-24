package se.liu.noaan869;

public class FallThrough implements CollisionHandler{
    @Override public boolean hasCollision(Board board) {

        int fallingLength = board.getFalling().getLength();


        //see if the block collides with the outside
        for (int x = 0; x < fallingLength; x++) {
            for (int y = 0; y < fallingLength; y++) {
                SquareType type = board.getFalling().getType(y, x);
                int fallingX = board.getFallingx();
                int fallingY = board.getFallingy();
                SquareType squareType = board.getSquareAt(fallingX + y, fallingY + x + 2);
                if (type != SquareType.EMPTY && squareType == SquareType.OUTSIDE) {
                    return true;
                }
            }
        }

        //see if the block collides with the another poly block
        for (int x = 0; x < fallingLength; x++) {
            for (int y = 0; y < fallingLength; y++) {
                SquareType type = board.getFalling().getType(y, x);
                int fallingX = board.getFallingx();
                int fallingY = board.getFallingy();
                SquareType squareType = board.getSquareAt(fallingX + y, fallingY + x + 2);
                if (type != SquareType.EMPTY && squareType == SquareType.EMPTY) {
                    board.deleteSquaresAt(board.getFallingx() + y, board.getFallingy() + x);
                }
            }
        }

        return false;
    }

}
