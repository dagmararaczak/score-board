package game;

import game.comperators.GameTimeComparator;
import game.exceptions.GameAlreadyExistsException;
import game.exceptions.InvalidScoreException;
import game.vo.Game;
import game.vo.Team;

import java.util.*;
import java.util.stream.Collectors;

/**
 * The ScoreBoard class manages a collection of games, allowing for starting new games,
 * updating scores, finishing games, and providing a summary of all games on the score board.
 */
public class ScoreBoard {
    private final Set<Game> games = new HashSet<>();

    private Game currentGame;

    /**
     * Start new game and add the game to score board
     * In case the game already exists in score board throw GameAlreadyExistsException
     *
     * @param homeTeam home team name
     * @param awayTeam away team name
     */
    public void startGame(String homeTeam, String awayTeam) {
        if (isGameExists(homeTeam, awayTeam)) {
            throw new GameAlreadyExistsException(homeTeam, awayTeam);
        }
        this.currentGame = new Game(new Team(homeTeam), new Team(awayTeam));
        games.add(currentGame);
    }

    /**
     * Finish current game
     */
    public void finishGame() {
        this.currentGame = null;
    }

    /**
     * Update score for home and away team
     *
     * @param homeTeamScore absolute home team score
     * @param awayTeamScore absolute away team score
     */
    public void updateScore(int homeTeamScore, int awayTeamScore) {
        if (!shouldBeUpdatedScore(homeTeamScore, awayTeamScore)) {
            throw new InvalidScoreException();
        }

        this.currentGame.updateHomeTeamScore(homeTeamScore);
        this.currentGame.updateAwayTeamScore(awayTeamScore);
    }

    /**
     * Provide summary of score board
     *
     * @return summary of score board sorted by total score value and time of creation the game
     */
    public List<Game> getSummary() {
        return games.stream().sorted((Comparator
                        .comparing(Game::getTotalScore).reversed())
                        .thenComparing(new GameTimeComparator()))
                .collect(Collectors.toList());
    }

    /**
     * Clear score board
     */
    public void resetScoreBoard() {
        games.clear();
    }

    private boolean isGameExists(String homeTeam, String awayTeam) {
        return this.games.stream()
                .anyMatch(game -> Objects.equals(game.getHomeTeam().getName(), homeTeam)
                        && Objects.equals(game.getAwayTeam().getName(), awayTeam));
    }

    private boolean isCorrectUpdatedScore(int oldScore, int newScore) {
        return newScore == oldScore || newScore == oldScore + 1;
    }

    private boolean shouldBeUpdatedScore(int homeTeamScore, int awayTeamScore) {
        return Objects.nonNull(currentGame)
                && isCorrectUpdatedScore(this.currentGame.getHomeTeam().getScore(), homeTeamScore)
                && isCorrectUpdatedScore(this.currentGame.getAwayTeam().getScore(), awayTeamScore);
    }

    public Set<Game> getGames() {
        return games;
    }

    public Game getCurrentGame() {
        return currentGame;
    }
}
