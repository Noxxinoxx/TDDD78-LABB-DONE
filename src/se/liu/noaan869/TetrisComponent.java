package se.liu.noaan869;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

public class TetrisComponent extends JComponent implements BoardListener{

    private final Board board;

    private final int blockSize = 20;


    public TetrisComponent(Board board) {
        this.board = board;


    }

    public int getBlockSize() {
        return blockSize;
    }

    public void boardChanged(){
        repaint();
    }



    @Override
    protected void paintComponent(Graphics g) {
        System.out.println("repaint");
        super.paintComponent(g);
        final Graphics2D g2d = (Graphics2D) g;
        HashMap<SquareType, Color> colourMap = new HashMap<>();
        colourMap.put(SquareType.EMPTY, Color.WHITE);
        colourMap.put(SquareType.O, Color.YELLOW);
        colourMap.put(SquareType.Z, Color.RED);
        colourMap.put(SquareType.L, Color.ORANGE);
        colourMap.put(SquareType.T, Color.MAGENTA);
        colourMap.put(SquareType.S, Color.GREEN);
        colourMap.put(SquareType.J, Color.BLUE);
        colourMap.put(SquareType.I, Color.CYAN);
        colourMap.put(SquareType.OUTSIDE, Color.BLACK);
        

        int OFFSET = 2;

        for (int i = 0; i < board.getHeight(); i++) {
            for (int j = 0; j < board.getWidth(); j++) {
                SquareType type = board.getSquareAt(j, i);

                g2d.setColor(colourMap.get(type));
                
                g2d.fillRect((j - OFFSET)*blockSize, (i - OFFSET)*blockSize, blockSize, blockSize);

            }

        }

    }



}
