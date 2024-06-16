package game.comperators;

import game.vo.Game;

import java.util.Comparator;

public class GameTimeComparator implements Comparator<Game> {
    @Override
    public int compare(Game game1, Game game2) {
        return game2.getTimeOfCreation().compareTo(game1.getTimeOfCreation());
    }
}
