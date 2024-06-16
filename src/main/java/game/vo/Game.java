package game.vo;

import java.time.LocalDateTime;

public class Game {
    private final Team homeTeam;

    private final Team awayTeam;

    private final LocalDateTime timeOfCreation;

    public Game(Team homeTeam, Team awayTeam) {
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.timeOfCreation = LocalDateTime.now();
    }

    public synchronized void updateHomeTeamScore(int homeTeamScore) {
        this.homeTeam.setScore(homeTeamScore);
    }

    public synchronized void updateAwayTeamScore(int awayTeamScore) {
        this.awayTeam.setScore(awayTeamScore);
    }

    public Team getHomeTeam() {
        return homeTeam;
    }

    public Team getAwayTeam() {
        return awayTeam;
    }

    public LocalDateTime getTimeOfCreation() {
        return timeOfCreation;
    }

    /**
     * Provide total score which is sum of home and away team score
     *
     * @return total score
     */
    public int getTotalScore() {
        return this.getHomeTeam().getScore() + getAwayTeam().getScore();
    }
}
