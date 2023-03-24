package se.liu.noaan869;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static se.liu.noaan869.TetrisViewer.pauseGame;
import static se.liu.noaan869.TetrisViewer.shuldQuit;


public class Board {
    private SquareType[][] squares;
	private final int width;
	private final int height;
	private boolean gameOver = false;
	private boolean pausedGame = false;
	private Poly falling = null;
	private int fallingx; 
	private int fallingy;

    private int speed = 500;
    private final List<BoardListener> listeners = new ArrayList<>();
	
	private int score = 0;

    private final TetrisViewer tetrisViewer;

    private final TetrisComponent comp;

    private int FALLING_SPEED = 1;
    private int WIDTH_OFFSET = 4;
    private int HEIGHT_OFFSET = 4;

    private String collisionHandeler;
    private FallThrough fallThrough;
    private Heavy heavy;
	
	public Board(int width, int height) {
		this.width = width + WIDTH_OFFSET;
		this.height = height + HEIGHT_OFFSET;
		this.squares = new SquareType[this.height][this.width];
        this.comp = new TetrisComponent(this);
        this.tetrisViewer = new TetrisViewer();
        this.collisionHandeler = "default";
        this.fallThrough = new FallThrough();
        this.heavy = new Heavy();
        this.addBoardListener(this.comp);

        startGame();

	}
	private void emptyGamePlan() {
		System.out.println(width);
        for (int y = 0; y < getHeight(); y++) {
            for (int x = 0; x < getWidth() ; x++) {
                if (y < 2 || (y >= (getHeight() - 2) && y <= getHeight()) || x < 2 ||(x >= (getWidth() - 2) && x <= getWidth())) {
                    this.squares[y][x] = SquareType.OUTSIDE;
                } else this.squares[y][x] = SquareType.EMPTY;
            }
        }
    }
	private void removeRow(){
		int rowsdel = 0;
        int HEIGHT_DELETE = 2;
        int WIDTH_DELETE = 2;
		for(int y = 2; y < getHeight() - HEIGHT_DELETE; y++) {
    	   boolean full = true;
    	   for(int x = 2; x < getWidth() - WIDTH_DELETE; x++) {
    		   if(getSquareAt(x,y) == SquareType.EMPTY) {
    			   full = false;
    			   break;
    		   }
    	   }
    	   if (full){
               for (int j = 2; j < getWidth() - WIDTH_DELETE; j++) {
            	   moveType(j,y, SquareType.EMPTY);
            	   
               }
               
              moveDownRows(y);
              rowsdel +=  1;
           }
    	   
    	   
       }
		scoreUpdater(rowsdel);
    }
    public boolean isRowCollapisble(int col, int collapseFrom) {
        for(int y = collapseFrom; y < height; y++) {
            if(getSquareAt(col, y) == SquareType.EMPTY) {
                return true;
            }
        }
        return false;
    }

    public void collapseRow(int col, int collapseFrom) {

        int firstEmptyPos = 0;

        for(int row = collapseFrom; row < height; row++) {
            if(getSquareAt(col, row) == SquareType.EMPTY) {
                firstEmptyPos = row;
                break;
            }
        }

        for(int row = firstEmptyPos; collapseFrom - 1 < row; row--) {
            setSquaresAt(col, row, getSquareType(col, row - 1));
        }

        //Remove the square which we've collapsed down
        setSquaresAt(col, collapseFrom - 1, SquareType.EMPTY);
    }
	private void scoreUpdater(int rows) {
        int[] rowScores = new int[]{100, 300, 500, 800};
        if (rows >= 1 && rows <= 4) {
            score += rowScores[rows - 1];
        }
	}
	
	private void moveDownRows(int rowYPos) {
        int ROW_Y_POS = 1;
        int ROW_X_POS = 2;

		for(int y = rowYPos; y > 2 ; y--) {
     	   for(int x = 2; x < getWidth() - ROW_X_POS; x++) {
     		   
     		  moveType(x,y, getSquareAt(x, (y - ROW_Y_POS)));
     		
     	   }
        }
	}
	
