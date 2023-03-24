package se.liu.noaan869;

import java.util.Comparator;

public class ScoreComp implements Comparator<Highscore> {

    public int compare(Highscore o1, Highscore o2) {
        return Integer.compare(o1.getScore(), o2.getScore());
    }
}
