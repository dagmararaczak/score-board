package game.exceptions;

public class GameAlreadyExistsException extends RuntimeException {

    private static final String GAME_ALREADY_EXIST = "This game was already registered ";

    public GameAlreadyExistsException(String homeTeam, String awayTeam) {
        super(GAME_ALREADY_EXIST + " " + homeTeam + "-" + awayTeam);
    }
}