    private void addFallingToBoard(){
        for (int i = 0; i < falling.getLength(); i++) {
            for (int j = 0; j < falling.getLength(); j++) {
                if(falling.getType(j,i) != SquareType.EMPTY){
                	setTypeAtPosOffset(j+getFallingx(), i+getFallingy(), falling.getType(j,i));
                }
            }

        }
       
    }
    public void rotate(){
        falling = falling.rotateRight();
    }

    private void setTypeAtPosOffset(int x, int y, SquareType type){
        int Y_OFFSET = 1;
        this.squares[y + Y_OFFSET][x] = type;
    }

    private void moveType(int x, int y, SquareType type){
        this.squares[y][x] = type;
    }

    public void moveLeft() {
        setFallingx(getFallingx()-FALLING_SPEED);
        if(hasCollision(collisionHandeler)) {
            setFallingx(getFallingx() + FALLING_SPEED);
        }
    }

    public void moveRight() {
        setFallingx(getFallingx()+FALLING_SPEED);
        if(hasCollision(collisionHandeler)) {
            setFallingx(getFallingx() - FALLING_SPEED);
        }
    }

    public void moveDown(){
        if (!hasCollision(collisionHandeler)){
            setFallingy(getFallingy() + FALLING_SPEED);
        }
    }

    private void makeFalling() {
    	int SCREEN_X_DIVIDER = 2;
        removeRow();
        //var tredje är heavy, fall, deafult.
        collisionHandeler = "fallthrough";

    	fallingy = 4;
    	falling = new TeroMaker().getRandomPoly();
        fallingx = getWidth() / SCREEN_X_DIVIDER;
    }
    
    
    
    public void startGame() {

        init();


    }
    
    public void setPausedGame(boolean value) {
    	pausedGame = value;
    }

    public Dimension getPreferredSize(){
        int B_SIZE = 20;
        int OFFSET = 4;
        int WIDTH_OFFSET = 3;
        int HEIGHT_OFFSET = 1;
        int dX = ((this.getWidth()-WIDTH_OFFSET)* B_SIZE)-OFFSET;
        int dY = ((this.getHeight()-HEIGHT_OFFSET)* B_SIZE)+OFFSET;
        return new Dimension(dX ,dY);
    }

    public void init() {
        emptyGamePlan();
        GUIActionsInit();
        tetrisViewer.RenderGame();
        JFrame frame = tetrisViewer.getFrame();
        new Input(this,frame);

        //game tick
        final Action doOneStep = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {

                frame.remove(tetrisViewer.getImageIcon());

                tetrisViewer.getScore().setText("Score: " + score);
                frame.add(comp, BorderLayout.CENTER);
                frame.setSize(getPreferredSize());
                tick();

            }
        };

        int DELAY = 5000;
        final javax.swing.Timer clockTimer = new javax.swing.Timer(speed, doOneStep);
        clockTimer.setCoalesce(true);
        clockTimer.setInitialDelay(DELAY);
        clockTimer.start();
        //_____________________________________________________//

