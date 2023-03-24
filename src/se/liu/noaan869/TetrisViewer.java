package se.liu.noaan869;

import javax.swing.*;
import java.awt.*;

public class TetrisViewer {
	
	private final JFrame frame;
	private final GUI imageIcon;
    private final JLabel score;
    private final JButton pause;
    private final JButton end;
    private final JMenuBar bar;

	 public void RenderGame() {
         frame = new JFrame();
         imageIcon = new GUI();
         bar = new JMenuBar();
         end = new JButton("Avsluta");
         pause = new JButton("Pause");
         score = new JLabel();
        
        //cat picture
        frame.setLayout(new GridLayout(1,1));
        frame.add(imageIcon);
        frame.setSize(700, 700);
        frame.setVisible(true);


        bar.add(end);
        bar.add(pause);
        bar.add(score);
        
        frame.setJMenuBar(bar);
        frame.setVisible(true);
        

    }
    public GUI getImageIcon() {
        return this.imageIcon;
     }
    public JLabel getScore() {
        return this.score;
    }
    public JButton getPause() {
        return this.pause;
    }

    public JButton getEnd() {
        return this.end;
    }
    public JMenuBar getBar() {
        return this.bar;
    }
	 
	public JFrame getFrame() {
		return frame;
	}
	
	public static boolean pauseGame() {
        return askUserPasued();
    }
	
	public static boolean showErrorMessage(String error) {
        return askUser(error + " Try again?");
    }
	
	public static void showScoreBoard(String score) {
		JOptionPane.showMessageDialog(null, score);
	}
	
	public static void gameOver() {
		JOptionPane.showMessageDialog(null, "GameOver!");
	}
	
	public static String nameInput(){
	        return JOptionPane.showInputDialog("What your name?");
	} 
	 //ask user from old project / lab.
	public static boolean shuldQuit(){
        return askUser("Quit?") && askUser("Relly?");
	}
	private static boolean askUser(String question){
	        return JOptionPane.showConfirmDialog(null, question,
	                "", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
	}
	private static boolean askUserPasued(){
        return JOptionPane.showConfirmDialog(null, "Game is Paused. UnPause?",
                "", JOptionPane.DEFAULT_OPTION) == JOptionPane.YES_OPTION;
	}
}
