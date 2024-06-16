package game;

import game.exceptions.GameAlreadyExistsException;
import game.exceptions.InvalidScoreException;
import game.vo.Game;
import game.vo.Team;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ScoreBoardTest {

    private static final String HOME_TEAM = "Poland";
    private static final String HOME_TEAM_2 = "France";
    private static final String AWAY_TEAM = "German";
    private static final String AWAY_TEAM_2 = "Spain";

    private static final String INVALID_SCORE = "Provided score is incorrect";

    @Test
    void shouldStartNewGame() {
        //Given
        ScoreBoard scoreBoard = new ScoreBoard();

        //When
        scoreBoard.startGame(HOME_TEAM, AWAY_TEAM);

        //Then
        assertThat(scoreBoard.getCurrentGame()).isNotNull();

        assertThat(scoreBoard.getGames())
                .hasSize(1)
                .extracting(Game::getHomeTeam)
                .extracting(Team::getName)
                .containsExactlyInAnyOrder(HOME_TEAM);

        assertThat(scoreBoard.getGames())
                .extracting(Game::getHomeTeam)
                .extracting(Team::getScore)
                .containsExactlyInAnyOrder(0);

        assertThat(scoreBoard.getGames())
                .extracting(Game::getAwayTeam)
                .extracting(Team::getName)
                .containsExactlyInAnyOrder(AWAY_TEAM);

        assertThat(scoreBoard.getGames())
                .extracting(Game::getAwayTeam)
                .extracting(Team::getScore)
                .containsExactlyInAnyOrder(0);
    }

    @Test
    void shouldStartMoreThanOneNewGame() {
        //Given
        ScoreBoard scoreBoard = new ScoreBoard();

        //When
        scoreBoard.startGame(HOME_TEAM, AWAY_TEAM);
        scoreBoard.startGame(HOME_TEAM_2, AWAY_TEAM_2);

        //Then
        assertThat(scoreBoard.getGames())
                .hasSize(2)
                .extracting(Game::getHomeTeam)
                .extracting(Team::getName)
                .containsExactlyInAnyOrder(HOME_TEAM, HOME_TEAM_2);

        assertThat(scoreBoard.getGames())
                .extracting(Game::getAwayTeam)
                .extracting(Team::getName)
                .containsExactlyInAnyOrder(AWAY_TEAM, AWAY_TEAM_2);
    }

    @Test
    void shouldNotStartNewGame_when_gameAlreadyExists() {
        //Given
        ScoreBoard scoreBoard = new ScoreBoard();
        scoreBoard.startGame(HOME_TEAM, AWAY_TEAM);

        //When Then
        assertThatThrownBy(() -> scoreBoard.startGame(HOME_TEAM, AWAY_TEAM))
                .isInstanceOf(GameAlreadyExistsException.class)
                .hasMessageContaining(HOME_TEAM)
                .hasMessageContaining(AWAY_TEAM);
    }

    @Test
    void shouldFinishCurrentGame() {
        //Given
        ScoreBoard scoreBoard = new ScoreBoard();
        scoreBoard.startGame(HOME_TEAM, AWAY_TEAM);

        //When
        scoreBoard.finishGame();

        //Then
        assertThat(scoreBoard.getCurrentGame()).isNull();
    }

    @Test
    void shouldUpdateScore() {
        //Given
        ScoreBoard scoreBoard = new ScoreBoard();
        scoreBoard.startGame(HOME_TEAM, AWAY_TEAM);

        //When
        scoreBoard.updateScore(0, 1);

        //Then
        assertThat(scoreBoard.getGames())
                .hasSize(1)
                .extracting(Game::getHomeTeam)
                .extracting(Team::getScore)
                .containsExactlyInAnyOrder(0);

        assertThat(scoreBoard.getGames())
                .extracting(Game::getAwayTeam)
                .extracting(Team::getScore)
                .containsExactlyInAnyOrder(1);

        //When
        scoreBoard.updateScore(1, 1);

        //Then
        assertThat(scoreBoard.getGames())
                .hasSize(1)
                .extracting(Game::getHomeTeam)
                .extracting(Team::getScore)
                .containsExactlyInAnyOrder(1);

        assertThat(scoreBoard.getGames())
                .extracting(Game::getAwayTeam)
                .extracting(Team::getScore)
                .containsExactlyInAnyOrder(1);
    }

    @Test
    void shouldNotUpdateScore_when_ScoresAreIncorrect() {
        //Given
        ScoreBoard scoreBoard = new ScoreBoard();
        scoreBoard.startGame(HOME_TEAM, AWAY_TEAM);

        //When Then
        assertThatThrownBy(() -> scoreBoard.updateScore(10, 1))
                .isInstanceOf(InvalidScoreException.class)
                .hasMessage(INVALID_SCORE);

        assertThatThrownBy(() -> scoreBoard.updateScore(-2, 1))
                .isInstanceOf(InvalidScoreException.class)
                .hasMessage(INVALID_SCORE);

        assertThatThrownBy(() -> scoreBoard.updateScore(1, -1))
                .isInstanceOf(InvalidScoreException.class)
                .hasMessage(INVALID_SCORE);
    }

    @Test
    void shouldGiveSummaryWithCorrectedGames_when_DifferentTotalScore() {
        //Given
        ScoreBoard scoreBoard = new ScoreBoard();
        scoreBoard.startGame(HOME_TEAM, AWAY_TEAM);

        scoreBoard.startGame(HOME_TEAM_2, AWAY_TEAM_2);
        scoreBoard.updateScore(0, 1);

        //When Then
        assertThat(scoreBoard.getSummary())
                .hasSize(2)
                .extracting(Game::getHomeTeam)
                .extracting(Team::getName)
                .containsExactly(HOME_TEAM_2, HOME_TEAM);

        assertThat(scoreBoard.getSummary())
                .extracting(Game::getTotalScore)
                .containsExactly(1, 0);
        
        assertThat(scoreBoard.getSummary())
                .extracting(Game::getAwayTeam)
                .extracting(Team::getName)
                .containsExactly(AWAY_TEAM_2, AWAY_TEAM);
    }

    @Test
    void shouldGiveSummaryWithCorrectedGames_when_TheSameScore() throws InterruptedException {
        //Given
        ScoreBoard scoreBoard = new ScoreBoard();
        scoreBoard.startGame(HOME_TEAM, AWAY_TEAM);

        //Sleep only for test case due to the same date and time of creation the game
        Thread.sleep(2);
        scoreBoard.startGame(HOME_TEAM_2, AWAY_TEAM_2);

        //When Then
        assertThat(scoreBoard.getSummary())
                .hasSize(2)
                .extracting(Game::getHomeTeam)
                .extracting(Team::getName)
                .containsExactly(HOME_TEAM_2, HOME_TEAM);

        assertThat(scoreBoard.getSummary())
                .extracting(Game::getAwayTeam)
                .extracting(Team::getName)
                .containsExactly(AWAY_TEAM_2, AWAY_TEAM);
    }

    @Test
    void shouldResetScoreBoard() {
        //Given
        ScoreBoard scoreBoard = new ScoreBoard();
        scoreBoard.startGame(HOME_TEAM, AWAY_TEAM);

        //When2
        scoreBoard.resetScoreBoard();

        //Then
        assertThat(scoreBoard.getGames()).hasSize(0);
    }
}