        //Speed upgrade
        final Action speedAction = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                float SPEED_DIVIDER = 1.2f;
                int SPEED_MAX = 50;
                speed /= SPEED_DIVIDER;
                if(speed < SPEED_MAX) {
                    speed = SPEED_MAX;
                }
                clockTimer.setDelay(speed);
            }
        };

        int DELAY_TIMER = 60000;
        final javax.swing.Timer speedTimer = new javax.swing.Timer(DELAY_TIMER, speedAction);

        speedTimer.setCoalesce(true);
        speedTimer.setInitialDelay(DELAY);
        speedTimer.start();


    }


	public void tick() {


        if(!gameOver) {
        	if(!pausedGame) {
                if (getFalling() == null) {
                    makeFalling();
                    if (hasCollision(collisionHandeler)) {
                        gameOver = true;
                        //change to game over screen.
                        TetrisViewer.gameOver();
                        
                        //append name and score to hsList also shows the input name panel.
                        String name = TetrisViewer.nameInput();
                        Highscore highscore = new Highscore(name, getScore());
                    	HighscoreList currentHighscores = HighscoreList.getList();
                    	currentHighscores.add(highscore);
                    	
                    	
                    	//show the current highscore list
                    	TetrisViewer.showScoreBoard(currentHighscores.showList());
                		
                        
                        //dispose frame
                        this.tetrisViewer.getFrame().dispose();
                        
                        //make a task that 5 sec later restarts the game.
                        TimerTask restart = new TimerTask() {
                            public void run() {
                        		//remove cat image after timer init.
                            	startGame();    	
                        		
                            }
                        };
                        Timer timer = new Timer("Timer");
                        long delay = 5000L;
                        timer.schedule(restart, delay);
                        

                    }
                } else if (!hasCollision(collisionHandeler)) {
                    setFallingy(getFallingy() + FALLING_SPEED);

                } else {
                    
                    addFallingToBoard();

                    falling = null;

                }

        	}

            notifyListeners();
        }
    }


    private void GUIActionsInit() {


        this.tetrisViewer.getEnd().addActionListener(event -> {
            if(shuldQuit()){
                System.exit(0);
            }
        });
        this.tetrisViewer.getPause().addActionListener(event -> {
            setPausedGame(true);
            boolean value = pauseGame();
            if(value) {
                setPausedGame(false);
            }
        });
    }


    public boolean hasCollision(String colType) {

        if(colType == "default") {
            //see if there is an active falling tetromino.
            if (falling != null) {
                //loop over the poly that is falling
                for (int i = 0; i < falling.getLength(); i++) {
                    for (int j = 0; j < falling.getLength(); j++) {
                        //see if the poly is over not over an empty place.
                        if (falling.getType(j, i) != SquareType.EMPTY && getSquareType(j + getFallingx(), i + getFallingy() +2) != SquareType.EMPTY) {
                            return true;
                        }
                    }

                }
            }
            return false;
        }else if(colType == "fallthrough") {
            return fallThrough.hasCollision(this);
        }else if(colType == "heavy") {
            return heavy.hasCollision(this);
        }else{
            return false;
        }


	}
	public SquareType getSquareAt(int x, int y) {

        if (falling != null && fallingx <= x && x < fallingx + falling.getLength() && y < fallingy + falling.getLength() && fallingy <= y) {
            notifyListeners();
            SquareType type =  falling.getBlock()[x - fallingx][y - fallingy];
            if (type != SquareType.EMPTY){
                return type;
            }
        }
        notifyListeners();
        return getSquareType(x,y);

    }
	
	public int getScore() {
		return score;
	}


    public List<BoardListener> getBoardListeners() {
	        return listeners;
    }

    public void addBoardListener(BoardListener bl) {
        listeners.add(bl);

    }

    public void removeBoardListener(BoardListener bl) {
        listeners.remove(bl);

    }


    private void notifyListeners() {
        listeners.forEach(BoardListener::boardChanged);
    }

	public SquareType getSquareType(int x, int y) {
		return squares[y][x];
	}

    public void setSquaresAt(int x, int y, SquareType type) {
        squares[y][x] = type;
    }
    public void deleteSquaresAt(int x, int y) {
        squares[y][x] = SquareType.EMPTY;
    }

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}
	
	public Poly getFalling() {
		return falling;
	}

	public int getFallingx() {
		return fallingx;
	}


	public void setFallingx(int fallingx) {
		this.fallingx = fallingx;
	}


	public int getFallingy() {
		return fallingy;
	}


	public void setFallingy(int fallingy) {
		this.fallingy = fallingy;
	}
	
	
	
}
