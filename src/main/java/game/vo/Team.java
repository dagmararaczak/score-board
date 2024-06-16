package game.vo;

import java.util.concurrent.atomic.AtomicInteger;

public class Team {
    private final String name;

    private final AtomicInteger score;

    public Team(String name) {
        this.name = name;
        this.score = new AtomicInteger();
    }

    public void setScore(int score) {
        this.score.set(score);
    }

    public int getScore() {
        return score.get();
    }

    public String getName() {
        return name;
    }
}
